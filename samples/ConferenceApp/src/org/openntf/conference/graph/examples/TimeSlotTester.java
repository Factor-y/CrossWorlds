package org.openntf.conference.graph.examples;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openntf.conference.graph.TimeSlot;
import org.openntf.conference.graph.Track;
import org.openntf.conferenceapp.service.TimeSlotFactory;
import org.openntf.conferenceapp.service.TrackFactory;
import org.openntf.domino.junit.TestRunnerUtil;

public class TimeSlotTester implements Runnable {
	private long marktime;

	public TimeSlotTester() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			List<TimeSlot> times = TimeSlotFactory.getTimeSlotsSortedByProperty("Starttime");
			Date dt = null;
			System.out.println("Outputting all timeslots sorted on Starttime...");
			for (TimeSlot ts : times) {
				if (null == dt) {
					dt = ts.getStartTime().getTime();
				}
				System.out.println(ts.getStartTime().getTime() + " - " + ts.getEndTime().getTime());
			}
			System.out.println("***************");

			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM");
			times = TimeSlotFactory.getTimeSlotsForDate(dt);
			System.out.println("Outputting all timeslots sorted on same day as " + DATE_FORMAT.format(dt));
			for (TimeSlot ts : times) {
				System.out.println(ts.getStartTime().getTime() + " - " + ts.getEndTime().getTime());
			}
			System.out.println("***************");
			
			System.out.println("Getting now and next for " + dt);
			times = TimeSlotFactory.getSimulatedNowAndNext(dt);
			for (TimeSlot ts : times) {
				System.out.println(ts.getStartTime().getTime() + " - " + ts.getEndTime().getTime());
			}
			System.out.println("***************");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new TimeSlotTester(), TestRunnerUtil.NATIVE_SESSION);
	}

}
