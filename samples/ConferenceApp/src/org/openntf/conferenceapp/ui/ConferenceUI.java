package org.openntf.conferenceapp.ui;

import org.openntf.conferenceapp.authentication.AccessControl;
import org.openntf.conferenceapp.authentication.BasicAccessControl;
import org.openntf.conferenceapp.authentication.LoginScreen;
import org.openntf.conferenceapp.authentication.LoginScreen.LoginListener;
import org.openntf.conferenceapp.ui.pages.MainScreen;
import org.openntf.conferenceapp.ui.pages.TraditionalView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;


@Viewport("user-scalable=no,initial-scale=1.0")
@SuppressWarnings("serial")
@Theme("conferenceApp")
public class ConferenceUI extends UI {

	private AccessControl accessControl = new BasicAccessControl();

	@Override
	protected void init(VaadinRequest request) {
		Responsive.makeResponsive(this);

		getPage().setTitle("[OpenNTF] Engage.ug 2015");

		if (!accessControl.isUserSignedIn()) {
			setContent(new LoginScreen(accessControl, new LoginListener() {
				@Override
				public void loginSuccessful() {
					showMainView();
				}
			}));
		} else {
			showMainView();
		}

		// VerticalLayout vert = new VerticalLayout();

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

		// // Create the container
		// IndexedContainer container = new IndexedContainer();
		//
		// // Define the properties (columns)
		// container.addContainerProperty("SessionID", String.class, "");
		// container.addContainerProperty("Title", String.class, "");
		// container.addContainerProperty("Description", String.class, "");
		// container.addContainerProperty("Speakers", String.class, "");
		// container.addContainerProperty("StartTime", Date.class, "");
		// container.addContainerProperty("EndTime", Date.class, "");
		// container.addContainerProperty("Location", String.class, "");
		//
		// FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
		// Iterable<Presentation> presentations = graph.getVertices(null, null,
		// Presentation.class);
		// for (Presentation pres : presentations) {
		// Item newItem = container.getItem(container.addItem());
		// newItem.getItemProperty("SessionID").setValue(pres.getSessionId());
		// newItem.getItemProperty("Title").setValue(pres.getTitle());
		// newItem.getItemProperty("Description").setValue(pres.getDescription());
		// Iterable<Attendee> speakers = pres.getPresentingAttendees();
		// String speakerNames = "";
		// for (Attendee att : speakers) {
		// if ("".equals(speakerNames)) {
		// speakerNames = att.getFirstName() + " " + att.getLastName();
		// } else {
		// speakerNames += ", " + att.getFirstName() + " " + att.getLastName();
		// }
		// }
		// newItem.getItemProperty("Speakers").setValue(speakerNames);
		// TimeSlot ts = pres.getTimes().iterator().next();
		// newItem.getItemProperty("StartTime").setValue(ts.getStartTime().getTime());
		// newItem.getItemProperty("EndTime").setValue(ts.getEndTime().getTime());
		// Location loc = pres.getLocations().iterator().next();
		// newItem.getItemProperty("Location").setValue(loc.getName());
		// }
		//
		// Grid grid = new Grid(container);
		// Grid.Column col = grid.getColumn("SessionID");
		// col.setHeaderCaption("Session ID");
		// col.setSortable(true);
		// col = grid.getColumn("Title");
		// col.setSortable(true);
		// col.setWidth(300);
		// // col = grid.getColumn("Description");
		// // col.setSortable(true);
		// // col.setWidth(300);
		// col = grid.getColumn("Speakers");
		// col.setWidth(200);
		// col = grid.getColumn("StartTime");
		// col.setHeaderCaption("Start Time");
		// col.setSortable(true);
		// col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		// col = grid.getColumn("EndTime");
		// col.setHeaderCaption("End Time");
		// col.setSortable(true);
		// col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		// col = grid.getColumn("Location");
		// col.setSortable(true);
		// grid.setFrozenColumnCount(2);
		// grid.setColumnOrder("SessionID", "Title", "Speakers", "StartTime",
		// "EndTime", "Location");
		// grid.setWidth(100, Unit.PERCENTAGE);
		// grid.setSizeFull();

		// vert.addComponent(grid);

		// vert.setSizeFull();

		// setContent(vert);

	}

	protected void showMainView() {
		addStyleName(ValoTheme.UI_WITH_MENU);
		setContent(new MainScreen(ConferenceUI.this));
		if ("".equals(getNavigator().getState())) {
			getNavigator().navigateTo(TraditionalView.VIEW_NAME);
		} else {
			getNavigator().navigateTo(getNavigator().getState());
		}
	}

	public static ConferenceUI get() {
		return (ConferenceUI) UI.getCurrent();
	}

	public AccessControl getAccessControl() {
		return accessControl;
	}

}
