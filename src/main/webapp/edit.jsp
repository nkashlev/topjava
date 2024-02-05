<%--@elvariable id="meal" type="com.sun.corba.se.impl.ior.GenericIdentifiable"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="dateTime">DateTime:</label>
    <input type="datetime-local" id="dateTime" name="dateTime" style=position:absolute;left:6%; required>

    <br>
    <br>

    <label for="description">Description:</label>
    <input type="text" id="description" name="description" style=position:absolute;left:6%; required>

    <br>
    <br>

    <label for="calories">Calories:</label>
    <input type="number" id="calories" name="calories" style=position:absolute;left:6%; required>

    <br>
    <br>

    <input type="submit" value="Save"> <button onclick="window.history.back()" type="button">Cancel</button>
</form>

</body>
</html>