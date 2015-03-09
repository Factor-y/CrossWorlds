package org.openntf.conference.graph.examples;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.DEdge;
import org.openntf.domino.graph2.DVertex;
import org.openntf.domino.junit.TestRunnerUtil;

import java.util.List;
import java.util.Map;

import javolution.util.FastSet;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Presentation;
import org.openntf.conferenceapp.service.ConferenceGraphFactory;
import org.openntf.conferenceapp.service.ConferenceManager;
import org.openntf.conferenceapp.service.EventFactory;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.FramedGraph;

public class AttendeeTester implements Runnable {
	private long marktime;
	
	public AttendeeTester() {
		
	}
	
	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Iterable<Attendee> atts = (Iterable<Attendee>) graph.getVertices("Twitter", "paulswithers", Attendee.class);
			Attendee paulwithers = atts.iterator().next();
			Map<CharSequence, Object> props = paulwithers.asMap();
			System.out.println("Outputting properties for Paul Withers");
			for (CharSequence key : props.keySet()) {
				System.out.println(key);
			}
			System.out.println("***************");
			DVertex vert = (DVertex) paulwithers.asVertex();
			NoteCoordinate note = (NoteCoordinate) vert.getId();
			System.out.println("Current Id = " + note.toString());
			
			ConferenceGraph baseGraph = ConferenceGraphFactory.getConference(ConferenceGraphFactory.ENGAGE_KEY);
			Attendee me = baseGraph.getAttendee("paulswithers",false);
			if (null != me) {
				System.out.println("Found " + me.getFirstName() + " " + me.getLastName());
			} else {
				System.out.println("Couldn't find me");
			}
			
//			System.out.println("Changing key to pwithers@intec.co.uk");
//			vert.setProperty("$$Key", "pwithers@intec.co.uk");
//			vert.commit();
//			System.out.println("Changed key to pwithers@intec.co.uk");
//			graph.getVertex("pwithers@intec.co.uk", Attendee.class);
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
		TestRunnerUtil.runAsDominoThread(new AttendeeTester(), TestRunnerUtil.NATIVE_SESSION);
	}
}
