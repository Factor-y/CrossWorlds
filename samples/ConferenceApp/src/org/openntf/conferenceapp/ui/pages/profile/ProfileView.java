package org.openntf.conferenceapp.ui.pages.profile;

import org.openntf.conferenceapp.ui.ConferenceUI;
import org.openntf.conferenceapp.ui.pages.ErrorView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * @author daniele.vistalli
 *
 * This class implements a custom component used to show a user profile.
 * In case the user profile is owned by the current user then editing features are made available.
 * 
 */
public class ProfileView extends CustomComponent implements View {

	// User photo
	// Email
	// Twitter handle
	// Name
	// Lastname
	
	private static final String VIEW_NAME = "myprofile";

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	public ProfileView(ConferenceUI conferenceUI) {
		
		Panel viewContainer = new Panel();
		viewContainer.addStyleName("valo-content");
		viewContainer.setSizeFull();

		Navigator navigator = new Navigator(conferenceUI, viewContainer);
		navigator.setErrorView(ErrorView.class);
		navigator.addView(ProfileView.VIEW_NAME, this);

		viewContainer.setContent(new Label("Hello World"));
		
	}

}
