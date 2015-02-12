package org.openntf.xworlds.core;

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	public void start(BundleContext context) throws Exception {
		Factory.println("XWorlds", "XWorlds Server bundle starting v. " + context.getBundle().getVersion());

		Command xworldsAdminCmd = new Command();
		xworldsAdminCmd.setContext(context);
		
		context.registerService("java.lang.Object", xworldsAdminCmd, Command.PROPERTIES);
		
		XWorldsManager.getInstance().Startup();
		
	}

	public void stop(BundleContext context) throws Exception {
		Factory.println("XWorlds", "XWorlds Server bundle stopping");
		
		XWorldsManager.getInstance().Shutdown();
		
	}
}