package org.openntf.conferenceapp.ui.pages;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class TraditionalView extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "TraditionalView";
	public static final String VIEW_DESC = "Traditional View";
	private TraditionalGrid grid;

	public TraditionalView() {

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

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// viewLogic.enter(event.getParameters());
		if (null == grid) {
			grid = new TraditionalGrid();
			addComponent(grid);
		}
	}

}
