<%--
  Created by IntelliJ IDEA.
  User: Cthulhu_Sir
  Date: 02.10.2018
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>VK AUTH</title>
</head>
<body>
<c:if test="${isButton}">
    <c:if test="${isAuth}">
        <c:redirect url="${link}"></c:redirect>
    </c:if>
    <c:if test="${isAuth == false}">
        <button onclick="window.location='${link}'">Авторизоваться</button>
    </c:if>
</c:if>
<c:if test="${!isButton}">
    <p>${userName}</p>
    <p>${friends}</p>
</c:if>
</body>
</html>
