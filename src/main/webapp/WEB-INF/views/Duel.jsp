<%@ page import="ru.job4j.domain.duels.duelists.Duelist" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.page contentType="text/html; charset=UTF-8" />
<jsp:directive.page pageEncoding="UTF-8" />
<html>
<head>
    <title>Дуэль</title>
    <c:if test="${not empty requestScope.timer}">
        <meta http-equiv="refresh" content="${requestScope.timer}">
    </c:if>
    <c:if test="${not requestScope.canAttack}" >
        <meta http-equiv="refresh" content="10">
    </c:if>
    <style type="text/css">
        form { text-align: center}
    </style>
</head>
<body>
<h4 align="center">Дуэль</h4>
<c:if test="${not empty requestScope.timer}">
    <p align="center">До начала боя осталось ${requestScope.timer} секунд.</p>
    <p align="center">Страница обновится автоматически, по истечению таймера.</p>
</c:if>
<table align="center">
    <thead>
    <tr>
        <th>Вы</th>
        <th><%= ((Duelist) request.getAttribute("user")).name() %></th>
        <th>Противник</th>
        <th><%= ((Duelist) request.getAttribute("opponent")).name() %></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Урон:</td>
        <td><%= ((Duelist) request.getAttribute("user")).damage() %></td>
        <td>Урон:</td>
        <td><%= ((Duelist) request.getAttribute("opponent")).damage() %></td>
    </tr>
    <tr>
        <td>Жизни:</td>
        <td><%= ((Duelist) request.getAttribute("user")).health() %></td>
        <td>Жизни:</td>
        <td><%= ((Duelist) request.getAttribute("opponent")).health() %></td>
    </tr>
    <tr>
        <td>
            <form action="${requestScope.contextPath}/arena/duel" method="get">
                <input type="submit" value="Обновить" align="center">
            </form>
        </td>
        <c:if test="${requestScope.canAttack}" >
            <td>
                <form action="${requestScope.contextPath}/arena/duel" method="post">
                    <input type="submit" value="Атаковать" align="center">
                </form>
            </td>
        </c:if>
    </tr>
    </tbody>
</table>
<c:forEach var="log" items="${requestScope.logs}" >
    <p align="center" ><c:out value="${log}" /></p>
</c:forEach>
<p align="center"><jsp:include page="/techInfo" /></p>
</body>
</html>
