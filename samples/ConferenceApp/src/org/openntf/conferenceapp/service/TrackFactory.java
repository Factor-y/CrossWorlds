package org.openntf.conferenceapp.service;

import java.util.List;

import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Ordering;
import com.tinkerpop.frames.FramedGraph;

public class TrackFactory {

	public static List<Track> getTracksSortedByProperty(String property) {
		List<Track> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Iterable<Track> tracks = graph.getVertices(null, null, Track.class);
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(tracks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
	
	public static Track getTrack(String key) {
		Track retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			retVal_ = (Track) graph.getVertex(key, Track.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
}
