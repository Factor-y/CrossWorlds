package org.openntf.connected2015.world;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/profiles")
public class ProfilesController {
	
	/**
	 * @param request
	 * @return
	 */
	@POST
	@Path("/person")
	public Response newOrExistingProfile(@Context HttpServletRequest request) {
		
		return null;
	}
	
	@PUT
	@Path("/person") 
	public Response updateProfileInfo(
			@QueryParam("fisrtname") String FirstName,
			@QueryParam("lastname") String LastName) {
		
		return null;
	}

	/**
	 * @param profilekey
	 * @return
	 */
	@GET
	@Path("/person/{profilekey}")
	public Response getProfileData(@PathParam("profilekey") String profilekey) {

		return null;
	}
	
	@POST
	@Path("/group")
	public Response newOrExistingGroup() {
		return null;
	}

}
