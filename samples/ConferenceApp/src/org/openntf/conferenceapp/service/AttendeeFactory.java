package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.Track;
import org.openntf.domino.graph2.DGraph;
import org.openntf.domino.utils.Strings;

import com.google.common.collect.Lists;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedTransactionalGraph;

import javolution.util.FastSet;

/**
 * Factory class for access to Attendees / Speakers
 * 
 * @author Paul Stephen Withers
 * 
 */
public class AttendeeFactory {

	/**
	 * Retrieves a specific Attendee based on email address
	 * 
	 * @param email
	 *            String primary email of the user
	 * @return Attendee corresponding to the email
	 */
	@SuppressWarnings("unchecked")
	public static Attendee getAttendeeByEmail(String email) {

		FramedTransactionalGraph<DGraph> framedGraph = (FramedTransactionalGraph<DGraph>) ConferenceGraphFactory.getGraph("engage");

		Iterable<Attendee> vs = framedGraph.getVertices("Email", email, Attendee.class);
		if (vs.iterator().hasNext()) {
			return vs.iterator().next();
		} else {
			return null;
		}
	}

	/**
	 * Adds an attendee using a Map of properties, validating before attempting
	 * an insertion
	 * 
	 * @param props
	 *            Map<String, Object> of properties
	 * @return Attendee successfully entered or String of errors
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * Updates properties for the relevant Attendee
	 * 
	 * @param props
	 *            Map<String, Object> of properties and values to update
	 * @param att
	 *            Attendee to update
	 */
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

	/**
	 * Validates a given property have been supplied
	 * 
	 * @param props
	 *            Map of properties to validate against
	 * @param missingProps
	 *            ArrayList of properties not supplied
	 * @param check
	 *            String property to check for
	 */
	private static void validateProperties(Map<String, Object> props, ArrayList<String> missingProps, String check) {
		if (!props.containsKey(check)) {
			missingProps.add(check);
		}
	}

	/**
	 * Gets a FastSet of Speaker Attendee objects for the relevant Track
	 * 
	 * @param trackKey
	 *            String key for the Trackk to check
	 * @return FastSet<Attendee> Speakers for the given track
	 */
	@SuppressWarnings("unchecked")
	public static FastSet<Attendee> getSpeakers(String trackKey) {
		FastSet<Attendee> retVal_ = null;
		try {
			FramedGraph<DGraph> graph = ConferenceGraphFactory.getGraph("engage");
			List<Presentation> presentations = new ArrayList<Presentation>();
			FastSet<Attendee> speakers = new FastSet<Attendee>();
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
