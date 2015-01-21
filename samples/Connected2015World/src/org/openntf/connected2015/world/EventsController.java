package org.openntf.connected2015.world;

import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.conference.graph.Presentation;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DGraph;

import com.google.common.reflect.Reflection;
import com.tinkerpop.frames.FramedElement;
import com.tinkerpop.frames.FramedTransactionalGraph;

@Path("/graph/{graphname}")
public class EventsController {
	
	@GET
	@Path("/meta")
	public Response getGraphMetaData() {
		return Response.ok().entity("Not implemented").type(MediaType.TEXT_PLAIN).build();
	}
	
	@GET
	@Path("/v/{vertexclass}")
	public Response getPresentations(
			@PathParam("vertexclass") String vertexClassName) {
		
		// Initialize the graph
		ConferenceGraph graph = new ConferenceGraph();
		Class<FramedElement> vertexClass = null;
		try {
			vertexClass = (Class<FramedElement>) Class.forName("org.openntf.conference.graph."+vertexClassName);
		} catch (ClassNotFoundException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
		
		FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();
		Iterable<FramedElement> pres = framedGraph.getVertices(null, null, vertexClass);
		
		for (Method method : Presentation.class.getMethods())
		{
		    if (method.isAnnotationPresent(TypedProperty.class))
		    {
		        System.out.println(
		        		method.getAnnotation(TypedProperty.class)
		        );
		    }
		}
				
		return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity("ok").build();
	}

}
