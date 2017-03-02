<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 2/28/2017
  Time: 10:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Page</title>
</head>
<body>
<h1>Search for friends!</h1>
<form action="/search" method="get">
      Search for a name: <input type="text" name="name"><br>
      <input type="submit" value="Search">
</form>
<p>Still need to register? Go <a href="index.jsp">here</a>.</p>
<p>Already registered? Get recommendations for friends <a href="friends.jsp">here</a>.</p>
</body>
</html>
