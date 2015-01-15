package org.openntf.xworlds.appservers.webapp.utils;

import com.google.common.annotations.Beta;

import lotus.domino.Session;

@Beta
public class XWorldsFactory {

	/**
	 * Call this to get a session for the user currently executing in the thread.
	 * 
	 * - If the user is Anonymous you get an anonymous session
	 * - If the user is Authenticated by WebSphere security stack you get a session 
	 * for the loggedin user for it's security name
	 * - If the current thread is not running under a "user" security context but is 
	 * a "system" thread then a SessionType.NATIVE session is returned.
	 * 
	 * @return a Domino session with the identity of the current executing user for the thread.
	 */
	public static Session getCallerSession() {

		return null;
	}
	
	/**
	 * @return
	 */
	public static Session getDefaultSession() {
		
		return null;
	}

	public static String getCallerFullName() {
		
		return null;
	}
	
}
