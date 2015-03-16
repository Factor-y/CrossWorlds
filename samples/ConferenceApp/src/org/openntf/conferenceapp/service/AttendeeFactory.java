package org.openntf.conferenceapp.service;

import java.util.ArrayList;
import java.util.Map;

import org.openntf.conference.graph.Attendee;
import org.openntf.domino.graph2.DGraph;
import org.openntf.domino.utils.Strings;

import com.tinkerpop.frames.FramedTransactionalGraph;

public class AttendeeFactory {

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

}
