<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<title>Login Screen</title>
<script src="js/jquery-3.1.1.js"></script>
<script src="js/login.js"></script>
</head>
<body>
<form>
<fieldset>
<legend>Login</legend>
<font color="red"><div id="wrong"></div></font>   
Username: <input type="text" name="username" id="username"/> <br /><br />
Password:  	<input type="password" name="password" id="password" /><br /><br />
</fieldset>
</form>
<button id="login">Login</button>
<button id="register">Register</button>

</body>
</html>