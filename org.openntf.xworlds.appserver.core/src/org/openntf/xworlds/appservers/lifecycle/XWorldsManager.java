package org.openntf.xworlds.appservers.lifecycle;

import java.lang.Thread.State;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.Factory;

/**
 * This Manager class acts as an appserver-wide management service to ensure ODA services are
 * just started once.<br>
 * <br>
 * The application listener embedded in every XWorlds enabled application will startup the engine by calling 
 * XWorldsManager.startup()<br>
 * <br>
 * The startup code will initialize application tracking code to list all enabled applications and to manage 
 * monitoring and service reporting and will:
 * <ul>
 * <li>Setup a map of initialized "servletContexts" using XWorlds servcices
 * <li>Startup Factory.startup() if not already done globally (managing concurrency)
 * <li>Startup a "locker" NotesThread to ensure domino is loaded for the whole execution of the application server.
 * This will eventually be replaced by a service similar to ODA's Xots to submit batchs. Ideally XWolds should also 
 * come to support Java Batch extensions as supported by WAS Libery. 
 * </ul>
 * </br>
 * The application listener will even manage XWorldsManager.shutdown() to reduce the number of loaded istances 
 * and to update monitoring<br>
 * 
 * 
 *  
 * 
 * @author Daniele Vistalli
 *
 */
public class XWorldsManager {

	private static Logger log = Logger.getLogger(XWorldsManager.class.getName());
	private static XWorldsManager _instance =null;
	
	private boolean _started = false;

	private AtomicInteger xwmStartedCount = new AtomicInteger(0);
	private AtomicInteger xwmRunningContextsCount = new AtomicInteger(0); 
	
	lotus.domino.NotesThread NotesLockerThread = null;
	
	public static XWorldsManager getInstance() {
		
		if (_instance == null) {
			synchronized (XWorldsManager.class) {
				if (_instance == null) {
					_instance = new XWorldsManager();
				}
			}
		}
		return _instance;
	}
	
	public boolean isStarted() {
		return _started;
	}
	
	public void Startup() {
		
		// Start OpenNTF Domino API Factory
		if (!Factory.isStarted() && !_started) {
			
			// Synchronize ODA Factory initialization
			synchronized (Factory.class) {
				
				if (!Factory.isStarted()) {
					
					System.out.println("Starting ODA " + Thread.currentThread().getName());
					
					Factory.startup();
					NotesThread.sinitThread();

					System.out.println("ODA startup in progress");
					
					System.out.println("Starting XWorlds locker thread");
					
					NotesLockerThread = new NotesThread(new Runnable() {
						@Override
						public void run() {
							boolean stopped = false;
							System.out
									.println("Starting up lock domino thread");
							lotus.domino.NotesThread.sinitThread();
							while (!stopped) {
								try {
									Thread.sleep(4000);
								} catch (InterruptedException e) {
									System.out
											.println("Shutting dowm lock domino thread");
									stopped = true;
								} finally {
									lotus.notes.NotesThread.stermThread();
								}

							}
						}
					});

					NotesLockerThread.setDaemon(true);
					NotesLockerThread.start();

					NotesThread.stermThread();

					_started = true;

					} else {
						log.severe("ODA's Factory failed to start");
					}
					
			}
		}
		
		xwmStartedCount.incrementAndGet();
				
	}
	
	public String getXWorldsReportAsString() {
		StringBuilder report = new StringBuilder();
		
		report.append("Started environments count: " );
		report.append(xwmStartedCount.get());
		report.append("\n");
		report.append("Running application contexts: " );
		report.append(xwmRunningContextsCount.get());
		report.append("\n");
		
		return report.toString();
	}

	public void addApplication(String servletContextName, String contextPath,
			int majorVersion, int minorVersion) {
		
		System.out.println("Adding " + servletContextName + " [" + contextPath + "] " + majorVersion + "." + minorVersion);
		
		xwmRunningContextsCount.incrementAndGet();
	}

	public void removeApplication(String servletContextName,
			String contextPath, int majorVersion, int minorVersion) {

		System.out.println("Removing " + servletContextName + " / " + contextPath + " " + majorVersion + "." + minorVersion);
		xwmRunningContextsCount.decrementAndGet();
	}

	public void Shutdown() {
		
		if (xwmStartedCount.decrementAndGet() == 0 && _started == true) {
			
			// On startedCount = 0 shutdown everything
	    	NotesLockerThread.interrupt();
	    	
	    	int secs = 0;
	    	while (NotesLockerThread.getState() != State.TERMINATED 
	    			&& secs < 10) {
	    		secs++;
	    		System.out.println("Waiting for domino locker thread to terminate [" + NotesLockerThread.getState() + "]");
	    		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
	    	}
	    	
	    	log.info("Shutting down ODA Factory");
	    	if (Factory.isStarted()) {
	    		log.info("Shutting down ODA on thread " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
	    		Factory.shutdown();
	    	}
	    	
	    	_started = false; 
		}
		
	}
	
}
