package org.openntf.conferenceapp.ui.pages;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.conferenceapp.service.ConferenceGraphFactory;

import com.tinkerpop.frames.FramedGraph;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;

public class TraditionalGrid extends Grid {

	public TraditionalGrid() {
		setSizeFull();

		// Create the container
		IndexedContainer container = new IndexedContainer();

		// Define the properties (columns)
		container.addContainerProperty("SessionID", String.class, "");
		container.addContainerProperty("Title", String.class, "");
		container.addContainerProperty("Description", String.class, "");
		container.addContainerProperty("Speakers", String.class, "");
		container.addContainerProperty("StartTime", Date.class, "");
		container.addContainerProperty("EndTime", Date.class, "");
		container.addContainerProperty("Location", String.class, "");

		FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
		Iterable<Presentation> presentations = graph.getVertices(null, null, Presentation.class);
		for (Presentation pres : presentations) {
			Item newItem = container.getItem(container.addItem());
			newItem.getItemProperty("SessionID").setValue(pres.getSessionId());
			newItem.getItemProperty("Title").setValue(pres.getTitle());
			newItem.getItemProperty("Description").setValue(pres.getDescription());
			Iterable<Attendee> speakers = pres.getPresentingAttendees();
			String speakerNames = "";
			for (Attendee att : speakers) {
				if ("".equals(speakerNames)) {
					speakerNames = att.getFirstName() + " " + att.getLastName();
				} else {
					speakerNames += ", " + att.getFirstName() + " " + att.getLastName();
				}
			}
			newItem.getItemProperty("Speakers").setValue(speakerNames);
			TimeSlot ts = pres.getTimes().iterator().next();
			newItem.getItemProperty("StartTime").setValue(ts.getStartTime().getTime());
			newItem.getItemProperty("EndTime").setValue(ts.getEndTime().getTime());
			Location loc = pres.getLocations().iterator().next();
			newItem.getItemProperty("Location").setValue(loc.getName());
		}

		setContainerDataSource(container);
		Grid.Column col = getColumn("SessionID");
		col.setHeaderCaption("Session ID");
		col.setSortable(true);
		col = getColumn("Title");
		col.setSortable(true);
		col.setWidth(300);
		// col = grid.getColumn("Description");
		// col.setSortable(true);
		// col.setWidth(300);
		col = getColumn("Speakers");
		col.setWidth(200);
		col = getColumn("StartTime");
		col.setHeaderCaption("Start Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = getColumn("EndTime");
		col.setHeaderCaption("End Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = getColumn("Location");
		col.setSortable(true);
		setFrozenColumnCount(2);
		setColumnOrder("SessionID", "Title", "Speakers", "StartTime", "EndTime", "Location");
		setWidth(100, Unit.PERCENTAGE);
		setSizeFull();
	}
}
