package org.openntf.conference.graph;

import java.util.Comparator;

public class EventNameComparator implements Comparator {
	
	public int compare(Object p1, Object p2) {
		if (p1 instanceof Event && p2 instanceof Event) {

			String code1 = ((Event) p1).getTitle();
			String code2 = ((Event) p2).getTitle();
			
			return code1.compareTo(code2);
		} else {
			return -1;
		}
	}
}
