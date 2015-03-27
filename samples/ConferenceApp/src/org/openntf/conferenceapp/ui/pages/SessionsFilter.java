package org.openntf.conferenceapp.ui.pages;

import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Track;
import org.openntf.conferenceapp.service.LocationFactory;
import org.openntf.conferenceapp.service.TrackFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class SessionsFilter extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "SessionsFilter";
	public static final String VIEW_DESC = "Sessions By...";
	private TraditionalGrid grid;

	public SessionsFilter() {

	}

	public void loadContent() {
		try {

			setSizeFull();

			MenuBar menubar = new MenuBar();
			menubar.addStyleName(ValoTheme.MENU_SUBTITLE);
			menubar.setWidth(100, Unit.PERCENTAGE);
			menubar.addItem("Sessions By...", null);
			MenuItem tracks = menubar.addItem("Filter by Track", null, null);
			tracks.addItem("All", trackFilterCommand);
			for (Track track : TrackFactory.getTracksSortedByProperty("")) {
				MenuItem t = tracks.addItem(track.getDescription(), getIcon(track.getTitle()), trackFilterCommand);
			}
			MenuItem locations = menubar.addItem("Filter by Location", null, null);
			locations.addItem("All", locationFilterCommand);
			for (Location loc : LocationFactory.getLocationsSortedByProperty("")) {
				MenuItem l = locations.addItem(loc.getName(), locationFilterCommand);
			}
			MenuItem days = menubar.addItem("Filter by Date", null);
			days.addItem("All", dayFilterCommand);
			days.addItem("March 30, 2015", dayFilterCommand);
			days.addItem("March 31, 2015", dayFilterCommand);

			addComponent(menubar);

			HorizontalLayout sessionRow = new HorizontalLayout();
			sessionRow.setSizeFull();
			addComponent(sessionRow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Resource getIcon(String trackCode) {
		Resource iconCode;
		if ("Sp".equals(trackCode)) {
			iconCode = FontAwesome.STAR;
		} else if ("Str".equals(trackCode)) {
			iconCode = FontAwesome.QUESTION_CIRCLE;
		} else if ("Bus".equals(trackCode)) {
			iconCode = FontAwesome.BRIEFCASE;
		} else if ("Adm".equals(trackCode)) {
			iconCode = FontAwesome.TERMINAL;
		} else if ("Dev".equals(trackCode)) {
			iconCode = FontAwesome.STACK_OVERFLOW;
		} else {
			iconCode = FontAwesome.CIRCLE;
		}
		return iconCode;
	}

	MenuBar.Command trackFilterCommand = new MenuBar.Command() {
		public void menuSelected(MenuItem selectedItem) {
			System.out.println(selectedItem.getText());
		}
	};

	MenuBar.Command locationFilterCommand = new MenuBar.Command() {
		public void menuSelected(MenuItem selectedItem) {
			System.out.println(selectedItem.getText());
		}
	};

	MenuBar.Command dayFilterCommand = new MenuBar.Command() {
		public void menuSelected(MenuItem selectedItem) {
			System.out.println(selectedItem.getText());
		}
	};

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		loadContent();
	}

}
