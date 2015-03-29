package org.openntf.conferenceapp.authentication;

import java.io.Serializable;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.service.AttendeeFactory;
import org.openntf.xworlds.appservers.webapp.security.SecurityManager;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LoginScreen extends CssLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1486180911093854206L;
	private TextField username;
	private Button login;
	private LoginListener loginListener;
	private AccessControl accessControl;
	private ConferenceAuthenticationService conferenceAuthentication;
	
	public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
		this.loginListener = loginListener;
		this.accessControl = accessControl;
		this.conferenceAuthentication = new ConferenceAuthenticationService();
		
		buildUI();
	}

	private void buildUI() {
		addStyleName("login-screen");

		// login form, centered in the available part of the screen
		Component loginForm = buildLoginForm();

		// layout to center login form when there is sufficient screen space
		// - see the theme for how this is made responsive for various screen
		// sizes
		VerticalLayout centeringLayout = new VerticalLayout();
		centeringLayout.setStyleName("centering-layout");
		centeringLayout.addComponent(loginForm);
		centeringLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

		// information text about logging in
		CssLayout loginInformation = buildLoginInformation();

		addComponent(centeringLayout);
		addComponent(loginInformation);
	}

	private Component buildLoginForm() {
		FormLayout loginForm = new FormLayout();

		loginForm.addStyleName("login-form");
		loginForm.setSizeUndefined();
		loginForm.setMargin(false);
		loginForm.setHeight("160px");

		username = new TextField("Email address");
//		username.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focus(FocusEvent event) {
//				TextField parent = (TextField) event.getComponent();
//				parent.setValue("");
//			}
//		});
		loginForm.addComponent(username);
		username.setWidth(15, Unit.EM);
		username.setInputPrompt("Your email here");
		username.setDescription("Use your email address to get a personal profile.");
		username.setRequired(true);
		username.setRequiredError("Email must be provided to get a profile");
		username.addValidator(new EmailValidator("The user id can only be email"));
		username.setId("username");
		
		CssLayout buttons = new CssLayout();
		buttons.setStyleName("buttons");
		loginForm.addComponent(buttons);

		buttons.addComponent(login = new Button("Enter the conference"));
		login.setDisableOnClick(true);
		login.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					performAccessWithEmail();
				} finally {
					login.setEnabled(true);
				}
			}
		});
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		return loginForm;
	}

	private CssLayout buildLoginInformation() {
		CssLayout loginInformation = new CssLayout();
		loginInformation.setStyleName("login-information");
		Label loginInfoText = new Label("<h1>Engage.UG 2015</h1>"
				+ "You can access conference information as <strong>anonymous</strong> or <strong>get a personal profile</strong>.<br/>" + "<br/>"
				+ "Visit the app as anonymous or provide your email to <string>get an access link</string>.<br/>" + "<br/>"
				+ "The access link will set a cookie for you to easily (no password needed) access the app", ContentMode.HTML);
		loginInformation.addComponent(loginInfoText);
		return loginInformation;
	}

	private void performAccessWithEmail() {
		
		if (! username.isValid()) {
			return;
		}
		
		String userEmail = username.getValue().toLowerCase();
		String fullname = "CN=" + userEmail + "/OU=Engage2015/O=ConferenceAPP";

		Attendee attendee = null;
		
		VaadinServletRequest req =  (VaadinServletRequest) VaadinService.getCurrentRequest();
		System.out.println("Getting session for " + SecurityManager.getDominoFullName(req.getHttpServletRequest()));
		
//		FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
		attendee = AttendeeFactory.getAttendeeByEmail(userEmail);
		// TODO get the actual attendee vertex for the current user if it exists
		
		if (attendee != null) {
			System.out.println("User profile FOUND, accessing application " + attendee.asDocument().getUniversalID());
			// Profile exits, login is succesfull
			if (accessControl.signIn(fullname)) {
				loginListener.loginSuccessful();
			} else {
				showNotification(new Notification("Something is wrong", "We got your profile but login failed, something is definitely wrong",
						Notification.Type.HUMANIZED_MESSAGE));
			}
		} else {
			// Send email for creating the user profile
			
			System.out.println("User profile not found, sending invitation");
			
			showNotification(new Notification("Almost there", "<strong>Great !!!</strong><br><br>Check your email, a message is on it's way to help you setup a profile",
					Notification.Type.HUMANIZED_MESSAGE));
			username.focus();

			String userIdentityToken = conferenceAuthentication.generateAccessToken(username.getValue());
			System.out.println("Token: " + userIdentityToken);
			conferenceAuthentication.sendInvitationEmail(userEmail, userIdentityToken);
		}
		
	}

	private void showNotification(Notification notification) {
		// keep the notification visible a little while after moving the
		// mouse, or until clicked
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
		notification.setHtmlContentAllowed(true);
	}

	public interface LoginListener extends Serializable {
		void loginSuccessful();
	}
}
