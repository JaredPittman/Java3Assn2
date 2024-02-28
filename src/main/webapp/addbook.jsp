<%--
  Created by IntelliJ IDEA.
  User: Jpitt
  Date: 2024-02-27
  Time: 2:49 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add a book to the database!</title>
</head>
<body>

<h1>Add a Book!</h1>

<form action = "library-data" method = "POST">
    ISBN: <input type = "text" name = "isbn"> <br />
    Title: <input type = "text" name = "title" /> <br />
    Edition Number: <input type = "text" name = "edition_number" /> <br />
    Copyright: <input type = "text" name = "copyright" />
    Author: <input type = "text" name = "author_id" />
    <input type="hidden" id="view" name="view" value="add_book">
    <input type = "submit" value = "Submit" />
</form>

<a href="index.jsp">Back to Main Menu</a>


</body>
</html>
