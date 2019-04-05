<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.page contentType="text/html; charset=UTF-8" />
<jsp:directive.page pageEncoding="UTF-8" />
<html>
<head>
    <title>Duels</title>
    <c:if test="${requestScope.waitingFight}">
        <meta http-equiv="refresh" content="10">
    </c:if>
    <style type="text/css">
        form { text-align: center}
    </style>
</head>
<body>
<c:choose>
    <c:when test="${requestScope.waitingFight}">
        <p align="center">Ведется побор соперника. Для отмены нажмите "Выйти".</p>
        <form action="${requestScope.contextPath}/arena/duels" method="get">
            <input type="submit" value="Обновить" align="center">
        </form>
        <form action="${requestScope.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="cancel" align="center">
            <input type="submit" value="Выйти" align="center">
        </form>
    </c:when>
    <c:otherwise><%--@elvariable id="ratingAttr" type="java.util.Map<String,String>"--%>
        <table align="center">
            <tr>
                <td>Рейтинг</td>
                <td>${ratingAttr.rating}</td>
            </tr>
            <tr>
                <td>Побед</td>
                <td>${ratingAttr.rating}</td>
            </tr>
            <tr>
                <td>Поражений</td>
                <td>${ratingAttr.rating}</td>
            </tr>
        </table>
        <form action="${requestScope.contextPath}/arena/duels" method="post">
            <input type="hidden" name="action" value="start">
            <input type="submit" value="Начать">
        </form>
        <p align="center"><a href="${requestScope.contextPath}/arena">Главное меню</a></p>
    </c:otherwise>
</c:choose>
<p align="center" ><jsp:include page="/techInfo" /></p>
</body>
</html>