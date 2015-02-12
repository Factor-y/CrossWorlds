package org.openntf.conferenceapp.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ConnectedApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return super.getClasses();
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> sings = new HashSet<Object>();
		sings.add(new ProfilesController());
		sings.add(new EventsController());
		return super.getSingletons();
	}
	
	

}
