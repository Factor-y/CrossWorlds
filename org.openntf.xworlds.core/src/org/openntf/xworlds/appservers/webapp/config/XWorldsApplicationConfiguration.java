package org.openntf.xworlds.appservers.webapp.config;

public interface XWorldsApplicationConfiguration {

	/**
	 * @return the Notes full name for the user that will be treated as the signer for this application.
	 * May return null to indicate no signer has been specified. In that case the server/client identity 
	 * is used.
	 */
	public String getAppSignerFullName();
	
	/**
	 * CrossWorlds can run in developer mode allowing identity switching to simplify development & testing 
	 * on a client based setup.
	 * 
	 * Developer mode is controlled by adding
	 * 
	 * CROSSWORLDSDEV=true
	 * 
	 * to Notes.ini
	 * 
	 * or by adding
	 * 
	 * xworlds.developermode=true
	 * 
	 * to bootstrap.properties
	 * 
	 * Notes.ini value takes precedence over bootstrap.ini setting.
	 * 
	 * @return true if CrossWorlds is running in developer mode.
	 */
	public boolean isDeveloperMode(); 
	
}
