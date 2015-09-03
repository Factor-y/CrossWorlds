package org.openntf.xworlds.xots;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.IWrappedCallable;
import org.openntf.domino.thread.IWrappedRunnable;

public class XotsDominoExecutor extends DominoExecutor {
	private static final Logger log_ = Logger.getLogger(XotsDominoExecutor.class.getName());

	/**
	 * 
	 * @author Paul Withers
	 * 
	 */
	public static class XotsWrappedCallable<V> extends XotsWrappedTask implements IWrappedCallable<V> {

		public XotsWrappedCallable(final Callable<V> wrappedObject) {
			setWrappedTask(wrappedObject);
		}

		@SuppressWarnings("unchecked")
		@Override
		public V call() throws Exception {
			return (V) callOrRun();
		}
	}

	/**
	 * 
	 * @author Paul Withers
	 * 
	 */
	public static class XotsWrappedRunnable extends XotsWrappedTask implements IWrappedRunnable {

		public XotsWrappedRunnable(final Runnable wrappedObject) {
			setWrappedTask(wrappedObject);
		}

		@Override
		public void run() {
			try {
				callOrRun();
			} catch (Exception e) {
				log_.log(Level.SEVERE, "Could not execute " + "/" + getWrappedTask().getClass(), e);
			}
		}
	}

	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize, "Xots");
	}

	/**
	 * Helper for WrappedCallable/WrappedRunnable
	 * 
	 * @param ctx
	 * @param mcl
	 * @param wrapper
	 * @throws ServletException
	 */

	@Override
	protected <V> IWrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof IWrappedCallable)
			return (IWrappedCallable<V>) inner;

		if (inner instanceof XotsWrappedCallable<?>) {
			return new XotsWrappedCallable<V>(inner);
		}

		return new XotsWrappedCallable<V>(inner);
	}

	@Override
	protected IWrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof IWrappedRunnable)
			return (IWrappedRunnable) inner;

		if (inner instanceof XotsWrappedRunnable) {
			return new XotsWrappedRunnable(inner);
		}

		return new XotsWrappedRunnable(inner);
	}

}