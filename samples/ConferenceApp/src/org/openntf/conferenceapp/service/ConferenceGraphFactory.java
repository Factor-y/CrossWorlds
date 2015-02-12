package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.conference.graph.ConferenceGraph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
	
	
}
