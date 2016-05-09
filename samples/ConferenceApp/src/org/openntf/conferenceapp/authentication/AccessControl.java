package org.openntf.conferenceapp.authentication;

public interface AccessControl {

	public boolean signIn(String username);

	public boolean isUserSignedIn();

	public boolean isUserInRole(String role);

	public String getPrincipalName();

	public void logout();
}
