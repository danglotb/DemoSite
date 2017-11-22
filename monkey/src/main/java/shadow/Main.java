package shadow;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import shadow.budget.Budget;
import shadow.budget.NbRequestBudget;
import shadow.explorer.Explorer;
import shadow.explorer.HistoryExplorer;
import shadow.manager.Manager;
import shadow.manager.RandomManager;
import shadow.monkey.Monkey;
import shadow.monkey.RandomMonkey;
import shadow.oracle.CodeStatusOracle;
import shadow.oracle.Oracle;
import shadow.shadower.HistoryProxy;
import shadow.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bdanglot on 10/08/16.
 */
public class Main {

    public static boolean verbose = false;

    public static Logger logger;

    public static final String ADR_PROD = "172.17.0.1";
    public static final String LOCALHOST = "localhost";
    public static final int PORT = 8443;

    private static final List<Monkey> monkeys = new ArrayList<>();

    private static final int[] seeds = new int[20];

    static {
        Random rnd = new Random(PORT);
        for (int i = 0; i < seeds.length; i++) {
            seeds[i] = rnd.nextInt();
        }
    }

    public static Explorer explorer;

    public static int nbMonkey = 3;

    public static Budget budget;

    public static int initialBudget = 120;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT + 1);
        ServletContextHandler contextHandler = new ServletContextHandler();
        server.setHandler(contextHandler);
        Oracle oracle = new CodeStatusOracle();
        budget = new NbRequestBudget(120);
        Manager managerPerturbation = new RandomManager(ADR_PROD, 23);
        explorer = new HistoryExplorer(budget, oracle, managerPerturbation, ADR_PROD);
        HistoryProxy servlet = new HistoryProxy(ADR_PROD, PORT, explorer);
        contextHandler.addServlet(new ServletHolder(servlet), "/*");
        try {
            nbMonkey = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
            // ignored using default value
        }
        for (int i = 0; i < nbMonkey; i++) {
            monkeys.add(runMonkey(i));
        }
        try {
            server.start();
            View.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            for (Monkey monkey : monkeys) {
                monkey.quit();
            }
        }
    }

    private static Monkey runMonkey(int index) {
        Monkey monkey = new RandomMonkey(index, LOCALHOST + ":" + (PORT + 1), seeds[monkeys.size()]);
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    monkey.init();
                    while (true)
                        monkey.doRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            return monkey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
