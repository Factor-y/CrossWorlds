package org.openntf.conference.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;

import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.tinkerpop.frames.FramedTransactionalGraph;

public class DataInitializerIconUk implements Runnable {
	private long marktime;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public DataInitializerIconUk() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning dataInitializer...");

			// Get / create databases
			Session s = Factory.getSession(SessionType.NATIVE);
			Database attendees = s.getDatabase(s.getServerName(), ConferenceGraph.ATTENDEE_PATH, true);
			attendees.getAllDocuments().removeAll(true);
			Database events = s.getDatabase(s.getServerName(), ConferenceGraph.EVENT_PATH, true);
			events.getAllDocuments().removeAll(true);
			Database groups = s.getDatabase(s.getServerName(), ConferenceGraph.GROUP_PATH, true);
			groups.getAllDocuments().removeAll(true);
			Database invites = s.getDatabase(s.getServerName(), ConferenceGraph.INVITE_PATH, true);
			invites.getAllDocuments().removeAll(true);
			Database location = s.getDatabase(s.getServerName(), ConferenceGraph.LOCATION_PATH, true);
			location.getAllDocuments().removeAll(true);
			Database times = s.getDatabase(s.getServerName(), ConferenceGraph.TIMES_PATH, true);
			times.getAllDocuments().removeAll(true);
			Database defaults = s.getDatabase(s.getServerName(), ConferenceGraph.DEFAULT_PATH, true);
			defaults.getAllDocuments().removeAll(true);

			// Initialize the graph
			ConferenceGraph graph = new ConferenceGraph();
			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();

			loadData(s, framedGraph);

			Iterable<Presentation> pres = framedGraph.getVertices(null, null, Presentation.class);

			for (Presentation presentation : pres) {
				System.out.println(presentation.getTitle());
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	private static String readUrl(final String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public void loadData(final org.openntf.domino.Session s, final FramedTransactionalGraph<DGraph> framedGraph) {
		HashMap<String, String> locs = new HashMap<String, String>();
		locs.put("1", "Room 1");
		locs.put("2", "Room 2");
		locs.put("3", "Room 3");
		locs.put("4", "Room 4");
		HashMap<String, Track> tracks = new HashMap<String, Track>();
		HashMap<String, String> trackLkup = new HashMap<String, String>();
		trackLkup.put("6", "Management/Strategy");
		trackLkup.put("4", "Development");
		trackLkup.put("1", "Administration");
		trackLkup.put("3", "Beyond The Everyday");
		trackLkup.put("5", "ICON UK");
		int sessNo = 1;
		try {
			String urlData = readUrl("http://iconuk.org/iconuk/dragon/dragoniconuk2015.nsf/getAgendaICONUK.xsp?format=json");

			JsonJavaFactory factory = JsonJavaFactory.instanceEx;

			JsonJavaObject jsonData = (JsonJavaObject) JsonParser.fromJson(factory, urlData);

			ArrayList<JsonJavaObject> jsonRecords = (ArrayList<JsonJavaObject>) jsonData.get("records");
			SimpleDateFormat sdf = new SimpleDateFormat();
			for (JsonJavaObject record : jsonRecords) {
				JsonJavaObject obj = record.getAsObject("data");
				String locKey = obj.getAsString("session_locationNumber");
				Location loc = framedGraph.addVertex(locs.get(locKey), Location.class);
				if (Strings.isBlankString(loc.getName())) {
					loc.setName(locs.get(locKey));
				}

				String trackKey = obj.getAsString("session_tracknumber");
				Track track = framedGraph.addVertex(trackKey, Track.class);
				if (Strings.isBlankString(track.getTitle())) {
					track.setTitle(trackKey);
					track.setDescription(trackLkup.get(trackKey));
				}

				// DO DATES
				String date = obj.getAsString("session_daynumber");
				String actualDate;
				if ("1".equals(date)) {
					actualDate = "21/09/2015";
				} else {
					actualDate = "22/09/2015";
				}
				String time = obj.getAsString("session_time");
				String startTime = time.substring(0, 5);
				String endTime = time.substring(8);
				String manipulated = actualDate + " " + startTime;
				Date startDate = DATE_FORMAT.parse(manipulated);
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(startDate);
				startCal.set(Calendar.SECOND, 0);
				startCal.set(Calendar.MILLISECOND, 0);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(startDate);
				endCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Strings.left(endTime, ":")));
				endCal.set(Calendar.MINUTE, Integer.parseInt(Strings.right(endTime, ":")));
				endCal.set(Calendar.SECOND, 0);
				endCal.set(Calendar.MILLISECOND, 0);

				String tsKey = sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
				TimeSlot ts = framedGraph.addVertex(tsKey, TimeSlot.class);
				ts.setStartTime(startCal);
				ts.setEndTime(endCal);
				ts.setOfficial(1);

				Presentation sess = framedGraph.addVertex(sessNo, Presentation.class);
				sess.setTitle(obj.getAsString("session_name"));
				sess.setDescription(obj.getAsString("session_abstract"));
				sess.setStatus(Event.Status.CONFIRMED);
				sess.setSessionId(Integer.toString(sessNo));
				System.out.println("Assigning location - " + loc.getName() + " to session " + obj.getAsString("session_name"));
				sess.addLocation(loc);
				track.addIncludesSession(sess);

				ts.addEvent(sess);

				for (int i = 1; i < 6; i++) {
					String suffix = "";
					if (i > 1) {
						suffix = Integer.toString(i);
					}
					String speaker = obj.getAsString("speaker_name" + suffix);
					if ("".equals(speaker)) {
						break;
					}
					String speakerName = speaker;
					String organization = obj.getAsString("speaker_org" + suffix);
					Attendee att = framedGraph.addVertex(speakerName, Attendee.class);
					System.out.println(speaker);
					int sep = speakerName.indexOf(" ");
					if (sep > -1) {
						String firstName = speakerName.substring(0, sep);
						String lastName = speakerName.substring(sep + 1, speakerName.length());
						att.setFirstName(firstName);
						att.setLastName(lastName);
					} else {
						att.setFirstName(speakerName);
					}

					sess.addPresentedBy(att);
					sess.addAttendingAttendee(att);
					sess.addPlansToAttend(att);
				}

				sessNo++;

			}

			// Now do speakers
			urlData = readUrl("http://iconuk.org/iconuk/dragon/dragoniconuk2015.nsf/getspeakerdataICONUK.xsp?format=json");

			jsonData = (JsonJavaObject) JsonParser.fromJson(factory, urlData);

			jsonRecords = (ArrayList<JsonJavaObject>) jsonData.get("records");
			for (JsonJavaObject record : jsonRecords) {
				JsonJavaObject obj = record.getAsObject("data");

				String speakerName = obj.getAsString("speaker_name");
				Attendee att = framedGraph.getVertex(speakerName, Attendee.class);
				if (null != att) {

					att.setUrl(obj.getAsString("speaker_photourl"));
					att.setProfile(obj.getAsString("speaker_bio"));
				}
			}

			// Now do sponsors
			urlData = readUrl("http://iconuk.org/iconuk/dragon/dragoniconuk2015.nsf/getsponsordataICONUK.xsp");

			jsonData = (JsonJavaObject) JsonParser.fromJson(factory, urlData);

			jsonRecords = (ArrayList<JsonJavaObject>) jsonData.get("records");
			for (JsonJavaObject record : jsonRecords) {
				JsonJavaObject obj = record.getAsObject("data");
				String name = obj.getAsString("sponsor_name");
				String level = obj.getAsString("sponsor_level_value");
				String photoUrl = obj.getAsString("sponsor_logourl");
				String website = obj.getAsString("sponsor_website");
				String profile = obj.getAsString("sponsor_abstract");
				Level sponsorLevel = null;
				if ("1".equals(level)) {
					sponsorLevel = Level.PLATINUM;
				} else if ("2".equals(level)) {
					sponsorLevel = Level.GOLD;
				} else if ("3".equals(level)) {
					sponsorLevel = Level.SILVER;
				} else if ("4".equals(level)) {
					sponsorLevel = Level.BRONZE;
				}
				createSponsor(framedGraph, name, sponsorLevel, photoUrl, website, profile);
			}

			framedGraph.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Sponsor createSponsor(FramedTransactionalGraph<DGraph> framedGraph, String name, Level level, String photoUrl, String webUrl,
			String profile) {
		Sponsor org = null;
		org = framedGraph.addVertex(name, Sponsor.class);
		org.setName(name);
		org.setType(Group.Type.COMPANY);
		org.setLevel(level);
		org.setPhotoUrl(photoUrl);
		org.setUrl(webUrl);
		org.setProfile(profile);
		return org;
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DataInitializerIconUk(), TestRunnerUtil.NATIVE_SESSION);
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

}
