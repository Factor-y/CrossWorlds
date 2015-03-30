package org.openntf.conferenceapp.ui.pages.profile;

import java.util.HashMap;
import java.util.Map;

import org.openntf.conferenceapp.service.AttendeeFactory;
import org.openntf.conferenceapp.ui.ConferenceUI;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class ProfileCreationScreen extends CustomComponent implements View {

	private ConferenceUI parentUI = null;
	
	private FormLayout formLayout;

	private TextField email    	= null;
	private TextField firstname	= null;
	private TextField lastname 	= null;
	private TextField Twitter  	= null;
	private ComboBox country  	= null;
	private TextField Facebook 	= null;
	private TextField Url      	= null;
	private TextField Phone    	= null;
	private TextField Profile  	= null;
	private TextField Role     	= null;

	private Button submitBtn;
	
	
	public ProfileCreationScreen(ConferenceUI conferenceUI, String userEmail) {
		
		this.parentUI = conferenceUI;
		
		System.out.println("Building ui");
		
		formLayout = new FormLayout();
		formLayout.setMargin(true);
		
		formLayout.addComponent((email = new TextField("Email", userEmail)));
		formLayout.addComponent((firstname = new TextField("Firstname")));
		formLayout.addComponent((lastname = new TextField("Lastname")));
		formLayout.addComponent((Twitter = new TextField("Twitter")));
		formLayout.addComponent((country = new ComboBox("Country")));
		formLayout.addComponent((Facebook = new TextField("Facebook")));
		formLayout.addComponent((Url = new TextField("Url")));
		formLayout.addComponent((Phone = new TextField("Phone")));
		formLayout.addComponent((Profile = new TextField("Profile")));
		formLayout.addComponent((Role = new TextField("Role")));
		
		country.setInputPrompt("Select your country");
		country.addItem("Italy");
		country.addItem("Belgium");
		country.addItem("UK");
		country.setRequired(true);
		country.setNewItemsAllowed(false);
		country.setNullSelectionAllowed(false);
		country.setFilteringMode(FilteringMode.STARTSWITH);

		email.setWidth(30, Unit.EM);
		email.setRequired(true);
		email.setReadOnly(true);
		
		firstname.setWidth(30,Unit.EM);
		firstname.setRequired(true);
		
		lastname.setWidth(30, Unit.EM);
		lastname.setRequired(true);
		
		Twitter.setWidth(30, Unit.EM);
		country.setWidth(30, Unit.EM);
		Facebook.setWidth(30, Unit.EM);
		Url.setWidth(30, Unit.EM);
		Phone.setWidth(30, Unit.EM);
		Profile.setWidth(30, Unit.EM);
		Role.setWidth(30, Unit.EM);
		
		formLayout.addComponent((submitBtn = new Button("Create my profile and access the conference >>>")));
				
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				firstname.validate();
				
				Map<String, Object> userProfile = new HashMap<String, Object>();

				userProfile.put("Email", email.getValue());
				userProfile.put("Firstname", firstname.getValue());
				userProfile.put("Lastname", lastname.getValue());
				userProfile.put("Twitter", Twitter.getValue());
				userProfile.put("Country", country.getValue());
				userProfile.put("Facebook", Facebook.getValue());
				userProfile.put("Url", Url.getValue());
				userProfile.put("Phone", Phone.getValue());
				userProfile.put("Profile", Profile.getValue());
				userProfile.put("Role", Role.getValue());
				
				AttendeeFactory.addAttendee(userProfile);
				
				System.out.println("Profile created");
				
				parentUI.showMainView();
			}
		});
		
		setCompositionRoot(formLayout);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
