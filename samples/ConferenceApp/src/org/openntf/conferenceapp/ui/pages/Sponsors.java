package org.openntf.conferenceapp.ui.pages;

import java.util.List;

import org.openntf.conference.graph.Sponsor;
import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.conference.graph.TimeSlot;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class Sponsors extends CssLayout implements View {

	private static final long serialVersionUID = 1L;
	private boolean RUN_SIMULATED = true;
	public static final String VIEW_NAME = "Sponsors";
	public static final String VIEW_DESC = "Sponsors";
	List<TimeSlot> times;

	public Sponsors() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
		loadContent();
	}

	public void loadContent() {
		try {

			VerticalLayout main = new VerticalLayout();
			main.setSpacing(true);

			// Get time and number of sessions occurring then
			loadSponsorsForLevel(Level.STRATEGIC, main);
			loadSponsorsForLevel(Level.PLATINUM, main);
			loadSponsorsForLevel(Level.GOLD, main);
			loadSponsorsForLevel(Level.SILVER, main);
			loadSponsorsForLevel(Level.BRONZE, main);
			addComponent(main);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadSponsorsForLevel(Level level, VerticalLayout container) {
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		top.addStyleName(ValoTheme.MENU_TITLE);
		top.setSpacing(true);
		List<Sponsor> sponsors = SponsorFactory.getSponsorsForLevelSortedByProperty(level, null);
		Label title = new Label(level.name() + " " + getLevelHtml(level));
		title.setContentMode(ContentMode.HTML);
		title.setSizeUndefined();
		top.addComponent(title);
		container.addComponent(top);

		for (Sponsor s : sponsors) {
			HorizontalLayout sessionRow = new HorizontalLayout();
			sessionRow.setWidth(95, Unit.PERCENTAGE);
			Link url = new Link(s.getName(), new ExternalResource(s.getUrl()));
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
			container.addComponent(sessionRow);
			container.setComponentAlignment(sessionRow, Alignment.TOP_CENTER);
		}
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
