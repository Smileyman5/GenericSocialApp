<%@page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>
<% 
out.println("Hello, " + request.getSession().getAttribute("username") + ".<br>");
ArrayList<String> conFriends = (ArrayList<String>) request.getAttribute("conFriends");
if (conFriends != null && conFriends.size() != 0) {
	out.println("Current Friends:<br>");
	for (String friend: conFriends) {
		out.println(friend + "<br>");
	}
	out.println("<br><br>");
}
	
ArrayList<String> penFriends = (ArrayList<String>) request.getAttribute("penFriends");
if (penFriends != null && penFriends.size() != 0) {
	out.println("Pending Friend Requests:<br>");
	for (String friend: penFriends) {
		out.println(friend + "<br>");
	}
	out.println("<br><br>");
}

ArrayList<String> reqFriends = (ArrayList<String>) request.getAttribute("reqFriends");
if (reqFriends != null && reqFriends.size() != 0) {
	out.println("Friend Requests:<br>");
	for (String friend: reqFriends) {
		%>
		<li>
		<%out.println(friend);%>
		<form action="/friends" method="post">
		<input type="hidden" name="confirm" value="true" />
		<input type="hidden" name="name" id="name" value="<%out.println(friend);%>" />
		<input type="submit" value="Confirm" />
		</form>
		<form action="/friends" method="post">
		<input type="hidden" name="decline" value="true" />
		<input type="hidden" name="name" id="name" value="<%out.println(friend);%>" />
		<input type="submit" value="Decline" />
		</form>
		</li>  
		<%
		out.println("<br>");
	}
	out.println("<br><br>");
}
%>

 <form action="/profile" method="post">
 Search for a name: <input type="text" name="user"><br>
<input type="submit" value="Submit" onclick="form.action='profile';" />
</form>
<%
ArrayList<String> search = (ArrayList<String>) request.getAttribute("search");
if (search != null) {
	for (String friend: search) {
		%>
		<li>
		<%out.println(friend);%>
		<form action="/friends" method="post">
		<input type="hidden" name="friend" value="true" />
		<input type="hidden" name="name" id="name" value="<%=friend%>" />
		<input type="submit" value="Send Friend Request" />
		</form>
		</li>
		<%
	}
}
%>
</body>
</html>