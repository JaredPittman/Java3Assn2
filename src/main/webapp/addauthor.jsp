<%--
  Created by IntelliJ IDEA.
  User: Jpitt
  Date: 2024-02-27
  Time: 2:51 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Author!</title>
</head>
<body>

<h1>Add Author</h1>

<form action = "library-data" method = "POST">
    fName: <input type = "text" name = "first_name"> <br />
    lName: <input type = "text" name = "last_name" /> <br />
    <input type="hidden" id="view" name="view" value="add_author">
    <input type = "submit" value = "Submit" />
</form>

<a href="index.jsp">Back to Main Menu</a>

</body>
</html>
