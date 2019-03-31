<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Duels</title>
    <c:if test="${sessionScope.waitingFight}">
        <meta http-equiv="refresh" content="10">
    </c:if>
</head>
<body>
<c:choose>
    <c:when test="${sessionScope.waitingFight}">
        <p align="center">Ведется побор соперника. Для отмены нажмите "Выйти".</p>
        <form action="${pageContext.request.contextPath}/arena/duels" method="get">
            <input type="submit" value="Обновить" align="center">
        </form>
        <form action="${pageContext.request.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="cancel" align="center">
            <input type="submit" value="Выйти" align="center">
        </form>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="start" align="center">
            <input type="submit" value="Начать" align="center">
        </form>
        <p align="center"><a href="${pageContext.request.contextPath}/arena">Главное меню</a></p>
    </c:otherwise>
</c:choose>
<p align="center" ><jsp:include page="/techInfo" /></p>
</body>
</html>