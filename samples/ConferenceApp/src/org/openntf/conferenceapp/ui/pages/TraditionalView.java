package org.openntf.conferenceapp.ui.pages;

import java.text.SimpleDateFormat;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class TraditionalView extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "TraditionalView";
	public static final String VIEW_DESC = "Traditional View";

	public TraditionalView() {

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		top.addStyleName(ValoTheme.MENU_TITLE);
		top.setSpacing(true);
		Label title = new Label("Traditonal 'View-style' Navigation");
		title.setSizeUndefined();
		top.addComponent(title);
		addComponent(top);
		Label info = new Label("Click on Session ID, Title, Start Time or Location columns to re-sort on those columns");
		addComponent(info);
		Label gap = new Label();
		gap.setHeight("1em");
		addComponent(gap);

		Grid tradGrid = new Grid();
		tradGrid.setSizeFull();

		PresentationsContainer presentations = new PresentationsContainer();
		tradGrid.setContainerDataSource(presentations.getContainer());
		Grid.Column col = tradGrid.getColumn("SessionID");
		col.setHeaderCaption("Session ID");
		col.setSortable(true);
		col = tradGrid.getColumn("Title");
		col.setSortable(true);
		col.setWidth(400);
		col = tradGrid.getColumn("Track");
		col.setSortable(true);
		// col = grid.getColumn("Description");
		// col.setSortable(true);
		// col.setWidth(300);
		col = tradGrid.getColumn("Speakers");
		col.setWidth(200);
		col = tradGrid.getColumn("Day");
		col.setSortable(true);
		col.setWidth(80);
		col = tradGrid.getColumn("StartTime");
		col.setHeaderCaption("Start Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = tradGrid.getColumn("EndTime");
		col.setHeaderCaption("End Time");
		col.setSortable(true);
		col.setRenderer(new DateRenderer(new SimpleDateFormat("hh:mm")));
		col = tradGrid.getColumn("Location");
		col.setWidth(80);
		col.setSortable(true);
		tradGrid.setFrozenColumnCount(2);
		tradGrid.setColumnOrder("SessionID", "Title", "Track", "Speakers", "Day", "StartTime", "EndTime", "Location");
		setWidth(100, Unit.PERCENTAGE);
		setSizeFull();

		addComponent(tradGrid);
	}

}
