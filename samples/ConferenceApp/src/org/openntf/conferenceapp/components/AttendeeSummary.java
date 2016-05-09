package org.openntf.conferenceapp.components;

import org.openntf.conference.graph.Attendee;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AttendeeSummary extends HorizontalLayout {

	public AttendeeSummary() {

	}

	public AttendeeSummary(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	public void loadAttendeeSummary(Attendee att, int detailExpandRatio) {
		setMargin(true);
		Image photo = new Image("", new ExternalResource(att.getUrl()));
		photo.setHeight(80, Unit.PIXELS);
		VerticalLayout speakerDetails = new VerticalLayout();
		Label speakerName = new Label(att.getFirstName() + " " + att.getLastName());
		speakerName.setStyleName(ValoTheme.LABEL_H4);
		Label speakerBio = new Label(att.getProfile());
		speakerDetails.addComponents(speakerName, speakerBio);
		setDefaultComponentAlignment(Alignment.TOP_LEFT);
		addComponents(photo, speakerDetails);
		setExpandRatio(photo, 1);
		setExpandRatio(speakerDetails, detailExpandRatio);
		setSizeFull();
	}

}
