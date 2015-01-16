package org.openntf.xworlds.demo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;

@Path("/server")
public class Server {
	
	class DatabaseBean {
		private String apiPath;
		private String replicaid;
		private String title;
		
		public DatabaseBean(String apiPath,
				String title,
				String replicaId) {
			this.apiPath = apiPath;
			this.title = title;
			this.replicaid = replicaId;
		}

		public String getApiPath() {
			return apiPath;
		}

		public void setApiPath(String apiPath) {
			this.apiPath = apiPath;
		}

		public String getReplicaid() {
			return replicaid;
		}

		public void setReplicaid(String replicaid) {
			this.replicaid = replicaid;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		
	}
	
	@GET
	@Path("/dbdirectory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDbDirectory(@QueryParam("servername") String ServerName) {
		
		Session s = Factory.getSession(SessionType.CURRENT);
		
		DbDirectory dbdir = s.getDbDirectory(ServerName);
		
		Iterator<Database> i = dbdir.iterator();
		
		List<DatabaseBean> dbs = new ArrayList<Server.DatabaseBean>();
		
		while (i.hasNext()) {
			Database db = i.next();
			dbs.add(new DatabaseBean(db.getApiPath(), db.getTitle(), db.getReplicaID()));
		}
		
		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(dbs).build();
		
	}
	
	@GET
	@Path("/runas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response runAsUser(@QueryParam("username") String userName) {
		
		if (checkNotNull(userName,"username parameter must be specified") == null) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("username must be specified").build();
		}
		
		lotus.domino.Session rawSession = null;
		
		try {
			String runAs = userName;
			long userHandle = NotesUtil.createUserNameList(runAs);
			rawSession = XSPNative.createXPageSessionExt(runAs, userHandle, false, true, true);
			
			return Response.ok().entity(rawSession.getEffectiveUserName()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
	}

	@GET
	@Path("/runagent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDbDirectory(@QueryParam("servername") String serverName,
			@QueryParam("dbname") String dbname, @QueryParam("agentname") String agentName) {
		
		Session s = Factory.getSession(SessionType.CURRENT);
		
		Database db = s.getDatabase(serverName, dbname);

		Agent agent = db.getAgent(agentName);
		
		agent.run();
		
		return Response.status(200).type(MediaType.APPLICATION_JSON).entity(agent.getLastRun().toString()).build();
		
	}

}
