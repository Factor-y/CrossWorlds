package org.openntf.xworlds.appservers.webapp.config;

public interface XWorldsApplicationConfiguration {

	/**
	 * @return the Notes full name for the user that will be treated as the signer for this application.
	 * May return null to indicate no signer has been specified. In that case the server/client identity 
	 * is used.
	 */
	public String getAppSignerFullName();
	
}
