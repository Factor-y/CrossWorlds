package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.List;

import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.Tokens;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.transform.OrderPipe;
import com.tinkerpop.pipes.transform.TransformPipe;
import com.tinkerpop.pipes.transform.TransformPipe.Order;
import com.tinkerpop.pipes.util.structures.Pair;

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
	
	@SuppressWarnings("unchecked")
	public static List<Track> getTracksGremlinBeforeK() {
		List<Track> retVal_ = new ArrayList<Track>();
		try {
			FramedGraph graph = ConferenceGraphFactory.getEngageGraph();
			List<Track> tracks = Lists.newArrayList(graph.getVertices(null, null, Track.class));
			// Can't seem to get the compare to work, but here it is
			PipeFunction<Pair<Track,Track>, Integer> strCompare = new PipeFunction<Pair<Track,Track>, Integer>() {

				@Override
				public Integer compute(Pair<Track, Track> arg0) {
					System.out.println("Comparing...");
					System.out.println("**Comparing " + arg0.getA().getTitle() + " with " + arg0.getB().getTitle());
					String elem1 = arg0.getA().getTitle();
					String elem2 = arg0.getB().getTitle();
					Integer ord = elem1.compareToIgnoreCase(elem2);
					System.out.println(ord);
					return ord;
				}
				
			};
			PipeFunction<Track, Boolean> strFilter = new PipeFunction<Track, Boolean>() {

				@Override
				public Boolean compute(Track t) {
					Integer beforeK = t.getTitle().compareToIgnoreCase("K");
					if (beforeK < 0) {
						return true;
					} else {
						return false;
					}
				}
				
			};
			//GremlinPipeline pipe = new GremlinPipeline(tracks).add(new OrderPipe(strCompare)).back(1);
			GremlinPipeline pipe = new GremlinPipeline(tracks).filter(strFilter).back(1);
			retVal_ = pipe.toList();
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
