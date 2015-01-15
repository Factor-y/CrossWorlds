package org.openntf.xworlds.appservers.webapp;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.openntf.xworlds.appservers.webapp.config.DefaultXWorldsApplicationConfig;
import org.openntf.xworlds.appservers.webapp.config.XWorldsApplicationConfigurator;

/**
 * Application Lifecycle Listener implementation class XWorldsApplicationListener
 *
 */
public class XWorldsApplicationListener implements ServletContextListener {

	static final java.util.logging.Logger log = Logger.getLogger(XWorldsApplicationListener.class.getName());
	
	lotus.domino.NotesThread NotesController = null;
	
    /**
     * Default constructor. 
     */
    public XWorldsApplicationListener() {
    	Factory.println("XWorlds:AppListener","Starting Application Listener");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent appEvent) {

    	XWorldsManager xwm = XWorldsManager.getInstance();
    	
    	xwm.addApplication(appEvent.getServletContext().getServletContextName(),
    			appEvent.getServletContext().getContextPath(),
    			appEvent.getServletContext().getMajorVersion(),
    			appEvent.getServletContext().getMinorVersion()
    			);
    	
    	// Configure XWorlds for this application
    	
    	String appConfiguratorClass = null;
    	XWorldsApplicationConfigurator configurator = null;
    	if ((appConfiguratorClass = appEvent.getServletContext().getInitParameter("org.openntf.app.Configurator")) != null) {
    		try {
				configurator = (XWorldsApplicationConfigurator) Class.forName(appConfiguratorClass).newInstance();
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			} catch (InstantiationException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			}
    	} else {
    		configurator = new DefaultXWorldsApplicationConfig();
    	}
    	
		configurator.configure(appEvent.getServletContext());
		configurator.build();

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent appEvent) {
    	
    	XWorldsManager xwm = XWorldsManager.getInstance();
    	
    	xwm.removeApplication(appEvent.getServletContext().getServletContextName(),
    			appEvent.getServletContext().getContextPath(),
    			appEvent.getServletContext().getMajorVersion(),
    			appEvent.getServletContext().getMinorVersion()
    			);
    	        
    }
	
}
