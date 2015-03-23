package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javolution.util.FastSet;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Event;
import org.openntf.conference.graph.Location;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
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

	public static FastSet<Presentation> getPresentationsByTimeSlot(Object ts) {
		FastSet<Presentation> retVal_ = new FastSet();
		List<TimeSlot> times = new ArrayList<TimeSlot>();
		if (null == ts) {
			times = TimeSlotFactory.getTimeSlotsSorted();
		} else if (ts instanceof TimeSlot) {
			times.add((TimeSlot) ts);
		} else if (ts instanceof List) {
			times = (List<TimeSlot>) ts;
		} else if (ts instanceof Date) {
			times = TimeSlotFactory.getOfficialTimeSlotsForDate((Date) ts);
		}
		for (TimeSlot time : times) {
			Iterable<Event> presentations = time.getEvents();
			for (Event evt : presentations) {
				if (evt instanceof Presentation) {
					retVal_.add((Presentation) evt);
				}

			}

		}
		return retVal_;
	}

	public static FastSet<Presentation> getPresentationsByLocation(Object loc) {
		FastSet<Presentation> retVal_ = new FastSet();
		List<Location> locations = new ArrayList<Location>();
		if (null == loc) {
			locations = LocationFactory.getLocationsSortedByProperty("");
		} else if (loc instanceof Location) {
			locations.add((Location) loc);
		} else if (loc instanceof List) {
			locations = (List<Location>) loc;
		}
		for (Location location : locations) {
			Iterable<Event> presentations = location.getEvents();
			for (Event evt : presentations) {
				if (evt instanceof Presentation) {
					retVal_.add((Presentation) evt);
				}

			}

		}
		return retVal_;
	}

	// This throws a ClassCastException currently
	// java.lang.ClassCastException: com.sun.proxy.$Proxy11 incompatible with
	// com.tinkerpop.blueprints.Vertex
	// Because outV returns a FramedVertex and needs to return .asVertex()
	public static List<Attendee> getSpeakers(String trackKey) {
		List<Attendee> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			List presentations = Lists.newArrayList(graph.getVertices(null, null, Presentation.class));

			// GremlinPipeline pipe = new
			// GremlinPipeline(presentations).outE("PresentedBy").outV().dedup();
			GremlinPipeline pipe = new GremlinPipeline(presentations).outE("PresentedBy").outV();
			// List<DEdge> edges = pipe.toList();
			// for (DEdge edge : edges) {
			// System.out.println(edge.getVertex(Direction.OUT).getId());
			// }
			retVal_ = pipe.toList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

}
