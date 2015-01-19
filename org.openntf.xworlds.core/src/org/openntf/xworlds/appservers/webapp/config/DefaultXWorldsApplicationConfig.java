package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.annotations.Beta;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.websphere.security.WSSecurityHelper;


/**
 * The default configuration for a CrossWorlds Application is to use as CURRENT the following defaults:
 * 
 * <ol>
 * <li>If the appserver security is enabled -> CURRENT is a named session for the request subject (caller)</li>
 * <li>If the appserver security is disabled -> CURRENT is a native session with the identity of the USERID that's loaded</li>
 * </ol>
 * 
 * @author Daniele Vistalli
 *
 */
@Beta
public class DefaultXWorldsApplicationConfig extends BaseXWorldsApplicationConfigurator implements XWorldsApplicationConfiguration {

	private boolean _isWASSecurityEnabled = false;
	private String _appSignerFullName = null;

	@SuppressWarnings("serial")
	private class XSPBasedNamedSessionFactory implements ISessionFactory {
		
		private boolean _isFullAccess = false;
		
		
		public XSPBasedNamedSessionFactory(boolean fullAccess) {
			this._isFullAccess = fullAccess;
		}
		
		@Override
		public Session createSession() {

			try {
				final String username = getAppSignerFullName();
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

	@SuppressWarnings("serial")
	private ISessionFactory namedSessionFactory = new ISessionFactory() {
		
		@Override
		public Session createSession() {
			// TODO implement getting user identity from current thread
			return Factory.getNamedSession("", false);
		}
		
	};

	@SuppressWarnings("serial")
	private ISessionFactory namedSessionFactoryFullAccess = new ISessionFactory() {
		
		@Override
		public Session createSession() {
			// TODO implement getting user identity from current thread
			return Factory.getNamedSession("", true);
		}
		
	};
	
	private ISessionFactory namedSignerSessionFactory = new XSPBasedNamedSessionFactory(false);
	private ISessionFactory namedSignerSessionFactoryFullAccess = new XSPBasedNamedSessionFactory(true);

	public void configure(ServletContext context) {

		// Save the current application context
		this.setAppContext(context);
		// Get from the server the current security mode
		this._isWASSecurityEnabled = WSSecurityHelper.isServerSecurityEnabled();

		// Read the signer identity
		this._appSignerFullName = context.getInitParameter(CONTEXTPARAM_CWAPPSIGNER_IDENTITY);;
		
	}

	@Override
	public void setupRequest(ServletRequest request, ServletResponse response) {
		
		// Set the session factories for the "CURRENT" session.
		if (_isWASSecurityEnabled) {
			// If security is enabled ("server" mode)
			Factory.setSessionFactory(namedSessionFactory, SessionType.CURRENT);
			Factory.setSessionFactory(namedSessionFactoryFullAccess, SessionType.CURRENT_FULL_ACCESS);
		} else {
			
			// If security is not enabled ("client / designer" mode, use XSP api to create named sessions)
//			Factory.setNamedFactories4XPages(new INamedSessionFactory() {
//				
//				@Override
//				public Session createSession(String userName) {
//					// TODO implement better handling / encure this doesn't create issues.
//					
//					try {
//						System.out.println("Starting napi");
//						com.ibm.domino.napi.c.C.initLibrary(null);
//						System.out.println("Started napi");
//
//						final long userHandle = NotesUtil.createUserNameList(userName);
//						lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, false);
//						
//						System.out.println("Created session: " + rawSession.getUserName() + " / " + rawSession.getEffectiveUserName());
//						
//						Session sess = Factory.fromLotus(rawSession, Session.SCHEMA, null);
//						sess.setNoRecycle(false);
//						
//						System.out.println("Got session: " + sess.getUserName() + " / " + sess.getEffectiveUserName() + " / ");
//						
//						return sess;
//					} catch (NException e) {
//						throw new RuntimeException(e);
//					} catch (NotesException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}, new INamedSessionFactory() {
//				
//				@Override
//				public Session createSession(String userName) {
//					try {
//						final long userHandle = NotesUtil.createUserNameList(userName);
//						lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, false);
//						
//						Session sess = Factory.fromLotus(rawSession, Session.SCHEMA, null);
//						sess.setNoRecycle(false);
//	
//						return sess;
//					} catch (NException e) {
//						throw new RuntimeException(e);
//					} catch (NotesException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			});
			
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT_FULL_ACCESS);
			
		}

		// The behaviour for asSigner session is the same with security enabled or not.
		if (getAppSignerFullName() != null) {
			Factory.setSessionFactory(namedSignerSessionFactory, SessionType.SIGNER);
			Factory.setSessionFactory(namedSignerSessionFactoryFullAccess, SessionType.SIGNER_FULL_ACCESS);
		} else {
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER_FULL_ACCESS);
		}

		
	}

	@Override
	public String getAppSignerFullName() {
		return _appSignerFullName;
	}

	@Override
	public XWorldsApplicationConfiguration build() {
		getAppContext().setAttribute(APPCONTEXT_ATTRS_CWAPPCONFIG, this);
		return this;
	}
	
}
