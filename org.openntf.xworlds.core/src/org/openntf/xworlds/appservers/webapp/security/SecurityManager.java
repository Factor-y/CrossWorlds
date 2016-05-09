package org.openntf.xworlds.appservers.webapp.security;

import javax.servlet.http.HttpServletRequest;

import org.openntf.xworlds.appservers.webapp.config.DefaultXWorldsApplicationConfig;

public class SecurityManager {
	
	public static final String ANON_FULLNAME = "Anonymous";

	public static void setDominoFullName(HttpServletRequest request, String fullName) {
		
		if (fullName == null || fullName.equals(ANON_FULLNAME)) {
			request.getSession().removeAttribute("xworlds.request.username");
			DefaultXWorldsApplicationConfig.setDominoFullName(ANON_FULLNAME);
		} else {
			request.getSession().setAttribute("xworlds.request.username", fullName);
			DefaultXWorldsApplicationConfig.setDominoFullName(fullName);
		}
		
	}
	
	public static String getDominoFullName(HttpServletRequest request) {
		if (request.getSession(false) != null && request.getSession(false).getAttribute("xworlds.request.username") != null) {
			return (String) request.getSession().getAttribute("xworlds.request.username");
		}
		return ANON_FULLNAME;
	}
}
