package org.openntf.conferenceapp.ui.pages;

import java.util.HashMap;

import org.openntf.conferenceapp.components.IconHeaderComponent;
import org.openntf.conferenceapp.ui.ConferenceUI;
import org.openntf.conferenceapp.ui.pages.profile.ProfileView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends VerticalLayout {
	private NavigableMenuBar menu;

	public MainScreen(ConferenceUI ui) {

		setStyleName("main-screen");

		addComponent(new IconHeaderComponent());

		CssLayout viewContainer = new CssLayout();
		viewContainer.addStyleName("valo-content");
		viewContainer.setSizeFull();

		final Navigator navigator = new Navigator(ui, viewContainer);
		navigator.setErrorView(ErrorView.class);
		menu = new NavigableMenuBar(navigator);
		menu.setStyleName("menuArea");
		addComponent(menu);

		navigator.addViewChangeListener(menu);

		// Only used for demos, not a "modern" user experience!
		// navigator.addView(TraditionalView.VIEW_NAME, new TraditionalView());
		// menu.addView(TraditionalView.VIEW_NAME, TraditionalView.VIEW_DESC, null);

		navigator.addView(SessionsFilter.VIEW_NAME, new SessionsFilter());
		menu.addView(SessionsFilter.VIEW_NAME, SessionsFilter.VIEW_DESC, null);

		navigator.addView(NowAndNext.VIEW_NAME, new NowAndNext());
		menu.addView(NowAndNext.VIEW_NAME, NowAndNext.VIEW_DESC, null);

		navigator.addView(Sponsors.VIEW_NAME, new Sponsors());
		menu.addView(Sponsors.VIEW_NAME, Sponsors.VIEW_DESC, null);

		navigator.addView(CalendarView.VIEW_NAME, new CalendarView());
		menu.addView(CalendarView.VIEW_NAME, CalendarView.VIEW_DESC, null);

		navigator.addView(Speakers.VIEW_NAME, new Speakers());
		menu.addView(Speakers.VIEW_NAME, Speakers.VIEW_DESC, null);

		navigator.addView(ProfileView.VIEW_NAME, new ProfileView());
		menu.addView(ProfileView.VIEW_NAME, ProfileView.VIEW_DESC, null);

		addComponent(viewContainer);
		setExpandRatio(viewContainer, 1);
		setSizeFull();
	}

	/** A menu bar that both controls and observes navigation */
	protected class NavigableMenuBar extends MenuBar implements ViewChangeListener {
		private MenuItem previous = null; // Previously selected item
		private MenuItem current = null; // Currently selected item

		// Map view IDs to corresponding menu items
		HashMap<String, MenuItem> menuItems = new HashMap<String, MenuItem>();

		private Navigator navigator = null;

		public NavigableMenuBar(Navigator navigator) {
			this.navigator = navigator;
		}

		/** Navigate to a view by menu selection */
		MenuBar.Command mycommand = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				String viewName = selectItem(selectedItem);
				navigator.navigateTo(viewName);
			}
		};

		public void addView(String viewName, String caption, Resource icon) {
			menuItems.put(viewName, addItem(caption, icon, mycommand));
		}

		/** Select a menu item by its view ID **/
		protected boolean selectView(String viewName) {
			// Check that the menu item exists
			if (!menuItems.containsKey(viewName))
				return false;

			if (previous != null)
				previous.setStyleName(null);
			if (current == null)
				current = menuItems.get(viewName);
			current.setStyleName("highlight");
			previous = current;

			return true;
		}

		/** Selects a new menu item */
		public String selectItem(MenuItem selectedItem) {
			current = selectedItem;

			// Do reverse lookup for the view ID
			for (String key : menuItems.keySet())
				if (menuItems.get(key) == selectedItem)
					return key;

			return null;
		}

		@Override
		public boolean beforeViewChange(ViewChangeEvent event) {
			return selectView(event.getViewName());
		}

		@Override
		public void afterViewChange(ViewChangeEvent event) {
		}
	};
}
