package org.openntf.xworlds.appservers.webapp.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.annotations.Beta;

@Beta
public interface XWorldsApplicationConfigurator {

	/**
	 * This constant is the name of the ServletContext attribute holding the application configuration.
	 */
	public static final String CONTEXTPARAM_CWAPPCONFIG_CLASS = "org.openntf.crossoworlds.appconfigurator.class";
	/**
	 * The notes full name for the identity to be used when a "SIGNER" session is required. 
	 */
	public static final String CONTEXTPARAM_CWAPPSIGNER_IDENTITY = "org.openntf.crossworlds.appsignername";
	/**
	 * The notes full name for the identity to be used when a "SIGNER" session is required. 
	 */
	public static final String CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY = "org.openntf.crossworlds.devtimename";
	/**
	 * This constant is the name of the ServletContext attribute holding the application configuration.
	 */
	public static final String APPCONTEXT_ATTRS_CWAPPCONFIG = "org.openntf.crossoworlds.appconfig";

	/**
	 * @param context
	 */
	public void configure(ServletContext context);
	
	/**
	 * 
	 */
	public XWorldsApplicationConfiguration build();
	
	/**
	 * @param request
	 * @param response
	 */
	public void setupRequest(HttpServletRequest request, HttpServletResponse response);
	
}
