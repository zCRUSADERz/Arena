<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Duels</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/arena/duels" method="post">
    <input type="submit" value="Начать дуэль">
</form>
<p align="center" ><jsp:include page="/techInfo" /></p>
</body>
</html>
