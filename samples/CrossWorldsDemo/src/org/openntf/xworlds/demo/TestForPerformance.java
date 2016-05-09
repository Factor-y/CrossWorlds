package org.openntf.xworlds.demo;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@Path("/test")
public class TestForPerformance {

	@GET
	public Response testCheck() {
		return Response.status(200).entity("OK - Test").build();
	}
	
	@GET
	@Path("/countdocs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response countDocumens() {
		Session s = Factory.getSession(SessionType.CURRENT);

		return Response.status(HttpServletResponse.SC_NOT_IMPLEMENTED).build();
	}

	@POST
	@Path("/makedocs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doMakeDocuments(
			@QueryParam("dbname") @DefaultValue("crossworlds\\test.nsf") String dbname,
			@QueryParam("count") @DefaultValue("1") int docsToCreate) {

		System.out.println("Servlet thread: " + Thread.currentThread().getId());
		
		Session s = Factory.getSession(SessionType.SIGNER);

		Database db = s.getDatabase(dbname);
		
		long start = System.currentTimeMillis();
		
		int c = 0;
		for (; c < docsToCreate; c++) {

			Document doc = db.createDocument();

			doc.replaceItemValue("Form", "fUserName",false);
			doc.replaceItemValue("Firstname", "A-Cross " + (Math.random() * 1000),false);
			doc.replaceItemValue("Lastname", "A-Worlds",false);

			doc.save();

		}

		System.out.println("Millisecs for running: "
				+ (System.currentTimeMillis() - start) + " for " + c
				+ " documents.");

		return Response.status(200).entity(c).build();

	}

}
