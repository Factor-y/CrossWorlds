package org.openntf.xworlds.demo;

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
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

@Path("/test")
public class TestForPerformance {

	@GET
	public Response testCheck() {
		return Response.status(200).entity("OK").build();
	}

	@POST
	@Path("/fakename")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doMakeDocuments(
			@QueryParam("dbname") @DefaultValue("crossworlds\\demo.nsf") String dbname,
			@QueryParam("count") @DefaultValue("1") int docsToCreate) {

		Session s = Factory.getNamedSession(
				"CN=Daniele Vistalli,O=Factor-y,C=IT", false);

		Database db = s.getDatabase(dbname);

		long start = System.currentTimeMillis();

		int c = 0;
		for (; c < docsToCreate; c++) {

			Document doc = db.createDocument();

			doc.replaceItemValue("Form", "fUserName");
			doc.replaceItemValue("firstname", "A-Cross "
					+ (Math.random() * 1000));
			doc.replaceItemValue("lastname", "A-Worlds");

			doc.save();

		}

		System.out.println("Millisecs for running: "
				+ (System.currentTimeMillis() - start) + " for " + c
				+ " documents.");

		return Response.status(200).entity(c).build();

	}

}
