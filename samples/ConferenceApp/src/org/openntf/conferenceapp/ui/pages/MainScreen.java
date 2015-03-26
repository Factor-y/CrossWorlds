package org.openntf.conferenceapp.ui.pages;

import org.openntf.conferenceapp.ui.ConferenceUI;
import org.openntf.conferenceapp.ui.Menu;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends VerticalLayout {
	private Menu menu;

	public MainScreen(ConferenceUI ui) {

		setStyleName("main-screen");

		CssLayout viewContainer = new CssLayout();
		viewContainer.addStyleName("valo-content");
		viewContainer.setSizeFull();

		final Navigator navigator = new Navigator(ui, viewContainer);
		navigator.setErrorView(ErrorView.class);
		menu = new Menu(navigator);
		menu.addView(new TraditionalView(), TraditionalView.VIEW_NAME, TraditionalView.VIEW_NAME, null);

		navigator.addViewChangeListener(viewChangeListener);

		addComponent(menu);
		addComponent(viewContainer);
		setExpandRatio(viewContainer, 1);
		setSizeFull();
	}

	// notify the view menu about view changes so that it can display which view
	// is currently active
	ViewChangeListener viewChangeListener = new ViewChangeListener() {

		@Override
		public boolean beforeViewChange(ViewChangeEvent event) {
			return true;
		}

		@Override
		public void afterViewChange(ViewChangeEvent event) {
			menu.setActiveView(event.getViewName());
		}

	};
}
