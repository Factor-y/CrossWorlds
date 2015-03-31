package org.openntf.conferenceapp.ui.pages.login;

import java.io.Serializable;
import java.util.logging.Logger;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.authentication.AccessControl;
import org.openntf.conferenceapp.authentication.ConferenceMembershipService;
import org.openntf.conferenceapp.service.AttendeeFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

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
	
	private static Logger log = Logger.getLogger(LoginScreen.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -1486180911093854206L;
	private TextField userEmailField;
	private Button login;
	private LoginListener loginListener;
	private AccessControl accessControl;
	private ConferenceMembershipService membershipService;
	
	public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
		this.loginListener = loginListener;
		this.accessControl = accessControl;
		this.membershipService = new ConferenceMembershipService();
		
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

		userEmailField = new TextField("Email address");
//		username.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focus(FocusEvent event) {
//				TextField parent = (TextField) event.getComponent();
//				parent.setValue("");
//			}
//		});
		loginForm.addComponent(userEmailField);
		userEmailField.setWidth(15, Unit.EM);
		userEmailField.setInputPrompt("Your email here");
		userEmailField.setDescription("Use your email address to get a personal profile.");
		userEmailField.setRequired(true);
		userEmailField.setRequiredError("Email must be provided to get a profile");
		userEmailField.addValidator(new EmailValidator("The user id can only be email"));
		userEmailField.setId("username");
		
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
		
		if (! userEmailField.isValid()) {
			return;
		}
		
		String userEmail = userEmailField.getValue().toLowerCase();

		Attendee attendee = null;
		
		VaadinServletRequest req =  (VaadinServletRequest) VaadinService.getCurrentRequest();
		
		log.warning(Factory.getSession(SessionType.CURRENT).getEffectiveUserName());
		Factory.getSession(SessionType.CURRENT).clearIdentity();
		
		attendee = AttendeeFactory.getAttendeeByEmail(userEmail);
		
		if (attendee != null) {
			showNotification(new Notification("Great, you have a profile", "Check your email for the link to access the app",
					Notification.Type.HUMANIZED_MESSAGE));

			log.info("User profile exists for: " + userEmailField.getValue() + ", sending reminder invitation");
			
			String userIdentityToken = membershipService.generateAccessToken(userEmailField.getValue());
			membershipService.sendInvitationEmail(userEmail, userIdentityToken);
		} else {
			// Send email for creating the user profile
			
			log.info("User profile not found for: " + userEmailField.getValue() + ", sending invitation");
			
			showNotification(new Notification("Almost there", "<strong>Great !!!</strong><br><br>Check your email, a message is on it's way to help you setup a profile",
					Notification.Type.HUMANIZED_MESSAGE));
			userEmailField.focus();

			String userIdentityToken = membershipService.generateAccessToken(userEmailField.getValue());
			membershipService.sendInvitationEmail(userEmail, userIdentityToken);
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
