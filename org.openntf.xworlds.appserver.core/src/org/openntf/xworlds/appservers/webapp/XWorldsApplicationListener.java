package org.openntf.xworlds.appservers.webapp;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;

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
    	System.out.println("Starting Application Listener");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent app) {

    	XWorldsManager xwm = XWorldsManager.getInstance();
    	
    	xwm.Startup();
    	
    	xwm.addApplication(app.getServletContext().getServletContextName(),
    			app.getServletContext().getContextPath(),
    			app.getServletContext().getMajorVersion(),
    			app.getServletContext().getMinorVersion()
    			);

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent app) {
    	
    	XWorldsManager xwm = XWorldsManager.getInstance();
    	
    	xwm.removeApplication(app.getServletContext().getServletContextName(),
    			app.getServletContext().getContextPath(),
    			app.getServletContext().getMajorVersion(),
    			app.getServletContext().getMinorVersion()
    			);
    	xwm.Shutdown();
    	        
    }
	
}
