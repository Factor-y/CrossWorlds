package org.openntf.conferenceapp.ui.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Track;
import org.openntf.conferenceapp.components.AttendeeSummary;
import org.openntf.conferenceapp.service.AttendeeFactory;
import org.openntf.conferenceapp.service.TrackFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class Speakers extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Speakers";
	public static final String VIEW_DESC = "Speakers";
	private VerticalLayout content = new VerticalLayout();
	private List<HorizontalLayout> panels = new ArrayList<HorizontalLayout>();
	private HashMap<String, TreeMap<String, Attendee>> trackSpeakers = new HashMap<String, TreeMap<String, Attendee>>();
	private OptionGroup trackSelector = new OptionGroup();
	boolean contentLoaded = false;

	public Speakers() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (!contentLoaded) {
			loadContent();
		}
		contentLoaded = true;
	}

	public void loadContent() {
		try {
			getContent().setWidth(100, Unit.PERCENTAGE);
			getContent().setDefaultComponentAlignment(Alignment.TOP_CENTER);

			// Load speakers
			HashMap<String, TreeMap<String, Attendee>> tmpTrackSpeakers = new HashMap<String, TreeMap<String, Attendee>>();
			TreeMap<String, Attendee> allSpeakers = new TreeMap<String, Attendee>();
			for (Track track : TrackFactory.getTracksSortedByProperty("")) {
				TreeMap<String, Attendee> currTrackSpeakers = new TreeMap<String, Attendee>();
				for (Attendee att : AttendeeFactory.getSpeakers(track.getTitle())) {
					currTrackSpeakers.put(att.getFirstName() + " " + att.getLastName(), att);
					allSpeakers.put(att.getFirstName() + " " + att.getLastName(), att);
				}
				tmpTrackSpeakers.put(track.getTitle(), currTrackSpeakers);
				trackSelector.addItem(track.getTitle());
				trackSelector.setItemCaption(track.getTitle(), track.getDescription());
			}
			tmpTrackSpeakers.put("All", allSpeakers);
			setTrackSpeakers(tmpTrackSpeakers);

			getTrackSelector().addItem("All");
			getTrackSelector().setItemCaption("All", "All");
			getTrackSelector().setImmediate(true);
			getTrackSelector().addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					redrawSpeakers((String) event.getProperty().getValue());
				}
			});
			getTrackSelector().setValue("All");
			getTrackSelector().addStyleName("horizontal");
			getTrackSelector().setSizeUndefined();

			redrawSpeakers("All");
			addComponent(getContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void redrawSpeakers(String trackKey) {
		try {
			getContent().removeAllComponents();
			getContent().addComponent(getTrackSelector());
			TreeMap<String, Attendee> currTrackSpeakers = getTrackSpeakers().get(trackKey);
			HorizontalLayout attendeePanel = new HorizontalLayout();
			attendeePanel.setWidth(100, Unit.PERCENTAGE);
			int x = 1;
			for (Attendee att : currTrackSpeakers.values()) {
				AttendeeSummary panel = new AttendeeSummary();
				panel.loadAttendeeSummary(att, 2);
				attendeePanel.addComponent(panel);
				if (x == 3) {
					getContent().addComponent(attendeePanel);
					attendeePanel = new HorizontalLayout();
					attendeePanel.setWidth(100, Unit.PERCENTAGE);
					x = 1;
				} else {
					x++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<HorizontalLayout> getPanels() {
		return panels;
	}

	public void setPanels(List<HorizontalLayout> panels) {
		this.panels = panels;
	}

	public HashMap<String, TreeMap<String, Attendee>> getTrackSpeakers() {
		return trackSpeakers;
	}

	public void setTrackSpeakers(HashMap<String, TreeMap<String, Attendee>> trackSpeakers) {
		this.trackSpeakers = trackSpeakers;
	}

	public VerticalLayout getContent() {
		return content;
	}

	public void setContent(VerticalLayout content) {
		this.content = content;
	}

	public OptionGroup getTrackSelector() {
		return trackSelector;
	}

	public void setTrackSelector(OptionGroup trackSelector) {
		this.trackSelector = trackSelector;
	}

}
