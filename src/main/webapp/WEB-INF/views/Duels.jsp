<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Duels</title>
    <c:if test="${not empty sessionScope.waitingFight}">
        <meta http-equiv="refresh" content="10">
    </c:if>
</head>
<body>
<c:choose>
    <c:when test="${empty sessionScope.waitingFight}">
        <form action="${pageContext.request.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="start">
            <input type="submit" value="Начать">
        </form>
    </c:when>
    <c:otherwise>
        <p>Ведется побор соперника. Для отмены нажмите "Выйти".</p>
        <form action="${pageContext.request.contextPath}/arena/duels" method="get">
            <input type="submit" value="Обновить">
        </form>
        <form action="${pageContext.request.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="cancel">
            <input type="submit" value="Выйти">
        </form>
    </c:otherwise>
</c:choose>
<p align="center" ><jsp:include page="/techInfo" /></p>
</body>
</html>
