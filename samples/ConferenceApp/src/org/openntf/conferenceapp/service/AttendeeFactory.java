package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javolution.util.FastSet;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Track;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.DGraph;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;
import org.openntf.xworlds.appservers.webapp.security.SecurityManager;

import com.google.common.collect.Lists;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedTransactionalGraph;

public class AttendeeFactory {

	public static Attendee getAttendeeByEmail(String email) {
		
		FramedTransactionalGraph<DGraph> framedGraph = (FramedTransactionalGraph<DGraph>) ConferenceGraphFactory.getGraph("engage");
		
		Iterable<Attendee> vs = framedGraph.getVertices("Email", email, Attendee.class);
		if (vs.iterator().hasNext()) {
			return vs.iterator().next();
		} else {
			return null;
		}
	}
	
	public static void commitGraph() {
		FramedTransactionalGraph<DGraph> framedGraph = (FramedTransactionalGraph<DGraph>) ConferenceGraphFactory.getGraph("engage");
		framedGraph.commit();
	}
	
	public static Attendee addAttendee(Map<String, Object> props) {
		Attendee retVal_ = null;
		try {
			ArrayList<String> missingProps = new ArrayList<String>();
			validateProperties(props, missingProps, "Firstname");
			validateProperties(props, missingProps, "Lastname");
			if (!props.containsKey("Email") && !props.containsKey("Twitter")) {
				missingProps.add("Email or Twitter Username");
			}
			if (!missingProps.isEmpty()) {
				String missing = Strings.join(missingProps, ",");
				throw new IllegalStateException(missing);
			}

			FramedTransactionalGraph<DGraph> framedGraph = (FramedTransactionalGraph<DGraph>) ConferenceGraphFactory.getGraph("engage");
			String key = "";
			if (props.containsKey("Email")) {
				key = (String) props.get("Email");
			} else {
				key = (String) props.get("Twitter");
			}
			Attendee att = framedGraph.addVertex(key, Attendee.class);
			att.setFirstName((String) props.get("Firstname"));
			att.setLastName((String) props.get("Lastname"));
			updateAttendeeProps(props, att);
			
			framedGraph.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	private static void updateAttendeeProps(Map<String, Object> props, Attendee att) {
		if (props.containsKey("Email")) {
			att.setEmail((String) props.get("Email"));
		}
		if (props.containsKey("Email")) {
			att.setTwitterId((String) props.get("Twitter"));
		}
		if (props.containsKey("Country")) {
			att.setCountry((String) props.get("Country"));
		}
		if (props.containsKey("Facebook")) {
			att.setFacebookId((String) props.get("Facebook"));
		}
		if (props.containsKey("Url")) {
			att.setUrl((String) props.get("Url"));
		}
		if (props.containsKey("Phone")) {
			att.setPhone((String) props.get("Phone"));
		}
		if (props.containsKey("Profile")) {
			att.setProfile((String) props.get("Profile"));
		}
		if (props.containsKey("Role")) {
			att.setRole((String) props.get("Role"));
		}
	}

	private static void validateProperties(Map<String, Object> props, ArrayList<String> missingProps, String check) {
		if (!props.containsKey(check)) {
			missingProps.add(check);
		}
	}

	public static FastSet<Attendee> getSpeakers(String trackKey) {
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
				speakers.addAll(presSpeakers);
			}
			return speakers;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

}
