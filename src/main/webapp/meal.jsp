<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method = "POST" action="meals">
    <table border = "0">
        <jsp:useBean id="meal" scope="request" type="java.lang.Object"/>
        <tr style = "background: lightgrey">
            <td colspan="2"><b>DateTime</b></td>
            <td colspan="2"><label>
                <input name="date" type="datetime-local" value="<c:out value="${meal.dateTime}" />"/>
            </label></td>
        </tr>
        <tr style = "background: beige">
            <td colspan="2"><b>Description</b></td>
            <td colspan="2"><label>
                <input name="description" size="50" type="text" value="<c:out value="${meal.description}" />"/>
            </label></td>
        </tr>
        <tr style = "background: lightgrey">
            <td colspan="2">Calories</td>
            <td colspan="2"><label>
                <input name="calories" size="50" type="text" value="<c:out value="${meal.calories}" />"/>
            </label></td>
        </tr>
        <tr style = "background: yellowgreen">
            <td colspan = "0"><input type = "submit" name="action" value = "save"/></td>
            <td colspan = "0"><input type = "submit" name="action" value = "cancel"/></td>
        </tr>
    </table>
</form>
</body>
</html>
