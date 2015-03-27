package org.openntf.conferenceapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.ibm.icu.util.Calendar;

public class TimeSlotFactory {

	public static List<TimeSlot> getOfficialTimeSlotsForDate(Date dt) {
		List<TimeSlot> retVal_ = new ArrayList<TimeSlot>();
		// Get date as dd MMM
		if (null == dt) {
			dt = new Date();
		}
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		String day = DATE_FORMAT.format(cal.getTime());

		// Now get all TimeSlots
		ConferenceGraph graph = ConferenceGraphFactory.getConference(ConferenceGraphFactory.ENGAGE_KEY);
		Iterable<TimeSlot> times = graph.getTimeSlots();
		Iterator it = times.iterator();
		while (it.hasNext()) {
			TimeSlot ts = (TimeSlot) it.next();
			if (day.equals(ts.getDay()) && ts.getOfficial() == 1) {
				retVal_.add(ts);
			}
		}

		// Now sort on time
		Ordering ord = Ordering.from(new DVertexFrameComparator("Starttime"));
		retVal_ = ord.sortedCopy(retVal_);

		return retVal_;
	}

	public static List<TimeSlot> getTimeSlotsSortedByProperty(String property) {
		List<TimeSlot> retVal_ = new ArrayList<TimeSlot>();

		ConferenceGraph graph = ConferenceGraphFactory.getConference(ConferenceGraphFactory.ENGAGE_KEY);
		Iterable<TimeSlot> times = graph.getTimeSlots();

		// Now sort on time
		if (StringUtil.isEmpty(property)) {
			property = "Starttime";
		}
		Ordering ord = Ordering.from(new DVertexFrameComparator(property));
		retVal_ = ord.sortedCopy(times);

		return retVal_;
	}

	public static List<TimeSlot> getTimeSlotsSorted() {
		List<TimeSlot> retVal_ = new ArrayList<TimeSlot>();
		Ordering<TimeSlot> byStart = new Ordering<TimeSlot>() {

			@Override
			public int compare(final TimeSlot t1, final TimeSlot t2) {
				return t1.getStartTime().compareTo(t2.getStartTime());
			}
		};

		ConferenceGraph graph = ConferenceGraphFactory.getConference(ConferenceGraphFactory.ENGAGE_KEY);
		Iterable<TimeSlot> times = graph.getTimeSlots();
		retVal_ = byStart.sortedCopy(times);
		return retVal_;
	}

	public static List<TimeSlot> getNowAndNext() {
		List<TimeSlot> retVal_ = new ArrayList<TimeSlot>();

		retVal_ = getSimulatedNowAndNext(new Date());

		return retVal_;
	}

	public static List<TimeSlot> getSimulatedNowAndNext(Date dt) {
		List<TimeSlot> retVal_ = new ArrayList<TimeSlot>();

		ConferenceGraph graph = ConferenceGraphFactory.getConference(ConferenceGraphFactory.ENGAGE_KEY);
		Iterable<TimeSlot> times = graph.getTimeSlots();
		List<TimeSlot> nextTimes = new ArrayList<TimeSlot>();
		Iterator it = times.iterator();
		while (it.hasNext()) {
			TimeSlot ts = (TimeSlot) it.next();
			// Ignore, TimeSlot already ended
			// Ignore if StartTime more than one hour after now, remove
			if (ts.getOfficial() == 1) {
				if (dt.before(ts.getEndTime().getTime())) {
					Calendar check = Calendar.getInstance();
					check.setTime(ts.getStartTime().getTime());
					check.add(Calendar.MINUTE, -1);
					if (dt.after(check.getTime())) {
						// Currently running
						retVal_.add(ts);
					} else {
						nextTimes.add(ts);
					}
				}
			}
		}

		// Now sort on time
		if (!nextTimes.isEmpty()) {
			Ordering ord = Ordering.from(new DVertexFrameComparator("Starttime"));
			nextTimes = ord.sortedCopy(nextTimes);
			retVal_.add(nextTimes.get(0));
		}

		return retVal_;
	}

}
