package org.openntf.conferenceapp.authentication;

import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.vaadin.server.VaadinService;

public class BasicAccessControlService implements AccessControl {
	
	private final static Logger log = Logger.getLogger(BasicAccessControlService.class.getName());
	
	@Override
	public boolean signIn(String userEmail) {
		
		log.info("signIn " + userEmail);
		
		if (userEmail == null || userEmail.isEmpty())
			// TODO: @Daniele, this needs to continue as Anonymous
			return false;

		// TODO: @Daniele otherwise log in as relevant user
		CurrentUser.set(userEmail);

		Cookie myCookie = new Cookie("conference-uid", userEmail);
		myCookie.setPath("/");
		myCookie.setMaxAge(24*60*60*31); // 1 month
		
		VaadinService.getCurrentResponse().addCookie(myCookie);
		
		log.info("clearing session identity");
		Factory.getSession(SessionType.CURRENT).clearIdentity();
		
		return true;
	}

	@Override
	public boolean isUserSignedIn() {
		return ! CurrentUser.get().isEmpty();
	}

	@Override
	public boolean isUserInRole(String role) {
		if ("admin".equals(role)) {
			// Only the "admin" user is in the "admin" role
			return getPrincipalName().equals("admin");
		}

		// All users are in all non-admin roles
		return true;
	}

	@Override
	public String getPrincipalName() {
		return CurrentUser.get();
	}

	@Override
	public void logout() {
		CurrentUser.set(null);
	}

}
