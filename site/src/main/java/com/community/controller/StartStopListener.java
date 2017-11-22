package com.community.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import perturbation.rmi.PerturbationServer;
import perturbation.rmi.PerturbationServerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartStopListener{

	private static final String PATH = "/home/bdanglot/blc/BroadleafCommerce/common/src/main/java/org/broadleafcommerce/";

	private static final String ROOT_PACKAGE = "org.broadleafcommerce";

	private static PerturbationServer server;

	protected static final Log LOG = LogFactory.getLog(StartStopListener.class);

	public static void init() {
		try {
			LOG.info("===========================================================");
			LOG.info("=========               JPERTURB         ==================");
			LOG.info("===========================================================");
			PerturbationServerImpl.startServer(PATH, ROOT_PACKAGE);
			Thread.sleep(500);
			Registry registry = LocateRegistry.getRegistry(PerturbationServerImpl.PORT);
			server = (PerturbationServer) registry.lookup(PerturbationServerImpl.NAME_SERVER);
			LOG.info("Number of Locations in core:BLC-framework : " + server.getLocations().size());
			LOG.info("Servlet has been started.");
			LOG.info("===========================================================");
			LOG.info("=========               JPERTURB         ==================");
			LOG.info("===========================================================");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error when creating RMI object");
		}
	}
}
