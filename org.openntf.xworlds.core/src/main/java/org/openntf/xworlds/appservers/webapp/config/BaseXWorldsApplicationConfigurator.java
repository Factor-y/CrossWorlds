package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseXWorldsApplicationConfigurator implements XWorldsApplicationConfigurator {

	private static final String ERROR_DONT_USE_DIRECTLY = "This base application configurator shouldn't be used directly, verify the configuration.";
	private ServletContext appContext = null;
	
	@Override
	public void configure(ServletContext context) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
		
	}

	@Override
	public void setupRequest(HttpServletRequest request, HttpServletResponse response) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	public ServletContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ServletContext appContext) {
		this.appContext = appContext;
	}

}
