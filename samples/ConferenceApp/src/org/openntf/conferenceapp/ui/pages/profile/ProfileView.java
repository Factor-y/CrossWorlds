package org.openntf.conferenceapp.ui.pages.profile;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

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
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	public ProfileView() {
		
	}

}
