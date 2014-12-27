package org.openntf.xworlds.scratch;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class DominoRestApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(Server.class);
        return classes;
	}

	
	
}
