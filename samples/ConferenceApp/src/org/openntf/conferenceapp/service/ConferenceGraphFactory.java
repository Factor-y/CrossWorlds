package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.conference.graph.ConferenceGraph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tinkerpop.frames.FramedGraph;

public class ConferenceGraphFactory {

	private static Map<String, ConferenceGraph> registeredGraphs = new HashMap<String, ConferenceGraph>();
	
	
	static void registerConference(String conferenceKey, ConferenceGraph graph) {
		Preconditions.checkNotNull(conferenceKey,"The conference name/key cannot be null");
		Preconditions.checkNotNull(graph,"The conference graph cannot be null");
		
		registeredGraphs.put(conferenceKey, graph);
	}
	
	/**
	 * @return a list of available Conference graphs
	 */
	static List<String> getAvailableConferences() {
		return Lists.newArrayList(registeredGraphs.keySet());
	}
	
	static ConferenceManager getConferenceManager(String conferenceName) {
		return null;
	}
	
	static ConferenceGraph getConference(final String key) {
		ConferenceGraph retVal_ = null;
		try {
			if (!registeredGraphs.containsKey(key)) {
				retVal_ = new ConferenceGraph();
				registerConference(key, retVal_);
			} else {
				retVal_ = registeredGraphs.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}
	
	static FramedGraph getGraph(final String confName) {
		return getConference(confName).getFramedGraph();
	}
}
