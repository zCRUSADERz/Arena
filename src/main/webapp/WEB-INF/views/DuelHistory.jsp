<%@ page import="ru.job4j.domain.duels.duelists.DuelistInfo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:directive.page contentType="text/html; charset=UTF-8" />
<jsp:directive.page pageEncoding="UTF-8" />
<html>
<head>
    <title>Дуэль</title>
    <style type="text/css">
        form { text-align: center}
    </style>
</head>
<body>
<h4 align="center">Дуэль</h4>
<table align="center">
    <thead>
    <tr>
        <th>Вы</th>
        <th><%= ((DuelistInfo) request.getAttribute("user")).name() %></th>
        <th>Противник</th>
        <th><%= ((DuelistInfo) request.getAttribute("opponent")).name() %></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>Урон:</td>
        <td><%= ((DuelistInfo) request.getAttribute("user")).damage() %></td>
        <td>Урон:</td>
        <td><%= ((DuelistInfo) request.getAttribute("opponent")).damage() %></td>
    </tr>
    <tr>
        <td>Жизни:</td>
        <td><%= ((DuelistInfo) request.getAttribute("user")).health() %></td>
        <td>Жизни:</td>
        <td><%= ((DuelistInfo) request.getAttribute("opponent")).health() %></td>
    </tr>
    </tbody>
</table>
<c:forEach var="log" items="${requestScope.logs}" >
    <p align="center" ><c:out value="${log}" /></p>
</c:forEach>
<p align="center"><jsp:include page="/techInfo" /></p>
</body>
</html>
