<%--@elvariable id="queryTimer" type="java.lang.Long"--%>
<%--@elvariable id="queryCounter" type="java.lang.Integer"--%>
<%--@elvariable id="requestTimer" type="java.lang.Long"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
page:${requestTimer} ms, db: ${queryCounter}req (${queryTimer}ms)
