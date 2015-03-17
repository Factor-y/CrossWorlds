package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.List;

import org.openntf.conference.graph.Location;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.frames.FramedGraph;

public class LocationFactory {

	/**
	 * Gets all Location in the graph, sorted on the property passed, or "Name"
	 * if an empty String is passed
	 * 
	 * @param property
	 *            String property to sort on. If empty String passed, default of
	 *            "Name" is used.
	 * @return ArrayList<Location> sorted on property, or empty ArrayList if
	 *         nothing found or error encountered
	 */
	public static List<Location> getLocationsSortedByProperty(String property) {
		List<Location> retVal_ = new ArrayList<Location>();
		try {
			FramedGraph graph = ConferenceGraphFactory.getEngageGraph();
			Iterable<Location> locations = graph.getVertices(null, null, Location.class);
			if (StringUtil.isEmpty(property)) {
				property = "Name";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(locations);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	/**
	 * Gets a Location for the relevant key
	 * 
	 * @param key
	 *            String for Location name
	 * @return Location or null if no location found or error encountered
	 */
	public static Location getLocation(String key) {
		Location retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getEngageGraph();
			retVal_ = (Location) graph.getVertex(key, Location.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
}
