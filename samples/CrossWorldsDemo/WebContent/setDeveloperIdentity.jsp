<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CrossWorlds devloper mode - Setting session identity</title>
</head>
<body>
<%

	// Invalidate
	session.invalidate();
	// Get new session
	session = request.getSession(true);
	// Override identity
	String newIdentity = request.getParameter("fullname");
	if (newIdentity == null) {
		session.removeAttribute("xworlds.request.username");
	} else {
		session.setAttribute("xworlds.request.username", newIdentity);
	}
	
%>
New user identity set to <strong><%= session.getAttribute("xworlds.request.username") %></strong>
<br><br>
<form action="setDeveloperIdentity.jsp" method="post">
	<input type="text" name="fullname" size="80" value="<%= session.getAttribute("xworlds.request.username") != null ? session.getAttribute("xworlds.request.username") : "Anonymous" %>">
	<input type="submit" value="Change identity" >
</form>
</body>
</html>