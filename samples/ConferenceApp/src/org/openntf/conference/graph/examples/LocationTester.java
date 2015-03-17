package org.openntf.conference.graph.examples;

import java.util.List;

import javolution.util.FastSet;

import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.conferenceapp.service.EventFactory;
import org.openntf.conferenceapp.service.LocationFactory;
import org.openntf.domino.junit.TestRunnerUtil;

public class LocationTester implements Runnable {
	private long marktime;

	public LocationTester() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			System.out.println("Getting Locations...");
			List<Location> locs = LocationFactory.getLocationsSortedByProperty("");
			Location location = null;
			for (Location loc : locs) {
				if (null == location) {
					location = loc;
				}
				System.out.println(loc.getName());
			}
			System.out.println("************************");

			System.out.println("Getting presentations for " + location.getName());
			FastSet<Presentation> presentations = EventFactory.getPresentationsByLocation(location);
			for (Presentation pres : presentations) {
				TimeSlot ts = pres.getTimes().iterator().next();
				System.out.println(pres.getTitle() + ": " + ts.getStartTime().getTime() + "-" + ts.getEndTime().getTime());
			}
			System.out.println("************************");
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
		TestRunnerUtil.runAsDominoThread(new LocationTester(), TestRunnerUtil.NATIVE_SESSION);
	}

}
