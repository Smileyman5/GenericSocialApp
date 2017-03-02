<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 2/28/2017
  Time: 10:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en-US">
<head>
    <meta charset="UTF-8">
    <title>Generic Social App</title>
</head>
<body>
<h1>Welcome to Generic Social App!</h1>
<form action="index.jsp" method="post">
      Username: <input type="text" name="username"><br>
      Password: <input type="password" name="password"><br>
      Re-type Password: <input type="password" name="re_password"><br>
      <input type="submit" value="Register">
</form>
<p>Already have friends here? Search <a href="search.jsp">here</a>.</p>
</body>
</html>
