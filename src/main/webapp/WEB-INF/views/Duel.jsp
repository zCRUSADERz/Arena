<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.page contentType="text/html; charset=UTF-8" />
<jsp:directive.page pageEncoding="UTF-8" />
<html>
<head>
    <title>Дуэль</title>
    <%--@elvariable id="attr" type="java.util.Map<String, String>"--%>
    <c:choose>
        <c:when test="${not empty attr.startTimer}" >
            <meta http-equiv="refresh" content="${attr.startTimer}">
        </c:when>
        <c:when test="${not empty attr.turnTimer}" >
            <meta http-equiv="refresh" content="${attr.turnTimer}">
        </c:when>
    </c:choose>
    <style type="text/css">
        form { text-align: center}
        progress {
            background: green;
        }
    </style>
</head>
<body>
<h4 align="center">Дуэль</h4>
<c:if test="${not empty attr.startTimer}">
    <p align="center">До начала боя осталось ${attr.startTimer} секунд.</p>
    <p align="center">Страница обновится автоматически, по истечению таймера.</p>
</c:if>
<table align="center">
    <thead>
    <tr>
        <th>Вы</th>
        <th>${attr.your_name}</th>
        <th>Противник</th>
        <th>${attr.name}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Урон:</td>
        <td>${attr.your_damage}</td>
        <td>Урон:</td>
        <td>${attr.damage}</td>
    </tr>
    <tr>
        <td>Жизни:</td>
        <td>
            <progress value="${attr.your_health}" max="${attr.your_start_health}"></progress>
        </td>
        <td>Жизни:</td>
        <td><progress value="${attr.health}" max="${attr.start_health}"></progress></td>
    </tr>
    <c:if test="${empty attr.finished}" >
        <tr>
            <td>
                <form action="${requestScope.contextPath}/arena/duel" method="get">
                    <input type="submit" value="Обновить" align="center">
                </form>
            </td>
            <c:if test="${empty attr.startTimer && empty attr.turnTimer}" >
                <td>
                    <form action="${requestScope.contextPath}/arena/duel" method="post">
                        <input type="submit" value="Атаковать" align="center">
                    </form>
                </td>
            </c:if>
        </tr>
    </c:if>
    </tbody>
</table>
<%--@elvariable id="logs" type="java.util.List<String>"--%>
<c:forEach var="log" items="${logs}" >
    <p align="center" ><c:out value="${log}" /></p>
</c:forEach>
<p align="center"><jsp:include page="/techInfo" /></p>
</body>
</html>
