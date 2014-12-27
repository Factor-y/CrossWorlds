package org.openntf.xworlds.scratch;

import java.util.HashMap;
import java.util.Iterator;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@Path("/server")
public class Server {
	
	@GET
	@Path("/dbdirectory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDbDirectory(@QueryParam("servername") @DefaultValue("") String ServerName) {
		
		Session s = Factory.getSession(SessionType.TRUSTED);
		
		DbDirectory dbdir = s.getDbDirectory(ServerName);
		
		Iterator<Database> i = dbdir.iterator();
		
		HashMap<String, String> dbs = new HashMap<String, String>();
		
		while (i.hasNext()) {
			Database db = i.next();
			dbs.put(db.getTitle(), db.getApiPath());
		}
		
		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(dbs).build();
	}

}
