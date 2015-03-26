package org.openntf.conferenceapp.ui;

import java.util.HashMap;
import java.util.Map;

import org.openntf.conferenceapp.ui.pages.TraditionalView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class Menu extends CssLayout {
	private static final String VALO_MENUITEMS = "valo-menuitems";
	private static final String VALO_MENU_TOGGLE = "valo-menu-toggle";
	private static final String VALO_MENU_VISIBLE = "valo-menu-visible";
	private Navigator navigator;
	private Map<String, Button> viewButtons = new HashMap<String, Button>();

	private MenuBar menuItemsLayout;
	private HorizontalLayout menuPart;

	public Menu(Navigator navigator) {
		this.navigator = navigator;
		setPrimaryStyleName(ValoTheme.MENU_ROOT);
		menuPart = new HorizontalLayout();
		menuPart.addStyleName(ValoTheme.MENU_PART);

		// header of the menu
		// final HorizontalLayout top = new HorizontalLayout();
		// top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		// top.addStyleName(ValoTheme.MENU_TITLE);
		// top.setSpacing(true);
		Label title = new Label("OurConference");
		title.addStyleName(ValoTheme.MENU_TITLE);
		title.setSizeUndefined();
		// top.addComponent(title);
		menuPart.addComponent(title);

		// logout menu item
		MenuBar logoutMenu = new MenuBar();
		logoutMenu.addItem("Logout", FontAwesome.SIGN_OUT, new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				VaadinSession.getCurrent().getSession().invalidate();
				Page.getCurrent().reload();
			}
		});

		// // button for toggling the visibility of the menu when on a small
		// screen
		// final Button showMenu = new Button("Menu", new ClickListener() {
		// @Override
		// public void buttonClick(final ClickEvent event) {
		// if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
		// menuPart.removeStyleName(VALO_MENU_VISIBLE);
		// } else {
		// menuPart.addStyleName(VALO_MENU_VISIBLE);
		// }
		// }
		// });
		// showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		// showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		// showMenu.addStyleName(VALO_MENU_TOGGLE);
		// showMenu.setIcon(FontAwesome.NAVICON);
		// menuPart.addComponent(showMenu);

		// logoutMenu.addStyleName("user-menu");
		// menuPart.addComponent(logoutMenu);

		loadMenu();
		menuPart.addComponent(menuItemsLayout);

		addComponent(menuPart);
	}

	private void loadMenu() {
		// Define a common menu command for all the menu items.
		MenuBar.Command loadPage = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().getNavigator().navigateTo(selectedItem.getText());
				if (menuPart.getStyleName().contains(VALO_MENU_VISIBLE)) {
					menuPart.removeStyleName(VALO_MENU_VISIBLE);
				} else {
					menuPart.addStyleName(VALO_MENU_VISIBLE);
				}
			}
		};

		// container for the navigation buttons, which are added by addView()
		menuItemsLayout = new MenuBar();
		MenuItem item = menuItemsLayout.addItem(TraditionalView.VIEW_NAME, loadPage);
		item.setStyleName(ValoTheme.MENU_ITEM);
		addView(new TraditionalView(), TraditionalView.VIEW_NAME, TraditionalView.VIEW_NAME, null);
	}

	/**
	 * Register a pre-created view instance in the navigation menu and in the
	 * {@link Navigator}.
	 *
	 * @see Navigator#addView(String, View)
	 *
	 * @param view
	 *            view instance to register
	 * @param name
	 *            view name
	 * @param caption
	 *            view caption in the menu
	 * @param icon
	 *            view icon in the menu
	 */
	public void addView(View view, final String name, String caption, Resource icon) {
		navigator.addView(name, view);
	}

	/**
	 * Register a view in the navigation menu and in the {@link Navigator} based
	 * on a view class.
	 *
	 * @see Navigator#addView(String, Class)
	 *
	 * @param viewClass
	 *            class of the views to create
	 * @param name
	 *            view name
	 * @param caption
	 *            view caption in the menu
	 * @param icon
	 *            view icon in the menu
	 */
	public void addView(Class<? extends View> viewClass, final String name, String caption, Resource icon) {
		navigator.addView(name, viewClass);
	}

	/**
	 * Highlights a view navigation button as the currently active view in the
	 * menu. This method does not perform the actual navigation.
	 *
	 * @param viewName
	 *            the name of the view to show as active
	 */
	public void setActiveView(String viewName) {
		for (Button button : viewButtons.values()) {
			button.removeStyleName("selected");
		}
		Button selected = viewButtons.get(viewName);
		if (selected != null) {
			selected.addStyleName("selected");
		}
		menuPart.removeStyleName(VALO_MENU_VISIBLE);
	}
}
