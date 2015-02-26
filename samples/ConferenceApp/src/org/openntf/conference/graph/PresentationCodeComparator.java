package org.openntf.conference.graph;

import java.util.Comparator;

public class PresentationCodeComparator implements Comparator {
	
	public int compare(Object p1, Object p2) {
		if (p1 instanceof Presentation && p2 instanceof Presentation) {

			String code1 = ((Presentation) p1).getSessionId();
			String code2 = ((Presentation) p2).getSessionId();
			
			return code1.compareTo(code2);
		} else {
			return -1;
		}
	}
}
