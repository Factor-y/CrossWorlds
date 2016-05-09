package org.openntf.conference.graph.examples;

import java.util.List;

import org.openntf.conference.graph.Sponsor;
import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.conferenceapp.service.SponsorFactory;
import org.openntf.domino.junit.TestRunnerUtil;

public class SponsorTester implements Runnable {
	private long marktime;

	public SponsorTester() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			List<Sponsor> sponsors = SponsorFactory.getSponsorsSortedByProperty("Level");
			System.out.println("Logging sponsors.....");
			for (Sponsor s : sponsors) {
				System.out.println("Sponsor " + s.getLevel() + ": " + s.getName());
				System.out.println("Contact is " + SponsorFactory.getContactForSponsor(s.getName()).getEmail());
			}
			System.out.println("************************");

			System.out.println("************************");
			System.out.println("Getting Sponsors for Bronze");
			List<Sponsor> sponsors2 = SponsorFactory.getSponsorsForLevelSortedByProperty(Level.BRONZE, null);
			for (Sponsor s : sponsors2) {
				System.out.println("Sponsor " + s.getName() + " - " + s.getUrl());
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
		TestRunnerUtil.runAsDominoThread(new SponsorTester(), TestRunnerUtil.NATIVE_SESSION);
	}

}
