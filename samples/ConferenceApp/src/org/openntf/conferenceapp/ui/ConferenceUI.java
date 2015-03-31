package org.openntf.conferenceapp.ui;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.authentication.AccessControl;
import org.openntf.conferenceapp.authentication.BasicAccessControlService;
import org.openntf.conferenceapp.authentication.ConferenceMembershipService;
import org.openntf.conferenceapp.ui.pages.MainScreen;
import org.openntf.conferenceapp.ui.pages.TraditionalView;
import org.openntf.conferenceapp.ui.pages.login.LoginScreen;
import org.openntf.conferenceapp.ui.pages.login.LoginScreen.LoginListener;
import org.openntf.conferenceapp.ui.pages.profile.ProfileCreationScreen;
import org.openntf.conferenceapp.ui.pages.profile.ProfileView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Viewport("user-scalable=no,initial-scale=1.0")
@SuppressWarnings("serial")
@Theme("conferenceApp")
public class ConferenceUI extends UI {

	private static Logger log = Logger.getLogger(ConferenceUI.class.getName());
	
	public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	private AccessControl accessControlService = new BasicAccessControlService();

	private ProfileView profileView;
		
	@Override
	protected void init(VaadinRequest request) {
		Responsive.makeResponsive(this);

		getPage().setTitle("[OpenNTF] Engage.ug 2015");

		if (request.getParameter("logout") != null) {
			accessControlService.logout();
		}
		
		/*
		 * When I get an access token I try to validate it,
		 * if valid I extract the user identity and lookup the attendee profile.
		 * 
		 * If the profile exists I move to the common navigation otherwhise I
		 * open the profile management view to create the Attendee profile
		 *  
		 */
		
		if (!accessControlService.isUserSignedIn()) {
			// User not logged in
			// check if has access token
			
			String accessToken = null;
			String attendeeEmail = null;
				
			// Check for an access token in the URL
			if ((accessToken = request.getParameter("accesstoken")) != null) {
				
				log.info("Access token is available on URL: " + accessToken);
				attendeeEmail = ConferenceMembershipService.getAttendeeEmailFromAccesTokenAccessToken(accessToken);
				
				Attendee attendee = ConferenceMembershipService.findUserProfileByEmail(attendeeEmail);
				
				if (attendee == null) {
					setContent(new ProfileCreationScreen(this,attendeeEmail));
				} else {
					accessControlService.signIn(attendeeEmail);
					showMainView();
				}
				
			} else {
				
				// otherwise check if has cookie, in case setup login
				for (Cookie cookie : request.getCookies()) {
					if (cookie.getName().equals("conference-uid")) {
						attendeeEmail = ConferenceMembershipService.getAttendeeEmailFromAccesTokenAccessToken(cookie.getValue());
						log.info("Performing loing based on cookie identity " + attendeeEmail);
						accessControlService.signIn(attendeeEmail);
						showMainView();
					}
				}
				
				if (attendeeEmail == null) {
					// otherwise goto loginscreen
					setContent(new LoginScreen(accessControlService, new LoginListener() {
						@Override
						public void loginSuccessful() {
							showMainView();
						}
					}));
				}
			}
		} else {
			// if loggedin goto mainview
			showMainView();
		}

	}

	public void showMainView() {
		
		addStyleName(ValoTheme.UI_WITH_MENU);
		
		// Load mainScreen and restore state
		setContent(new MainScreen(this));
		
		if ("".equals(getNavigator().getState())) {
			getNavigator().navigateTo(TraditionalView.VIEW_NAME);
		} else {
			getNavigator().navigateTo(getNavigator().getState());
		}
	}

	public static ConferenceUI get() {
		return (ConferenceUI) UI.getCurrent();
	}

	public AccessControl getAccessControl() {
		return accessControlService;
	}

}
