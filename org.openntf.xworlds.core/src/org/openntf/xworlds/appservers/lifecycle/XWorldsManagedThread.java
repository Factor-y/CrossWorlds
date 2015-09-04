package org.openntf.xworlds.appservers.lifecycle;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class XWorldsManagedThread {

	private static Logger log = Logger.getLogger(XWorldsManagedThread.class.getName());

	private static class TvDominoRequest {
				
		private boolean ready = false;
		
		public void reset() {
			ready = false;
		}
		
		public boolean isReady() {
			return ready;
		}
		public void setReady(boolean readyForDomino) {
			this.ready = readyForDomino;
		}
		
	}
	
	static ThreadLocal<TvDominoRequest> XWorldsThreadState = new ThreadLocal<TvDominoRequest>() {
		
		@Override
		protected TvDominoRequest initialValue() {
			return new TvDominoRequest();
		}
		
	};

	public static void setupAsDominoThread(HttpServletRequest request) {
				
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
		
			if (XWorldsThreadState.get().isReady() == false) {
				log.fine("Setting up this thread for domino " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
				Factory.initThread(Factory.STRICT_THREAD_CONFIG);
				
				// Override the default session factory.
				Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);
				
				NotesThread.sinitThread();
				XWorldsThreadState.get().setReady(true);
			} else {
				log.severe("Domino already setup for thread " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
			}
		
		} else {
			log.severe("The XWorldsManager has not yet been started. Check for information at ....");
		}
		
	}

	public static void shutdownDominoThread() {
		
		if (XWorldsManager.getInstance().isStarted()) {
			
			if (XWorldsThreadState.get().isReady() == true) {
				log.fine("Shutting down this thread for domino " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
				NotesThread.stermThread();
				Factory.termThread();
				XWorldsThreadState.get().reset();
			} else {
				log.severe("ERROR: Domino wasn't setup for thread " + Thread.currentThread().getId() + " / " + Thread.currentThread().getName());
			}
		} else {
			log.severe("The XWorldsManager has not yet been started. Check for information at ....");
		}

	}
	
}
