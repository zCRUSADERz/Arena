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
<h4 align="center">Дуэль</h4>
<c:if test="${not empty requestScope.timer}">
    <p align="center">До начала боя осталось ${requestScope.timer} секунд.</p>
    <p align="center">Страница обновится автоматически, по истечению таймера.</p>
</c:if>
<table align="center">
    <thead>
    <tr>
        <th>Вы</th>
        <th>${requestScope.yourName}</th>
        <th>Противник</th>
        <th>${requestScope.opponentName}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Урон:</td>
        <td>${requestScope.yourDamage}</td>
        <td>Урон:</td>
        <td>${requestScope.opponentDamage}</td>
    </tr>
    <tr>
        <td>Жизни:</td>
        <td>${requestScope.yourHealth}</td>
        <td>Жизни:</td>
        <td>${requestScope.opponentHealth}</td>
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
