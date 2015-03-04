package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.List;

import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.frames.FramedGraph;

public class TrackFactory {

	/**
	 * Gets all Tracks in the graph, sorted on the property passed, or "Title" if an empty String is passed
	 * 
	 * @param property String property to sort on. If empty String passed, default of "Title" is used. Other property for a Track is "Description"
	 * @return ArrayList<Track> sorted on property, or empty ArrayList if nothing found or error encountered
	 */
	public static List<Track> getTracksSortedByProperty(String property) {
		List<Track> retVal_ = new ArrayList<Track>();
		try {
			FramedGraph graph = ConferenceGraphFactory.getEngageGraph();
			Iterable<Track> tracks = graph.getVertices(null, null, Track.class);
			if (StringUtil.isEmpty(property)) {
				property = "Title";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(tracks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
	
	/**
	 * Gets a track for the relevant key
	 * 
	 * @param key String for Track, e.g. "Dev"
	 * @return Track or null if no track found or error encountered
	 */
	public static Track getTrack(String key) {
		Track retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getEngageGraph();
			retVal_ = (Track) graph.getVertex(key, Track.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
}
