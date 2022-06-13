<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        TABLE {
            width: 700px;
            border-collapse: collapse;
        }

        .col1 {
            width: 18%;
        }

        .col2 {
            width: 48%;
        }

        .col3 {
            width: 14%;
        }

        .col4 {
            width: 10%;
        }

        .col5 {
            width: 10%;
        }

        TD, TH {
            padding: 2px;
            border: 1px solid black;
        }

        TH {
            background: whitesmoke;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<p><a href="meals?action=add">Add meal</a></p>
<table>
    <thead>
    <tr style="background: lightgrey">
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>*</th>
        <th>-</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr>
        <tr style="color:${mealTo.excess ? 'red' : 'green'}">
            <td class="col1" style="text-align: center">
                <fmt:parseDate value="${mealTo.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
            </td>
            <td class="col2" style="text-align: left">${mealTo.description}</td>
            <td class="col3" style="text-align: center">${mealTo.calories}</td>
            <td class="col4" style="text-align: center"><a href="meals?action=update&id=${mealTo.id}">Update</a></td>
            <td class="col5" style="text-align: center"><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
