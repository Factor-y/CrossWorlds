package org.openntf.conferenceapp.ui.pages;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SessionDetailsDialog extends Window {
	private String sessionTitle;
	private String sessionDesc;
	private String speakers;

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
		Label speakers = new Label("Speakers: " + getSpeakers());
		speakers.setStyleName(ValoTheme.LABEL_SMALL);
		content.addComponent(speakers);

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

	public String getSpeakers() {
		return speakers;
	}

	public void setSpeakers(String speakers) {
		this.speakers = speakers;
	}
}