package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javolution.util.FastCollection;
import javolution.util.FastMap;
import javolution.util.FastSet;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Social;
import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.DEdge;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;
import org.openntf.domino.utils.Strings;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class EventFactory {

	static boolean debug = false;

	public static List<Presentation> getPresentationsSortedByProperty(String property) {
		List<Presentation> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Iterable<Presentation> presentations = graph.getVertices(null, null, Presentation.class);
			if (StringUtil.isEmpty(property)) {
				property = "SessionID";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(presentations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	public static FastSet<Attendee> getSpeakersWithDuplicates(String trackKey) {
		FastSet<Attendee> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			List<Presentation> presentations = new ArrayList<Presentation>();
			FastSet<Attendee> speakers = new FastSet<Attendee>();
			int count = 0;
			if (Strings.isBlankString(trackKey)) {
				presentations = Lists.newArrayList(graph.getVertices(null, null, Presentation.class));
			} else {
				Track track = TrackFactory.getTrack(trackKey);
				presentations = Lists.newArrayList(track.getIncludesSessions());
			}
			for (Presentation pres : presentations) {
				List<Attendee> presSpeakers = Lists.newArrayList(pres.getPresentingAttendees());
				if (debug) {
					count = count + presSpeakers.size();
					System.out.println("Speakers for session " + pres.getTitle() + " - " + presSpeakers.size());
					System.out.println("Running total - " + count);
				}
				speakers.addAll(presSpeakers);
			}
			if (debug) {
				System.out.println("Total speakers - " + speakers.size());
			}
			return speakers;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	// This throws a ClassCastException currently
	public static List<Attendee> getSpeakers(String trackKey) {
		List<Attendee> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			List presentations = Lists.newArrayList(graph.getVertices(null, null, Presentation.class));

			//GremlinPipeline pipe = new GremlinPipeline(presentations).outE("PresentedBy").outV().dedup();
			GremlinPipeline pipe = new GremlinPipeline(presentations).outE("PresentedBy");
			List<DEdge> edges = pipe.toList();
			for (DEdge edge : edges) {
				System.out.println(edge.getVertex(Direction.OUT).getId());
			}
			retVal_ = pipe.toList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

}
