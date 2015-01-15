package org.openntf.xworlds.core;

import java.util.Dictionary;
import java.util.Hashtable;

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class Command {

	static final Dictionary<String, Object> PROPERTIES = new Hashtable<String, Object>();

	static {
		// define the command scope as "jndi:"
		PROPERTIES.put("osgi.command.scope", "xworlds");
		// define the command names (which map to methods)
		PROPERTIES.put("osgi.command.function", "report counters install".split(" "));
	}

	private BundleContext context;

	public void report() {
		System.out.println("Reporting about the status of the XWorlds Server");
		System.out.println();
		
		XWorldsManager xwm = XWorldsManager.getInstance();
		
		System.out.println(xwm.getXWorldsReportAsString());
		
	}
	
	public void counters(boolean enable, boolean perThread) {
		System.out.println("Setting ODA countes");
		System.out.println("Enabled: " + enable);
		System.out.println("Count per thread: " + perThread);
		Factory.enableCounters(enable, perThread);
	}

	public void install(String packageToInstall) {
		System.out.println("Installing: " + packageToInstall);
		try {
			context.installBundle(packageToInstall);
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

}