package org.openntf.conferenceapp.authentication;

import java.io.Serializable;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
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

	public LoginScreen(AccessControl accessControl, LoginListener loginListener) {
		this.loginListener = loginListener;
		this.accessControl = accessControl;
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

		TextField username = new TextField("Email address", "Anonymous");
		username.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				TextField parent = (TextField) event.getComponent();
				parent.setValue("");
			}
		});
		loginForm.addComponent(username);
		username.setWidth(15, Unit.EM);
		username.setDescription("Leave as Anonymous to continue anonymously or user your email address to get a personal profile.");
		CssLayout buttons = new CssLayout();
		buttons.setStyleName("buttons");
		loginForm.addComponent(buttons);

		buttons.addComponent(login = new Button("Enter the conference"));
		login.setDisableOnClick(true);
		login.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					login();
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

	private void login() {
		if (accessControl.signIn(username.getValue(), null)) {
			loginListener.loginSuccessful();
		} else {
			showNotification(new Notification("Login failed", "Please check your username and password and try again.",
					Notification.Type.HUMANIZED_MESSAGE));
			username.focus();
		}
	}

	private void showNotification(Notification notification) {
		// keep the notification visible a little while after moving the
		// mouse, or until clicked
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}

	public interface LoginListener extends Serializable {
		void loginSuccessful();
	}
}
