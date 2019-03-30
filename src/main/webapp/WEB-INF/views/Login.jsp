<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sign in</title>
    <style type="text/css">
        body {font-size:14px;}
        label {float:left; padding-right:10px;}
        .field {clear:both; text-align:right; line-height:25px;}
        .main {float:left;}
    </style>
</head>
<body>
<h1>Sign in</h1>

<form action="${pageContext.request.contextPath}/" method="post">
    <div class="main">
        <div class="field">
            <label for="name">User name</label>
            <input type="text" id="name" name="name" value="${param.name}" />
        </div>
        <div class="field">
            <c:if test="${not empty requestScope.name}" >
                <c:out value="${requestScope.name}" />
            </c:if>
        </div>
        <div class="field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" value="${param.password}" />
        </div>
        <div class="field">
            <c:if test="${not empty requestScope.password}" >
                <c:out value="${requestScope.password}" />
            </c:if>
        </div>
        <div class="field">
            <input type="submit" value="Sign in">
        </div>
    </div>
</form>
<p align="center" ><jsp:include page="/techInfo" /></p>
</body>
</html>