<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>From date (including)</th>
            <th>To date (including)</th>
            <th>From time (including)</th>
            <th>To time (excluding)</th>
        </tr>
        </thead>
        <jsp:useBean id="startDate" type="java.time.LocalDate" scope="request"/>
        <jsp:useBean id="endDate" type="java.time.LocalDate" scope="request"/>
        <jsp:useBean id="startTime" type="java.time.LocalTime" scope="request"/>
        <jsp:useBean id="endTime" type="java.time.LocalTime" scope="request"/>
        <form method="get" action="meals">
            <tr>
                <td><input type="date" value="${startDate}" name="startDate"></td>
                <td><input type="date" value="${endDate}" name="endDate"></td>
                <td><input type="time" value="${startTime}" name="startTime"></td>
                <td><input type="time" value="${endTime}" name="endTime"></td>
            </tr>
            <button type="submit" name="submit" value="submit">Apply filter</button>
            <button onclick="" type="button">Filter reset</button>
        </form>
    </table>
    <br><br>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>