package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openntf.domino.Session;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.annotations.Beta;
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

	@SuppressWarnings("serial")
	private ISessionFactory namedSignerSessionFactory = new ISessionFactory() {
		
		@Override
		public Session createSession() {
			return Factory.getNamedSession(getAppSignerFullName(), false);
		}
	};

	@SuppressWarnings("serial")
	private ISessionFactory namedSignerSessionFactoryFullAccess = new ISessionFactory() {
		
		@Override
		public Session createSession() {
			return Factory.getNamedSession(getAppSignerFullName(), true);
		}
	};

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
			// If security is not enabled ("client / designer" mode)
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
