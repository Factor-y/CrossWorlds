package org.openntf.xworlds.demo;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.groovy.control.CompilerConfiguration;

@Path("/play")
public class PlayWithCrossWorlds {
	
	private CompilerConfiguration compilerConfig = null;

	public PlayWithCrossWorlds() {
		compilerConfig = new CompilerConfiguration();
		
		compilerConfig.setDebug(true);
		compilerConfig.setVerbose(true);
		
	}
	
	@Path("/runscript")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response runScript(
			@FormParam("scriptbody") String scriptBody
			) {

		StringWriter sw = new StringWriter();
		
		// call groovy expressions from Java code
		Binding binding = new Binding();
		binding.setVariable("out", new PrintWriter(sw));
		
		Map<String,String> result = new HashMap<String,String>();

		Object value = null; 
		try {
			
			GroovyShell shell  = new GroovyShell(binding, compilerConfig);
			value = shell.evaluate(scriptBody);
			
		} catch (Exception e) {
			result.put("errormessage", e.getMessage());
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			result.put("exceptionStack", sw.toString());
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON_TYPE)
					.entity(result)
					.build();
		} catch (Error e) {
			result.put("errormessage", e.getMessage());
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			result.put("exceptionStack", sw.toString());
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.type(MediaType.APPLICATION_JSON_TYPE)
					.entity(result)
					.build();
		}
		
		
		result.put("value", value.toString());
		result.put("out", sw.toString());
		
		return Response
				.ok(result, MediaType.APPLICATION_JSON_TYPE)
				.build();
	}

}
