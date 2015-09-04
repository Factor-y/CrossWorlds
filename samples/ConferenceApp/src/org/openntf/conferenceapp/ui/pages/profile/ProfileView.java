package org.openntf.conferenceapp.ui.pages.profile;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.authentication.ConferenceMembershipService;
import org.openntf.conferenceapp.authentication.CurrentUser;
import org.openntf.conferenceapp.components.TwitterTimelineComponent;
import org.openntf.conferenceapp.service.AttendeeFactory;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
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
	private TextField email    	= null;
	private TextField firstname	= null;
	private TextField lastname 	= null;
	private TextField twitter  	= null;
	private ComboBox country  	= null;
	private TextField Facebook 	= null;
	private TextField Url      	= null;
	private TextField Phone    	= null;
	private TextField Profile  	= null;
	private TextField Role     	= null;
	
	private Button submitBtn;
	
	private TwitterTimelineComponent twitterTimeline;

	@Override
	public void enter(ViewChangeEvent event) {

		
		Attendee myUserProfile = ConferenceMembershipService.findUserProfileByEmail(CurrentUser.get());
		
		attendeeBean = new BeanItem<Attendee>(myUserProfile);
		
		email.setPropertyDataSource(attendeeBean.getItemProperty("email"));
		firstname.setPropertyDataSource(attendeeBean.getItemProperty("firstName"));
		lastname.setPropertyDataSource(attendeeBean.getItemProperty("lastName"));
		country.setPropertyDataSource(attendeeBean.getItemProperty("country"));
		
		twitter.setPropertyDataSource(attendeeBean.getItemProperty("twitterId"));

		Role.setPropertyDataSource(attendeeBean.getItemProperty("role"));
		
		submitBtn.setEnabled(true);
		submitBtn.setCaption("Update my profile");
		
//		twitterTimeline.setTwitterId(myUserProfile.getTwitterId());
		
	}

	public ProfileView() {

		Panel profilePanel = new Panel();

		FormLayout profileForm = new FormLayout();
		profileForm.setMargin(true);

		profileForm.addComponent((email = new TextField("Email")));
		profileForm.addComponent((firstname = new TextField("Firstname")));
		profileForm.addComponent((lastname = new TextField("Lastname")));
		profileForm.addComponent((twitter = new TextField("Twitter")));
		profileForm.addComponent((country = new ComboBox("Country")));
		profileForm.addComponent((Facebook = new TextField("Facebook")));
		profileForm.addComponent((Url = new TextField("Url")));
		profileForm.addComponent((Phone = new TextField("Phone")));
		profileForm.addComponent((Profile = new TextField("Profile")));
		profileForm.addComponent((Role = new TextField("Role")));
		
		country.setInputPrompt("Select your country");
		country.addItem("Italy");
		country.addItem("Belgium");
		country.addItem("UK");
		country.setRequired(true);
		country.setNewItemsAllowed(false);
		country.setNullSelectionAllowed(false);
		country.setFilteringMode(FilteringMode.STARTSWITH);

		email.setWidth(40, Unit.EM);
		email.setRequired(true);
		email.setReadOnly(true);
		
		firstname.setWidth(30,Unit.EM);
		firstname.setRequired(true);
		
		lastname.setWidth(30, Unit.EM);
		lastname.setRequired(true);
		
		twitter.setWidth(30, Unit.EM);
		country.setWidth(30, Unit.EM);
		Facebook.setWidth(30, Unit.EM);
		Url.setWidth(30, Unit.EM);
		Phone.setWidth(30, Unit.EM);
		Profile.setWidth(30, Unit.EM);
		Role.setWidth(30, Unit.EM);
		
		profileForm.addComponent((submitBtn = new Button("Update my profile")));
		
//		profileForm.addComponent((twitterTimeline = new TwitterTimelineComponent(attendeeBean.getBean().getTwitterId())));

		submitBtn.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
								
//				twitterTimeline.setTwitterId(attendeeBean.getBean().getTwitterId());

				AttendeeFactory.commitGraph();
				Notification.show("Profile has been updated");
				
				submitBtn.setEnabled(false);
				submitBtn.setCaption("Profile is updated");
			}
		}
		);
		
		profilePanel.setContent(profileForm);
		profilePanel.setSizeFull();
		
		setCompositionRoot(profilePanel);
	}

}
