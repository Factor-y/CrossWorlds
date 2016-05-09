package org.openntf.conferenceapp.ui.pages.profile;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.authentication.ConferenceMembershipService;
import org.openntf.conferenceapp.authentication.CurrentUser;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 * @author daniele.vistalli
 *
 *         This class implements a custom component used to show a user profile.
 *         In case the user profile is owned by the current user then editing
 *         features are made available.
 * 
 */
public class ProfileView extends CustomComponent implements View {

	// User photo
	// Email
	// Twitter handle
	// Name
	// Lastname

	public static final String VIEW_NAME = "myprofile";
	public static final String VIEW_DESC = "My Profile";

	BeanItem<Attendee> attendeeBean = null;
	private TextField emailField;
	
	@Override
	public void enter(ViewChangeEvent event) {

		System.out.println("Loading data for profile: " + CurrentUser.get());
		
		Attendee myUserProfile = ConferenceMembershipService.findUserProfileByEmail(CurrentUser.get());
				
		attendeeBean = new BeanItem<Attendee>(myUserProfile);
		emailField.setPropertyDataSource(attendeeBean.getItemProperty("Email"));
		
	}

	public ProfileView() {

		Panel profilePanel = new Panel();

		FormLayout profileForm = new FormLayout();
		profileForm.setMargin(true);

		profileForm.addComponent(emailField = new TextField("Identity"));

		profilePanel.setContent(profileForm);
		profilePanel.setSizeFull();
		
		setCompositionRoot(profilePanel);
	}

}
