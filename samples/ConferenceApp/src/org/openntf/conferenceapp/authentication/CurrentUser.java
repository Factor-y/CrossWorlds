package org.openntf.conferenceapp.authentication;

import java.util.logging.Logger;

import org.openntf.xworlds.appservers.webapp.security.SecurityManager;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;

public final class CurrentUser {

	private static Logger log = Logger.getLogger(CurrentUser.class.getName());

	/**
	 * The attribute key used to store the username in the session.
	 */
	public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();

	private static String getUserFullName(String userEmail) {
		return "CN=" + userEmail + "/OU=ICONUK2015/O=ConferenceApp";
	}

	/**
	 * Returns the name of the current user stored in the current session, or an empty string if no user name is stored.
	 * 
	 * @throws IllegalStateException
	 *             if the current session cannot be accessed.
	 */
	public static String get() {
		String currentUser = (String) getCurrentRequest().getWrappedSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
		if (currentUser == null) {
			return "";
		} else {
			return currentUser;
		}
	}

	/**
	 * Sets the name of the current user and stores it in the current session. Using a {@code null} username will remove the username from the
	 * session.
	 * 
	 * @throws IllegalStateException
	 *             if the current session cannot be accessed.
	 */
	public static void set(String currentUserEmail) {

		if (currentUserEmail == null) {
			getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
			// Set domino application identity
			VaadinServletRequest s = (VaadinServletRequest) getCurrentRequest();
			SecurityManager.setDominoFullName(s.getHttpServletRequest(), "Anonymous");
			log.info("Identity set to anoymous");
		} else {
			getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUserEmail);
			// Set domino application identity
			VaadinServletRequest s = (VaadinServletRequest) getCurrentRequest();
			SecurityManager.setDominoFullName(s.getHttpServletRequest(), getUserFullName(currentUserEmail));
			log.info("Identity set to: " + SecurityManager.getDominoFullName(s.getHttpServletRequest()));
		}
	}

	private static VaadinRequest getCurrentRequest() {
		VaadinRequest request = VaadinService.getCurrentRequest();
		if (request == null) {
			throw new IllegalStateException("No request bound to current thread");
		}
		return request;
	}
}
