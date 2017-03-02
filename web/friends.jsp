<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 2/28/2017
  Time: 10:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Friend Recommendations</title>
</head>
<body>
<h1>Friend Recommendations</h1>
<form action="/compare" method="get">
      <input type="submit" value="Recommend">
</form>
<p>Already have friends here? Search <a href="search.jsp">here</a>.</p>
<% if (request.getAttribute("message") == null) { %>
    <p>Still need to register? Go <a href="register.jsp">here</a>.</p>
<% } %>
${message}
</body>
</html>
