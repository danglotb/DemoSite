package shadow.shadower;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import shadow.explorer.Explorer;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

/**
 * Created by tdurieux
 */
public abstract class ProxyServlet extends org.eclipse.jetty.proxy.ProxyServlet {

	protected Explorer explorer;

	private String production;

	public ProxyServlet(String production, int port, Explorer explorer) {
		super();
		this.production = "https://" + production + ":" + port;
		this.explorer = explorer;
	}

	protected Request copyRequest(HttpServletRequest request, String rewrittenTarget) {
		Request proxyRequest1 = this.getHttpClient().newRequest(rewrittenTarget).method(request.getMethod()).version(HttpVersion.fromString(request.getProtocol()));
		this.copyRequestHeaders(request, proxyRequest1);
		this.addProxyHeaders(request, proxyRequest1);
		return proxyRequest1;
	}

	@Override
	protected String rewriteTarget(HttpServletRequest request) {
		return getTarget(request, production);
	}

	protected String getTarget(HttpServletRequest currentRequest, String target) {
		StringBuilder uri = new StringBuilder(target);
		String path = currentRequest.getRequestURI();
		if (target.endsWith("/")) {
			uri.setLength(uri.length() - 1);
		}
		String rest = path;
		if (rest != null && rest.length() > 0) {
			if (!rest.startsWith("/")) {
				uri.append("/");
			}
			uri.append(rest);
		}
		try {
			String query = currentRequest.getQueryString();
			if (query != null && "GET".equals(currentRequest.getMethod())) {
				String rewrittenURI = "://";
				if (uri.indexOf("/",
						uri.indexOf(rewrittenURI) + rewrittenURI.length())
						< 0) {
					uri.append("/");
				}

				uri.append("?").append(query);
			}
		} catch (NullPointerException e) {
			// ignore
		}

		URI rewrittenURI1 = URI.create(uri.toString()).normalize();
		return !this.validateDestination(rewrittenURI1.getHost(), rewrittenURI1.getPort()) ? null : rewrittenURI1.toString();
	}

	@Override
	protected HttpClient newHttpClient() {
		int selectors = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
		String value = this.getServletConfig().getInitParameter("selectors");
		if(value != null) {
			selectors = Integer.parseInt(value);
		}

		return new HttpClient(new HttpClientTransportOverHTTP(selectors), new SslContextFactory(true));
	}
}
