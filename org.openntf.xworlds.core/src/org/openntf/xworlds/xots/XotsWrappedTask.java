package org.openntf.xworlds.xots;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

import org.openntf.domino.Session;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractWrappedTask;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManagedThread;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

public class XotsWrappedTask extends AbstractWrappedTask {
	private Object wrappedTask;
	private String dominoFullName;
	private SessionType sessType;

	private class XotsNamedSessionFactory implements ISessionFactory, INamedSessionFactory {
		private static final long serialVersionUID = 1L;
		private boolean _isFullAccess = false;
		private String _dominoFullName;

		public XotsNamedSessionFactory(boolean fullAccess, String dominoFullName) {
			this._isFullAccess = fullAccess;
			this._dominoFullName = dominoFullName;
		}

		@Override
		public Session createSession() {

			return createSession(_dominoFullName);

		}

		@Override
		public Session createSession(String username) {
			try {

				final long userHandle = NotesUtil.createUserNameList(username);
				lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(username, userHandle, false, true, _isFullAccess);
				Session sess = Factory.fromLotus(rawSession, Session.SCHEMA, null);
				sess.setNoRecycle(false);
				return sess;
			} catch (NException e) {
				throw new RuntimeException(e);
			} catch (NotesException e) {
				throw new RuntimeException(e);
			}
		}
	};

	/**
	 * Common code for the wrappers
	 * 
	 * @param module
	 * @param bubbleException
	 * @param sessionFactory
	 * @param callable
	 * @param runnable
	 * @return
	 */
	protected Object callOrRun() throws Exception {

		try {
			XWorldsManagedThread.setupAsDominoThread(null);
			try {
				Object wrappedTask = getWrappedTask();

				Factory.setSessionFactory(new XotsNamedSessionFactory(false, dominoFullName), sessType);
				sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);

				if (wrappedTask instanceof Callable) {
					return ((Callable<?>) wrappedTask).call();
				} else {
					((Runnable) wrappedTask).run();
					return null;
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
				return null;
			}
		} finally {
			XWorldsManagedThread.shutdownDominoThread();
		}
	}

	/**
	 * Returns the wrapped task
	 * 
	 * @return
	 */
	protected synchronized Object getWrappedTask() {
		return wrappedTask;
	}

	/**
	 * Determines the sessionType under which the current runnable should run.
	 * The first non-null value of the following list is returned
	 * <ul>
	 * <li>If the runnable implements <code>IDominoRunnable</code>: result of
	 * <code>getSessionType</code></li>
	 * <li>the value of {@link SessionType} Annotation</li>
	 * <li>DominoSessionType.DEFAULT</li>
	 * </ul>
	 * 
	 * @param task
	 *            the runnable to determine the DominoSessionType
	 */
	protected synchronized void setWrappedTask(final Object task) {
		wrappedTask = task;
		if (task == null)
			return;
		// some security checks...
		if (task instanceof NotesThread) {
			// RPr: I'm not sure if this should be allowed anyway...
			throw new IllegalArgumentException("Cannot wrap the NotesThread " + task.getClass().getName() + " into a DominoRunner");
		}
		// if (task instanceof DominoFutureTask) {
		// // RPr: don't know if this is possible
		// throw new IllegalArgumentException("Cannot wrap the WrappedCallable "
		// + task.getClass().getName() + " into a DominoRunner");
		// }
		if (task instanceof AbstractWrappedTask) {
			// RPr: don't know if this is possible
			throw new IllegalArgumentException("Cannot wrap the WrappedCallable " + task.getClass().getName() + " into a DominoRunner");
		}

		if (task instanceof Tasklet.Interface) {
			Tasklet.Interface dominoRunnable = (Tasklet.Interface) task;
			sessionFactory = dominoRunnable.getSessionFactory();
			scope = dominoRunnable.getScope();
			context = dominoRunnable.getContext();
			sourceThreadConfig = dominoRunnable.getThreadConfig();
		}
		Tasklet annot = task.getClass().getAnnotation(Tasklet.class);

		if (annot != null) {
			if (sessionFactory == null) {
				switch (annot.session()) {
				case CLONE:
					sessType = SessionType.CURRENT;
					dominoFullName = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
					break;
				case CLONE_FULL_ACCESS:
					sessType = SessionType.CURRENT_FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
					break;

				case FULL_ACCESS:
					sessType = SessionType.FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case NATIVE:
					sessType = SessionType.NATIVE;
					dominoFullName = Factory.getSession(SessionType.NATIVE).getEffectiveUserName();
					break;

				case NONE:
					sessionFactory = null;
					break;

				case SIGNER:
					sessType = SessionType.SIGNER;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case SIGNER_FULL_ACCESS:
					sessType = SessionType.SIGNER_FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case TRUSTED:
					throw new IllegalStateException("SessionType.TRUSTED is not supported");

				default:
					break;
				}
			}

			if (context == null) {
				context = annot.context();
			}
			if (context == null) {
				context = Tasklet.Context.DEFAULT;
			}

			if (scope == null) {
				scope = annot.scope();
			}
			if (scope == null) {
				scope = Tasklet.Scope.NONE;
			}
			if (sourceThreadConfig == null) {
				switch (annot.threadConfig()) {
				case CLONE:
					sourceThreadConfig = Factory.getThreadConfig();
					break;
				case PERMISSIVE:
					sourceThreadConfig = Factory.PERMISSIVE_THREAD_CONFIG;
					break;
				case STRICT:
					sourceThreadConfig = Factory.STRICT_THREAD_CONFIG;
					break;
				}
			}
		}
		if (sourceThreadConfig == null)
			sourceThreadConfig = Factory.getThreadConfig();
	}

	/**
	 * Changes the Classloader and returns the old one
	 * 
	 * @param codeModule
	 * @return
	 */
	protected ClassLoader switchClassLoader(final ClassLoader newClassLoader) {
		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

			@Override
			public ClassLoader run() {
				Thread thread = Thread.currentThread();
				ClassLoader oldCl = thread.getContextClassLoader();
				thread.setContextClassLoader(newClassLoader);
				return oldCl;
			}
		});

	}

	/**
	 * Finds the constructor for the given Tasklet
	 * 
	 * @param clazz
	 * @param args
	 * @return
	 */
	protected Constructor<?> findConstructor(final Class<?> clazz, final Object[] args) {
		// sanity check if this is a public tasklet
		Tasklet annot = clazz.getAnnotation(Tasklet.class);
		if (annot == null) {
			throw new IllegalStateException("Cannot run " + clazz.getName() + ", because it does not annotate @Tasklet.");
		}

		// if (!(Callable.class.isAssignableFrom(clazz)) &&
		// !(Runnable.class.isAssignableFrom(clazz))) {
		// throw new IllegalStateException("Cannot run " + clazz.getName() + ",
		// because it is no Runnable or Callable.");
		// }

		// find the constructor
		Class<?> ctorClasses[] = new Class<?>[args.length];
		Object ctorArgs[] = new Object[args.length];
		for (int i = 0; i < ctorClasses.length; i++) {
			Object arg;
			ctorArgs[i] = arg = args[i];
			ctorClasses[i] = arg == null ? Null.class : arg.getClass();
		}

		Constructor<?> cTor = null;
		try {
			cTor = clazz.getConstructor(ctorClasses);
		} catch (NoSuchMethodException nsme1) {
			try {
				cTor = clazz.getConstructor(new Class<?>[] { Object[].class });
				ctorArgs = new Object[] { ctorArgs };
			} catch (NoSuchMethodException nsme2) {

			}
		}
		if (cTor == null) {
			throw new IllegalStateException("Cannot run " + clazz.getName() + ", because it has no constructor for Arguments: " + ctorArgs);
		}
		return cTor;
	}
}