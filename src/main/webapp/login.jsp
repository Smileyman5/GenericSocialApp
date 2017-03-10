<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<title>Login Screen</title>
</head>
<body>
<form method="post" action="login">
<fieldset>
<legend>Login</legend>
<%
	if (request.getAttribute("criteria") != null && request.getAttribute("criteria").equals("true")) {
		%><font color="red">Some criteria were not filled in.<br><br></font><%
	}
	else if (request.getAttribute("failed") != null && request.getAttribute("failed").equals("true")) {
		%><font color="red">Username/Password are incorrect.<br><br></font><%
	}
%>
Username: <input type="text" name="username"/> <br /><br />
Password:  	<input type="password" name="password" /><br /><br />
</fieldset>
<input type="submit" value="Login" onclick="form.action='login';" />
<input type="submit" value="Register" onclick="form.action='register.jsp';" />
</form>

</body>
</html>