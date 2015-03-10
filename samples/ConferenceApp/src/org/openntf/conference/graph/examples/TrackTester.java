package org.openntf.conference.graph.examples;

import java.util.ArrayList;
import java.util.List;

import org.openntf.conference.graph.Track;
import org.openntf.conferenceapp.service.TrackFactory;
import org.openntf.domino.junit.TestRunnerUtil;

public class TrackTester implements Runnable {
	private long marktime;

	public TrackTester() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			List<Track> tracks = TrackFactory.getTracksSortedByProperty("Title");
			String trackTitle = "";
			System.out.println("Logging tracks.....");
			for (Track t : tracks) {
				if ("".equals(trackTitle)) {
					trackTitle = t.getTitle();
				}
				System.out.println("Track " + t.getTitle() + ": " + t.getDescription());
			}
			System.out.println("************************");
			System.out.println("Outputting track for " + trackTitle);
			Track track = TrackFactory.getTrack(trackTitle);
			System.out.println("Track " + track.getTitle() + ": " + track.getDescription());
			System.out.println("************************");
			System.out.println("Getting Tracks using Gremlin");
			tracks = TrackFactory.getTracksGremlinBeforeK();
			for (Track t : tracks) {
				if ("".equals(trackTitle)) {
					trackTitle = t.getTitle();
				}
				System.out.println("Track " + t.getTitle() + ": " + t.getDescription());
			}
			System.out.println("************************");
			System.out.println("Getting Three Letter Tracks using Gremlin");
			tracks = TrackFactory.getTracksGremlinForThreeLetterTracks();
			for (Track t : tracks) {
				if ("".equals(trackTitle)) {
					trackTitle = t.getTitle();
				}
				System.out.println("Track " + t.getTitle() + ": " + t.getDescription());
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
		TestRunnerUtil.runAsDominoThread(new TrackTester(), TestRunnerUtil.NATIVE_SESSION);
	}

}
