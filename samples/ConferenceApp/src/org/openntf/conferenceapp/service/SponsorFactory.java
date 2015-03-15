package org.openntf.conferenceapp.service;

import java.util.Iterator;
import java.util.List;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.Sponsor;
import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.ibm.commons.util.StringUtil;
import com.tinkerpop.frames.FramedGraph;

public class SponsorFactory {

	static boolean debug = false;

	public static List<Sponsor> getSponsorsSortedByProperty(String property) {
		List<Sponsor> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Iterable<Sponsor> sponsors = graph.getVertices(null, null, Sponsor.class);
			if (StringUtil.isEmpty(property)) {
				property = "Name";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(sponsors);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	public static List<Sponsor> getSponsorsForLevelSortedByProperty(Level level, String property) {
		List<Sponsor> retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			List<Sponsor> sponsors = Lists.newArrayList(graph.getVertices(null, null, Sponsor.class));
			Iterator it = sponsors.iterator();
			while (it.hasNext()) {
				if (!level.equals(((Sponsor) it.next()).getLevel())) {
					it.remove();
				}
			}

			if (StringUtil.isEmpty(property)) {
				property = "Name";
			}
			Ordering ord = Ordering.from(new DVertexFrameComparator(property));
			retVal_ = ord.sortedCopy(sponsors);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

	public static Attendee getContactForSponsor(String sponsorName) {
		Attendee retVal_ = null;
		try {
			FramedGraph graph = ConferenceGraphFactory.getGraph("engage");
			Sponsor sponsor = (Sponsor) graph.getVertex(sponsorName, Sponsor.class);
			if (null != sponsor) {
				retVal_ = sponsor.getContactAttendees().iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal_;
	}

}
