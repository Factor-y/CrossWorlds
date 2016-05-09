package org.openntf.conferenceapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@Path("/rooms")
public class RoomsController {
	
	@GET
	public Response getSessions() {
		
		Session s = Factory.getSession(SessionType.NATIVE);
		
		Database db = s.getDatabase("Lotussphere/sphere2015.nsf");
		
		return Response.ok().entity(db.getTitle()).build();
	}

}
