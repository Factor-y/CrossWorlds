package org.openntf.conferenceapp.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.conferenceapp.service.ConferenceGraphFactory;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.frames.FramedGraph;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;

@SuppressWarnings("serial")
@Theme("valo")
public class ConferenceUI extends UI {

	@Override
	protected void init(VaadinRequest request) {

		getPage().setTitle("[OpenNTF] Conferences");

		VerticalLayout vert = new VerticalLayout();

		Session sess = Factory.getSession(SessionType.CURRENT);
		System.out.println(sess.getEffectiveUserName());

		// Table sessionTable = new Table();

		// Daniele's hard-coded version...
		// sessionTable.addContainerProperty("Date", Date.class, null);
		// sessionTable.addContainerProperty("Session", String.class, null);
		// sessionTable.addContainerProperty("Speaker", String.class, null);
		// sessionTable.addItem(new Object[] { new Date(),
		// "dev","Daniele Vistalli"}, "DV Session");
		// sessionTable.addItem(new Object[] { new Date(),
		// "dev","Paul Withers"}, "PW Session");

		// Version using Tracks
		// sessionTable.addContainerProperty("Title", String.class, null);
		// sessionTable.addContainerProperty("Description", String.class, null);
		// List<Track> tracks = TrackFactory.getTracksSortedByProperty("Title");
		// for (Track t : tracks) {
		// sessionTable.addItem(new Object[] { t.getTitle(), t.getDescription()
		// }, t.getTitle());
		// }
		//
		// sessionTable.setSizeFull();
		// sessionTable.addItemClickListener(new ItemClickListener() {
		//
		// public void itemClick(ItemClickEvent event) {
		//
		// System.out.println(event.getItemId() + " clicked");
		//
		// }
		// });

		// vert.addComponent(sessionTable);

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

		Grid grid = new Grid(container);
		Grid.Column col = grid.getColumn("SessionID");
		col.setHeaderCaption("Session ID");
		col.setSortable(true);
		col = grid.getColumn("Title");
		col.setSortable(true);
		col.setWidth(200);
		col = grid.getColumn("Description");
		col.setSortable(true);
		col.setWidth(300);
		col = grid.getColumn("Speakers");
		col = grid.getColumn("StartTime");
		col.setHeaderCaption("Start Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = grid.getColumn("EndTime");
		col.setHeaderCaption("End Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = grid.getColumn("Location");
		col.setSortable(true);
		grid.setFrozenColumnCount(2);
		grid.setColumnOrder("SessionID", "Title", "Description", "Speakers", "StartTime", "EndTime", "Location");
		grid.setWidth(100, Unit.PERCENTAGE);
		vert.addComponent(grid);

		vert.setSizeFull();

		setContent(vert);

	}

}
