<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Дуэль</title>
    <c:if test="${not empty requestScope.timer}">
        <meta http-equiv="refresh" content="${requestScope.timer}">
    </c:if>
</head>
<body>
<c:if test="${not empty requestScope.timer}">
    <p align="center">До начала боя осталось ${requestScope.timer} секунд.</p>
    <p align="center">Страница обновится автоматически.</p>
</c:if>
</body>
</html>
