package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.common.annotations.Beta;

@Beta
public interface XWorldsApplicationConfigurator {

	/**
	 * This constant is the name of the ServletContext attribute holding the application configuration.
	 */
	public static final String APPCONTEXT_ATTRS_CWAPPCONFIG = "org.openntf.crossoworlds.appconfig";
	/**
	 * 
	 */
	public static final String APPCONTEXT_ATTRS_CWAPPIDENTITY = "org.openntf.crossoworlds.appsignername";

	/**
	 * @param context
	 */
	public void configure(ServletContext context);
	
	/**
	 * 
	 */
	public void build();
	
	/**
	 * @param request
	 * @param response
	 */
	public void setupRequest(ServletRequest request, ServletResponse response);
	
}
