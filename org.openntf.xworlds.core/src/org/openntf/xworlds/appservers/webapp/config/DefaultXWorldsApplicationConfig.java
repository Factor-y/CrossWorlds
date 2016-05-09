package org.openntf.xworlds.appservers.webapp.config;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.session.INamedSessionFactory;
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

	private static final Logger log = Logger.getLogger(DefaultXWorldsApplicationConfig.class.getName());
	
	private boolean _isWASSecurityEnabled = false;
	private boolean _isDeveloperMode = false;
	private String _defaultDevelopmentUserName = null;
	private String _appSignerFullName = null;

	private static ThreadLocal<String> dominoFullName = new ThreadLocal<String>() {

		@Override
		protected String initialValue() {
			return "Anonymous";
		}
		
	};
	
	enum IdentityLocator {
		SIGNER,
		CURRENT
	}
	
	@SuppressWarnings("serial")
	private class XSPBasedNamedSessionFactory implements ISessionFactory, INamedSessionFactory {
		
		private boolean _isFullAccess = false;
		private IdentityLocator _identityLocator = null;		
		
		public XSPBasedNamedSessionFactory(boolean fullAccess, IdentityLocator locator) {
			this._isFullAccess = fullAccess;
			this._identityLocator = locator;
		}
		
		@Override
		public Session createSession() {

			String username = null;
			switch (_identityLocator) {
				case SIGNER:
					username = getAppSignerFullName();
					break;
				case CURRENT:
					username = getDominoFullName();
			}
			
			return createSession(username);
			
		}

		@Override
		public Session createSession(String username) {
			System.out.println("Username: " + username);
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
			}		}
	};
	
	private ISessionFactory namedSignerSessionFactory = new XSPBasedNamedSessionFactory(false,IdentityLocator.SIGNER);
	private ISessionFactory namedSignerSessionFactoryFullAccess = new XSPBasedNamedSessionFactory(true,IdentityLocator.SIGNER);

	private ISessionFactory currentUserSessionFactory = new XSPBasedNamedSessionFactory(false,IdentityLocator.CURRENT);
	private ISessionFactory currentUserSessionFactoryFullAccess = new XSPBasedNamedSessionFactory(true,IdentityLocator.CURRENT);

	
	@Override
	public String getAppSignerFullName() {
		return _appSignerFullName;
	}

	@Override
	public boolean isDeveloperMode() {
		return _isDeveloperMode;
	}

	public void configure(ServletContext context) {
	
		// Save the current application context
		this.setAppContext(context);
		// Get from the server the current security mode
		this._isWASSecurityEnabled = WSSecurityHelper.isServerSecurityEnabled();
	
		// Read the signer identity
		this._appSignerFullName = context.getInitParameter(CONTEXTPARAM_CWAPPSIGNER_IDENTITY);


		// TODO Enable developermode discovery and log it
		// TODO add Notes.ini developer mode configuration
		if ("true".equals(System.getProperty("xworlds.developermode"))) {
			this._isDeveloperMode = true;
			log.warning("CrossWorlds development mode is enabled trough system property \"xworlds.developermode=true\"");
			
			// Read the development time identity
			this._defaultDevelopmentUserName = context.getInitParameter(CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY);
			if (_defaultDevelopmentUserName == null) {
				_defaultDevelopmentUserName = "Anonymous";
			}
		}
		
	}

	@Override
		public void setupRequest(HttpServletRequest request, HttpServletResponse response) {
			
			// Set the session factories for the "CURRENT" session.
//			if (_isWASSecurityEnabled || isDeveloperMode()) {
//				// If security is enabled ("server" mode) generate named sessions based on the logged in user.
//				Factory.setSessionFactory(currentUserSessionFactory, SessionType.CURRENT);
//				Factory.setSessionFactory(currentUserSessionFactoryFullAccess, SessionType.CURRENT_FULL_ACCESS);
//			} else {
//				// If security not enabled NATIVE session is "Current ID"
//				Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);
//				Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT_FULL_ACCESS);
//			}
			Factory.setSessionFactory(currentUserSessionFactory, SessionType.CURRENT);
			Factory.setSessionFactory(currentUserSessionFactoryFullAccess, SessionType.CURRENT_FULL_ACCESS);
	
			// The behaviour for asSigner session is the same with security enabled or not.
			if (getAppSignerFullName() != null) {
				Factory.setSessionFactory(namedSignerSessionFactory, SessionType.SIGNER);
				Factory.setSessionFactory(namedSignerSessionFactoryFullAccess, SessionType.SIGNER_FULL_ACCESS);
			} else {
				Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER);
				Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER_FULL_ACCESS);
			}
						
			if (isDeveloperMode() && request.getSession(false) != null && request.getSession(false).getAttribute("xworlds.request.username") != null) {
				setDominoFullName((String) request.getSession(false).getAttribute("xworlds.request.username"));
			} else if (request.getSession(false) != null && request.getSession(false).getAttribute("xworlds.request.username") != null) {
				// TODO - Need to specify a different behaviour for developer mode and production to provide better security and avoid bad developer behaviour 
				setDominoFullName((String) request.getSession(false).getAttribute("xworlds.request.username"));
			} else if (isDeveloperMode()) {
				// If developer mode is enabled and no other override applies we fallback to the default development user name
				setDominoFullName(_defaultDevelopmentUserName);
			}
						
		}

	@Override
	public XWorldsApplicationConfiguration build() {
		getAppContext().setAttribute(APPCONTEXT_ATTRS_CWAPPCONFIG, this);
		return this;
	}

	public static String getDominoFullName() {
		return dominoFullName.get();
	}

	public static void setDominoFullName(String newDominoFullName) {
		dominoFullName.set(newDominoFullName);
	}

	@Override
	public String getDefaultDevelopmentUserName() {
		return _defaultDevelopmentUserName;
	}
	
}
