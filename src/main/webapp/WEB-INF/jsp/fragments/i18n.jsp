<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    var i18n = [];
    <%-- user.add/user.edit or meal.add/meal.edit --%>
    i18n["addTitle"] = '<spring:message code="${param.page}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.page}.edit"/>';

    <c:forEach var='key' items='<%=new String[]{
            "common.deleted", "common.saved", "common.enabled", "common.disabled", "common.errorStatus", "common.confirm",
            "common.users_unique_email_idx", "common.validation_error", "common.internal_server_error", "common.app_error",
            "common.data_error", "common.data_not_found", "common.meals_unique_user_datetime_idx"}%>'>
    i18n['${key}'] = '<spring:message code="${key}"/>';
    </c:forEach>
</script>