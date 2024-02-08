<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <h3><a href="index.html">Home</a></h3>
    <c:set var="title" value="${param.action eq 'create' ? 'Add Meal' : 'Edit Meal'}" />
    <h2>${title}</h2>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="dateTime">DateTime:</label>
    <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"
           style="position:absolute;left:6%;"
           required>

    <br>
    <br>

    <label for="description">Description:</label>
    <input type="text" id="description" name="description" value="${meal.description}"
           style="position:absolute;left:6%;"
           required>

    <br>
    <br>

    <label for="calories">Calories:</label>
    <input type="number" id="calories" name="calories" value="${meal.calories}" style="position:absolute;left:6%;"
           required>

    <br>
    <br>

    <input type="submit" value="Save">
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>

</body>
</html>