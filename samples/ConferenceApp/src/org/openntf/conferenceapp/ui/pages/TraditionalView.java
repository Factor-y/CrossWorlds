package org.openntf.conferenceapp.ui.pages;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class TraditionalView extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "TraditionalView";
	private TraditionalGrid grid;

	public TraditionalView() {

		setSizeFull();
		grid = new TraditionalGrid();
		addComponent(grid);

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	public void showSaveNotification(String msg) {
		Notification.show(msg, Type.TRAY_NOTIFICATION);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// viewLogic.enter(event.getParameters());
	}

}
