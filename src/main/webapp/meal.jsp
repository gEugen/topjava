<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Edit meal</title>
</head>
<style>
    TABLE {
        width: 500px; /* table width */
        border-collapse: collapse;
    }
    .col1 {
        width: 21%;
    }
    .col2 {
        width: 79%;
    }
    TD, TH {
        padding: 2px; /* field around width */
        border: 1px solid black;
    }
    TH {
        background: whitesmoke;
    }
</style>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method = "POST" action="meals">
    <table>
        <jsp:useBean id="meal" scope="request" type="java.lang.Object"/>
        <td style="display: none"><label>
            <input name="id" type="text" value="${meal.id}"/>
        </label></td>
        <tr style = "background: lightgrey">
            <td class="col1" colspan="2"><b>DateTime</b></td>
            <td class="col2" colspan="2"><label>
                <input name="date" type="datetime-local" value="${meal.dateTime}"/>
            </label></td>
        </tr>
        <tr style = "background: beige">
            <td colspan="2"><b>Description</b></td>
            <td colspan="2"><label>
                <input name="description" size="50" type="text" value="${meal.description}"/>
            </label></td>
        </tr>
        <tr style = "background: lightgrey">
            <td colspan="2">Calories</td>
            <td colspan="2"><label>
                <input name="calories" size="50" type="text" value="${meal.calories}"/>
            </label></td>
        </tr>
    </table>
    <p>
        <input style="padding: 7px" type = "submit" name="action" value = "save"/>
        <input style="padding: 7px" type = "button" onclick="location.href ='meals'" name="action" value =
                "cancel"/>
    </p>
</form>
</body>
</html>
