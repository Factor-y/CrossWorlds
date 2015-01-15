package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class BaseXWorldsApplicationConfigurator implements XWorldsApplicationConfigurator {

	private static final String ERROR_DONT_USE_DIRECTLY = "This base application configurator shouldn't be used directly, verify the configuration.";
	protected ServletContext appContext = null;
	
	@Override
	public void configure(ServletContext context) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
		
	}

	public void build() {
		appContext.setAttribute(APPCONTEXT_ATTRS_CWAPPCONFIG, this);
	}

	@Override
	public void setupRequest(ServletRequest request, ServletResponse response) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

}
