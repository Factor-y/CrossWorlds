package org.openntf.conferenceapp.ui.pages.profile;

import java.util.HashMap;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.openntf.conferenceapp.service.AttendeeFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class ProfileCreationScreen extends CustomComponent implements View {

	private FormLayout formLayout;

	private TextField Email    	= null;
	private TextField Firstname	= null;
	private TextField Lastname 	= null;
	private TextField Twitter  	= null;
	private ComboBox Country  	= null;
	private TextField Facebook 	= null;
	private TextField Url      	= null;
	private TextField Phone    	= null;
	private TextField Profile  	= null;
	private TextField Role     	= null;

	private Button submitBtn;
	
	
	public ProfileCreationScreen(String userEmail) {
		
		System.out.println("Building ui");
		
		formLayout = new FormLayout();
		
		formLayout.addComponent((Email = new TextField("Email", userEmail)));
		formLayout.addComponent((Firstname = new TextField("Firstname")));
		formLayout.addComponent((Lastname = new TextField("Lastname")));
		formLayout.addComponent((Twitter = new TextField("Twitter")));
		formLayout.addComponent((Country = new ComboBox("Country")));
		formLayout.addComponent((Facebook = new TextField("Facebook")));
		formLayout.addComponent((Url = new TextField("Url")));
		formLayout.addComponent((Phone = new TextField("Phone")));
		formLayout.addComponent((Profile = new TextField("Profile")));
		formLayout.addComponent((Role = new TextField("Role")));
		
		Country.addItem("Italy");
		Country.addItem("Belgium");
		Country.addItem("UK");
		Country.setRequired(true);
		Country.setNewItemsAllowed(false);
		
		Email.setWidth(30, Unit.EM);
		Firstname.setWidth(30,Unit.EM);
		Lastname.setWidth(30, Unit.EM);
		Twitter.setWidth(30, Unit.EM);
		Country.setWidth(30, Unit.EM);
		Facebook.setWidth(30, Unit.EM);
		Url.setWidth(30, Unit.EM);
		Phone.setWidth(30, Unit.EM);
		Profile.setWidth(30, Unit.EM);
		Role.setWidth(30, Unit.EM);
		
		formLayout.addComponent((submitBtn = new Button("Create my profile")));
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Map<String, Object> userProfile = new HashMap<String, Object>();

				userProfile.put("Email", Email.getValue());
				userProfile.put("Firstname", Firstname.getValue());
				userProfile.put("Lastname", Lastname.getValue());
				userProfile.put("Twitter", Twitter.getValue());
				userProfile.put("Country", Country.getValue());
				userProfile.put("Facebook", Facebook.getValue());
				userProfile.put("Url", Url.getValue());
				userProfile.put("Phone", Phone.getValue());
				userProfile.put("Profile", Profile.getValue());
				userProfile.put("Role", Role.getValue());
				
				AttendeeFactory.addAttendee(userProfile);
				
				System.out.println("Profile created");
				
			}
		});
		
		setCompositionRoot(formLayout);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
