package org.openntf.conferenceapp.ui.pages;

import java.util.ArrayList;
import java.util.List;

import org.openntf.conference.graph.Sponsor;
import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.conferenceapp.service.SponsorFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class Sponsors extends CssLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Sponsors";
	public static final String VIEW_DESC = "Sponsors";
	private List<VerticalLayout> panels = new ArrayList<VerticalLayout>();
	boolean contentLoaded = false;

	public Sponsors() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (!contentLoaded) {
			loadContent();
		}
		contentLoaded = true;
	}

	public void loadContent() {
		try {
			VerticalLayout main = new VerticalLayout();
			main.setSpacing(true);

			MenuBar menubar = new MenuBar();
			menubar.addStyleName(ValoTheme.MENU_SUBTITLE);
			menubar.setWidth(100, Unit.PERCENTAGE);
			menubar.addItem(Level.STRATEGIC.name(), filterSponsorsCommand);
			menubar.addItem(Level.PLATINUM.name(), filterSponsorsCommand);
			menubar.addItem(Level.GOLD.name(), filterSponsorsCommand);
			menubar.addItem(Level.SILVER.name(), filterSponsorsCommand);
			menubar.addItem(Level.BRONZE.name(), filterSponsorsCommand);
			addComponent(menubar);

			// Get time and number of sessions occurring then
			VerticalLayout stratDetails = loadSponsorsForLevel(Level.STRATEGIC, main);
			stratDetails.setDescription(Level.STRATEGIC.name());
			main.addComponent(stratDetails);
			panels.add(stratDetails);
			VerticalLayout platDetails = loadSponsorsForLevel(Level.PLATINUM, main);
			platDetails.setVisible(false);
			platDetails.setDescription(Level.PLATINUM.name());
			main.addComponent(platDetails);
			panels.add(platDetails);
			VerticalLayout goldDetails = loadSponsorsForLevel(Level.GOLD, main);
			goldDetails.setVisible(false);
			goldDetails.setDescription(Level.GOLD.name());
			main.addComponent(goldDetails);
			panels.add(goldDetails);
			VerticalLayout silverDetails = loadSponsorsForLevel(Level.SILVER, main);
			silverDetails.setVisible(false);
			silverDetails.setDescription(Level.SILVER.name());
			main.addComponent(silverDetails);
			panels.add(silverDetails);
			VerticalLayout bronzeDetails = loadSponsorsForLevel(Level.BRONZE, main);
			bronzeDetails.setVisible(false);
			bronzeDetails.setDescription(Level.BRONZE.name());
			main.addComponent(bronzeDetails);
			panels.add(bronzeDetails);
			addComponent(main);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	MenuBar.Command filterSponsorsCommand = new MenuBar.Command() {
		public void menuSelected(MenuItem selectedItem) {
			for (VerticalLayout l : panels) {
				if (l.getDescription().equals(selectedItem.getText())) {
					l.setVisible(true);
				} else {
					l.setVisible(false);
				}
			}
		}
	};

	private VerticalLayout loadSponsorsForLevel(Level level, VerticalLayout container) {
		VerticalLayout retVal_ = new VerticalLayout();
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		top.addStyleName(ValoTheme.MENU_TITLE);
		top.setSpacing(true);
		List<Sponsor> sponsors = SponsorFactory.getSponsorsForLevelSortedByProperty(level, null);
		Label title = new Label(level.name() + " " + getLevelHtml(level));
		title.setContentMode(ContentMode.HTML);
		title.setSizeUndefined();
		top.addComponent(title);
		retVal_.addComponent(top);

		for (Sponsor s : sponsors) {
			HorizontalLayout sessionRow = new HorizontalLayout();
			sessionRow.setWidth(95, Unit.PERCENTAGE);
			Link url = new Link("", new ExternalResource(s.getUrl()));
			url.setIcon(new ExternalResource(s.getPhotoUrl()));
			url.setTargetName("_blank");
			sessionRow.addComponent(url);
			VerticalLayout sponsorDetails = new VerticalLayout();
			sponsorDetails.setWidth(100, Unit.PERCENTAGE);
			Label sponsorName = new Label(s.getName());
			sponsorName.setStyleName(ValoTheme.LABEL_H2);
			sponsorDetails.addComponent(sponsorName);
			sponsorDetails.addComponent(new Label(s.getProfile()));
			sessionRow.addComponent(sponsorDetails);
			sessionRow.setExpandRatio(url, 1);
			sessionRow.setExpandRatio(sponsorDetails, 4);
			retVal_.addComponent(sessionRow);
			retVal_.setComponentAlignment(sessionRow, Alignment.TOP_CENTER);
		}
		return retVal_;
	}

	public String getLevelHtml(Level level) {
		String iconCode;
		String starCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.STAR.getFontFamily() + ";font-size:20px;color:#FFE118\">&#x"
				+ Integer.toHexString(FontAwesome.STAR.getCodepoint()) + ";</span>";
		if (level.equals(Level.STRATEGIC)) {
			iconCode = starCode + starCode + starCode + starCode + starCode;
		} else if (level.equals(Level.PLATINUM)) {
			iconCode = starCode + starCode + starCode + starCode;
		} else if (level.equals(Level.GOLD)) {
			iconCode = starCode + starCode + starCode;
		} else if (level.equals(Level.SILVER)) {
			iconCode = starCode + starCode;
		} else if (level.equals(Level.BRONZE)) {
			iconCode = starCode;
		} else {
			iconCode = "";
		}
		return iconCode;
	}

}
