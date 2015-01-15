package org.openntf.xworlds.appservers.lifecycle;

import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class XWorldsManagedThread {

	static Logger log = Logger.getLogger(XWorldsManagedThread.class.getName());

	static ThreadLocal<Boolean> threadReadyForDomino = new ThreadLocal<Boolean>() {
		
		@Override
		protected Boolean initialValue() {
			return false;
		}
		
	};

	public static void setupAsDominoThread() {
		
		if (XWorldsManager.getInstance().isStarted()) {

			if (! Factory.isStarted()) { // Wait for ODA Factory to beready
				int timeout = 30; // Maximum wait time for Factory startup;
				while (! Factory.isStarted() && timeout > 0) {
					try {
						System.out.print(".");
						timeout--;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		
			if (threadReadyForDomino.get() == false) {
				log.info("Setting up this thread for domino " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
				Factory.initThread(Factory.STRICT_THREAD_CONFIG);
				
				// Override the default session factory.
				Factory.setSessionFactory(  Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);
				
				NotesThread.sinitThread();
				threadReadyForDomino.set(true);
			} else {
				log.severe("Domino already setup for thread " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
			}
		
		} else {
			System.out.println("The XWorldsManager has not yet been started. Check for information at ....");
		}
		
	}

	public static void shutdownDominoThread() {
		
		if (XWorldsManager.getInstance().isStarted()) {
			
			if (threadReadyForDomino.get() == true) {
				log.info("Shutting down this thread for domino " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
				NotesThread.stermThread();
				Factory.termThread();
				threadReadyForDomino.set(false);
			} else {
				log.severe("ERROR: Domino wasn't setup for thread " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
			}
		} else {
			System.out.println("The XWorldsManager has not yet been started. Check for information at ....");
		}

	}

}
