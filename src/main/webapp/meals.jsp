<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<p><a href="meals?action=add">Add meal</a></p>
<table border=1>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th> </th>
        <th> </th>
    </tr>
    </thead>
    <tbody>

    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsTo}" var="MealTo">
        <tr>
            <tr style="color:${MealTo.excess ? 'red' : 'green'}">
            <td>
                <fmt:parseDate  value="${MealTo.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
            </td>
            <td><c:out value="${MealTo.description}" /></td>
            <td><c:out value="${MealTo.calories}"/></td>
            <td><a href="meals?action=update&date=<c:out value="${MealTo.dateTime}"/>">Update</a></td>
            <td><a href="meals?action=delete&date=<c:out value="${MealTo.dateTime}"/>">Delete</a></td>
        </tr>
    </c:forEach>

    </tbody>
</table>
</body>
</html>
