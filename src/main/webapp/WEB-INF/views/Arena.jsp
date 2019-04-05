<jsp:directive.page contentType="text/html; charset=UTF-8" />
<jsp:directive.page pageEncoding="UTF-8" />
<html>
<head>
    <title>Arena</title>
</head>
<body>
<p align="center"><a href="${pageContext.request.contextPath}/arena/duels">Дуэли</a></p>
<p align="center"><a href="${pageContext.request.contextPath}/logOut">Выйти</a></p>
<p align="center"><jsp:include page="/techInfo" /></p>
</body>
</html>