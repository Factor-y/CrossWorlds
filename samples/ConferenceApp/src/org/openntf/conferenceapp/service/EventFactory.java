package org.openntf.conferenceapp.service;

import java.util.List;

import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Social;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.frames.FramedGraph;

public class EventFactory {
	
	public static List<Presentation> getPresentationsSortedByProperty(String property) {
		List<Presentation> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Iterable<Presentation> tracks = graph.getVertices(null, null, Presentation.class);
			if (StringUtil.isEmpty(property)) {
				property = "SessionID";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(tracks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
	
	

}
