package org.openntf.conferenceapp.ui.pages;

import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.components.AttendeeSummary;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SessionDetailsDialog extends Window {
	private static final long serialVersionUID = 1L;
	private String sessionTitle;
	private String sessionDesc;
	private Iterable<Attendee> speakers;

	public SessionDetailsDialog() {
		super("Session Details"); // Set window caption
		center();
	}

	public void loadContent() {
		// Some basic content for the window
		VerticalLayout content = new VerticalLayout();
		Label title = new Label(getSessionTitle());
		title.setStyleName(ValoTheme.LABEL_H3);
		content.addComponent(title);
		Label desc = new Label(getSessionDesc());
		desc.setContentMode(ContentMode.HTML);
		content.addComponent(desc);
		for (Attendee att : getSpeakers()) {
			AttendeeSummary speakerArea = new AttendeeSummary();
			speakerArea.loadAttendeeSummary(att, 9);
			content.addComponent(speakerArea);
		}

		content.setMargin(true);
		setContent(content);
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSessionDesc() {
		return sessionDesc;
	}

	public void setSessionDesc(String sessionDesc) {
		this.sessionDesc = sessionDesc;
	}

	public Iterable<Attendee> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(Iterable<Attendee> speakers) {
		this.speakers = speakers;
	}
}