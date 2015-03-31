package org.openntf.conferenceapp.ui.pages;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.conference.graph.Track;
import org.openntf.conferenceapp.service.EventFactory;
import org.openntf.conferenceapp.service.TimeSlotFactory;
import org.openntf.conferenceapp.ui.ConferenceUI;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NowAndNext extends CssLayout implements View {

	private static final long serialVersionUID = 1L;
	private boolean RUN_SIMULATED = false;
	public static final String VIEW_NAME = "NowAndNext";
	public static final String VIEW_DESC = "Now and Next";
	List<TimeSlot> times;

	public NowAndNext() {

	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (RUN_SIMULATED) {
			loadSimulatedNowAndNext();
		} else {
			loadNowAndNext();
		}
		loadContent();
	}

	public List<TimeSlot> getTimes() {
		return times;
	}

	public void setTimes(List<TimeSlot> times) {
		this.times = times;
	}

	public void loadNowAndNext() {
		setTimes(TimeSlotFactory.getNowAndNext());
	}

	public void loadSimulatedNowAndNext() {
		List<TimeSlot> times = TimeSlotFactory.getTimeSlotsSortedByProperty("Starttime");
		TimeSlot ts = times.get(0);
		Date dt = ts.getStartTime().getTime();
		setTimes(TimeSlotFactory.getSimulatedNowAndNext(dt));
	}

	public void loadContent() {
		try {

			setSizeFull();

			// We're always having a header, even if it's to say
			// "This isn't active yet"
			if (getTimes().isEmpty()) {
				// Not yet active, log and quit
				final HorizontalLayout top = new HorizontalLayout();
				top.setDefaultComponentAlignment(Alignment.TOP_LEFT);
				top.addStyleName(ValoTheme.MENU_TITLE);
				top.setSpacing(true);
				Label title = new Label("Now and next is only active during the conference");
				title.setSizeUndefined();
				top.addComponent(title);
				addComponent(top);
				return;
			}

			// Get time and number of sessions occurring then
			TimeSlot ts1 = getTimes().get(0);
			Date currDate = new Date();
			if (RUN_SIMULATED) {
				Calendar tmpCal = ts1.getStartTime();
				tmpCal.add(Calendar.MINUTE, 10);
				currDate = tmpCal.getTime();
			}
			loadSessionsForTime(ts1, "Now", currDate);
			if (getTimes().size() > 1) {
				TimeSlot ts2 = getTimes().get(1);
				loadSessionsForTime(ts2, "Next", currDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadSessionsForTime(TimeSlot ts, String label, Date passedDate) {
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		top.addStyleName(ValoTheme.MENU_TITLE);
		top.setSpacing(true);
		Iterable<Presentation> presentations = EventFactory.getPresentationsByTimeSlot(ts);
		String minutes;
		if ("Now".equals(label)) {
			minutes = Integer.toString(minutesDiff(passedDate, ts.getEndTime().getTime())) + " minutes remaining)";
		} else {
			minutes = Integer.toString(minutesDiff(passedDate, ts.getStartTime().getTime())) + " minutes to start)";
		}
		Label title = new Label(label + getCountMsg(presentations) + minutes);
		title.setSizeUndefined();
		top.addComponent(title);
		addComponent(top);

		for (final Presentation pres : presentations) {
			// Load speaker details
			Iterable<Attendee> speakers = pres.getPresentingAttendees();
			String speakerNames = "";
			for (Attendee att : speakers) {
				if ("".equals(speakerNames)) {
					speakerNames = att.getFirstName() + " " + att.getLastName();
				} else {
					speakerNames += ", " + att.getFirstName() + " " + att.getLastName();
				}
			}
			final String passedSpeakers = speakerNames;

			HorizontalLayout sessionRow = new HorizontalLayout();
			sessionRow.setWidth(100, Unit.PERCENTAGE);
			Track track = pres.getIncludedInTracks().iterator().next();
			Label trackLabel = new Label();
			trackLabel.setDescription(track.getDescription());
			trackLabel.setValue(getTrackHtml(track.getTitle()));
			trackLabel.setWidth(25, Unit.PIXELS);
			trackLabel.setContentMode(ContentMode.HTML);
			sessionRow.addComponent(trackLabel);
			sessionRow.setComponentAlignment(trackLabel, Alignment.MIDDLE_CENTER);
			VerticalLayout sessionDetails = new VerticalLayout();
			sessionDetails.setWidth(100, Unit.PERCENTAGE);
			Button presTitle = new Button(pres.getSessionId() + " - " + pres.getTitle());
			presTitle.setStyleName(ValoTheme.BUTTON_LINK);
			presTitle.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					SessionDetailsDialog sub = new SessionDetailsDialog();
					sub.setSessionTitle(pres.getTitle());
					sub.setSessionDesc(pres.getDescription());
					sub.setSpeakers("Speakers: " + passedSpeakers);
					sub.loadContent();

					// Add it to the root component
					UI.getCurrent().addWindow(sub);
				}
			});
			sessionDetails.addComponent(presTitle);
			DateFormatSymbols s = new DateFormatSymbols(UI.getCurrent().getLocale());
			String[] days = s.getShortWeekdays();
			String dayTime = days[ts.getStartTime().get(Calendar.DAY_OF_WEEK)] + ConferenceUI.TIME_FORMAT.format(ts.getStartTime().getTime()) + "-"
					+ ConferenceUI.TIME_FORMAT.format(ts.getEndTime().getTime());
			Location loc = pres.getLocations().iterator().next();
			sessionDetails.addComponent(new Label(dayTime + "|" + loc.getName()));
			sessionRow.addComponent(sessionDetails);
			sessionRow.setExpandRatio(trackLabel, 1);
			sessionRow.setExpandRatio(sessionDetails, 19);
			addComponent(sessionRow);
		}

		Label gap = new Label();
		gap.setHeight("1em");
		addComponent(gap);
	}

	public String getTrackHtml(String trackCode) {
		String iconCode;
		if ("Sp".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.STAR.getFontFamily() + ";font-size:20px;color:#FFE118\">&#x"
					+ Integer.toHexString(FontAwesome.STAR.getCodepoint()) + ";</span>";
		} else if ("Str".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.QUESTION_CIRCLE.getFontFamily()
					+ ";font-size:20px;color:#B8CCE4\">&#x" + Integer.toHexString(FontAwesome.QUESTION_CIRCLE.getCodepoint()) + ";</span>";
		} else if ("Bus".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.BRIEFCASE.getFontFamily() + ";font-size:20px;color:#FFC7CE\">&#x"
					+ Integer.toHexString(FontAwesome.BRIEFCASE.getCodepoint()) + ";</span>";
		} else if ("Adm".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.TERMINAL.getFontFamily() + ";font-size:20px;color:#C6EFCE\">&#x"
					+ Integer.toHexString(FontAwesome.TERMINAL.getCodepoint()) + ";</span>";
		} else if ("Dev".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.STACK_OVERFLOW.getFontFamily()
					+ ";font-size:20px;color:#FFEB9C\">&#x" + Integer.toHexString(FontAwesome.STACK_OVERFLOW.getCodepoint()) + ";</span>";
		} else if ("Comm".equals(trackCode)) {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.EURO.getFontFamily() + ";font-size:20px;color:#FFEB9C\">&#x"
					+ Integer.toHexString(FontAwesome.EURO.getCodepoint()) + ";</span>";
		} else {
			iconCode = "<span class=\"v-icon\" style=\"font-family: " + FontAwesome.CIRCLE.getFontFamily() + ";font-size:20px;color:#FFFFFF\">&#x"
					+ Integer.toHexString(FontAwesome.CIRCLE.getCodepoint()) + ";</span>";
		}
		return iconCode;
	}

	public int minutesDiff(Date earlierDate, Date laterDate) {
		if (earlierDate == null || laterDate == null)
			return 0;

		return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
	}

	public String getCountMsg(Iterable<Presentation> pres) {
		List presentations = Lists.newArrayList(pres);
		if (presentations.size() < 2) {
			return " (" + presentations.size() + " session ";
		} else {
			return " (" + presentations.size() + " sessions ";
		}
	}

}
