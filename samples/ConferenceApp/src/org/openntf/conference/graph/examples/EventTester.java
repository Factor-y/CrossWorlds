package org.openntf.conference.graph.examples;

import org.openntf.domino.junit.TestRunnerUtil;

import java.util.List;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Presentation;
import org.openntf.conferenceapp.service.ConferenceGraphFactory;
import org.openntf.conferenceapp.service.ConferenceManager;
import org.openntf.conferenceapp.service.EventFactory;

import com.google.common.collect.Lists;

public class EventTester implements Runnable {
	private long marktime;
	
	public EventTester() {
		
	}
	
	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			List<Presentation> pres = Lists.newArrayList(EventFactory.getPresentationsSortedByProperty("Title"));
			System.out.println("Listing Presentations by Title...");
			for (Presentation p : pres) {
				System.out.println(p.getSessionId() + " - " + p.getTitle());
			}
			System.out.println("******************");
			
			pres = Lists.newArrayList(EventFactory.getPresentationsSortedByProperty("SessionID"));
			System.out.println("Listing Presentations by Session ID...");
			for (Presentation p : pres) {
				System.out.println(p.getSessionId() + " - " + p.getTitle());
			}
			System.out.println("******************");
			
			System.out.println("Outputting speakers for tracks...");
			List<Attendee> speakers = Lists.newArrayList(EventFactory.getSpeakersDomino(""));
			for (Attendee speaker : speakers) {
				System.out.println(speaker.getFirstName() + " " + speaker.getLastName() + " - " + speaker.getTwitterId());
			}
			System.out.println("******************");

			System.out.println("Outputting speakers for tracks from Gremlin...");
			List<Attendee> speakersGremlin = Lists.newArrayList(EventFactory.getSpeakers("Dev"));
			for (Attendee speaker : speakersGremlin) {
				System.out.println(speaker.getFirstName() + " " + speaker.getLastName() + " - " + speaker.getTwitterId());
			}
			System.out.println("******************");
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
		TestRunnerUtil.runAsDominoThread(new EventTester(), TestRunnerUtil.NATIVE_SESSION);
	}
}
