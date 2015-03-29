package org.openntf.conferenceapp.ui;

import java.text.SimpleDateFormat;

import javax.servlet.http.Cookie;

import org.openntf.conferenceapp.authentication.AccessControl;
import org.openntf.conferenceapp.authentication.BasicAccessControl;
import org.openntf.conferenceapp.authentication.ConferenceAuthenticationService;
import org.openntf.conferenceapp.authentication.LoginScreen;
import org.openntf.conferenceapp.authentication.LoginScreen.LoginListener;
import org.openntf.conferenceapp.ui.pages.MainScreen;
import org.openntf.conferenceapp.ui.pages.TraditionalView;
import org.openntf.conferenceapp.ui.pages.profile.ProfileCreationScreen;

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

	public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	private AccessControl accessControl = new BasicAccessControl();
	private ConferenceAuthenticationService authenticationService = new ConferenceAuthenticationService();
		
	@Override
	protected void init(VaadinRequest request) {
		Responsive.makeResponsive(this);

		getPage().setTitle("[OpenNTF] Engage.ug 2015");

		if (request.getParameter("logout") != null) {
			accessControl.logout();
		}
		/*
		 * When I get an access token I try to validate it,
		 * if valid I extract the user identity and lookup the attendee profile.
		 * 
		 * If the profile exists I move to the common navigation otherwhise I
		 * open the profile management view to create the Attendee profile
		 *  
		 */
		
		if (!accessControl.isUserSignedIn()) {
			// User not logged in
			// check if has access token
			String accessToken = null;
			if ((accessToken = request.getParameter("accesstoken")) != null) {
				System.out.println("Getting an access token: " + accessToken);
				// Check if profile exists or goto profile creatin view
				String attendeeKey = authenticationService.getAttendeeKeyFromAccesTokenAccessToken(accessToken);
				System.out.println("UserIdentity: " + attendeeKey);
				setContent(new ProfileCreationScreen(attendeeKey));
			} else {
				String attendeeKey = null; 
				// otherwise check if has cookie
				for (Cookie cookie : request.getCookies()) {
					if (cookie.getName().equals("conference-uid")) {
						attendeeKey = authenticationService.getAttendeeKeyFromAccesTokenAccessToken(cookie.getValue());
						System.out.println("Attende identified by cookie: " + attendeeKey); 
					}
				}
				
				// otherwise goto loginscreen
				setContent(new LoginScreen(accessControl, new LoginListener() {
					@Override
					public void loginSuccessful() {
						showMainView();
					}
				}));
			}
		} else {
			// if loggedin goto mainview
			showMainView();
		}

	}

	protected void showMainView() {
		
		addStyleName(ValoTheme.UI_WITH_MENU);
		
		// Load mainScreen and restore state
		setContent(new MainScreen(ConferenceUI.this));
		
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
		return accessControl;
	}

}
