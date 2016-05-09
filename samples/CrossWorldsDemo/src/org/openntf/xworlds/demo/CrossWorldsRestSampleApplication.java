package org.openntf.xworlds.demo;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class CrossWorldsRestSampleApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(Server.class);
        classes.add(TestForPerformance.class);
        return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> classes = new HashSet<Object>();
		classes.add(new PlayWithCrossWorlds());
		return classes;
	}

	
	
	
}
