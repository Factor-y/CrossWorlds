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
public class DefaultXWorldsApplicationConfig extends BaseXWorldsApplicationConfigurator {

	private boolean isWASSecurityEnabled = false;
	
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

	public void configure(ServletContext context) {

		// Save the current application context
		this.appContext = context;
		// Get from the server the current security mode
		this.isWASSecurityEnabled = WSSecurityHelper.isServerSecurityEnabled();
		
	}

	@Override
	public void setupRequest(ServletRequest request, ServletResponse response) {
		
		// Set the session factories for the "CURRENT" session.
		if (isWASSecurityEnabled) {
			Factory.setSessionFactory(namedSessionFactory, SessionType.CURRENT);
			Factory.setSessionFactory(namedSessionFactoryFullAccess, SessionType.CURRENT_FULL_ACCESS);

			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER_FULL_ACCESS);
		} else {
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT_FULL_ACCESS);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER_FULL_ACCESS);
		}
		
	}
	
}
