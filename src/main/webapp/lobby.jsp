<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- validate that the user is logged in--%>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Lobby</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>
</head>
<body>
<div class="container mt-5 pt-5">

    <jsp:useBean id="lobbies" class="de.dhbw.tinf18b4.chess.frontend.beans.LobbyHelper" scope="request"/>
    This is a lobby called <c:out value="${param.id}"/>
    <a href="${pageContext.request.contextPath}/lobby/" class="btn btn-outline-success">Back to overview</a>
    <a href="javascript:location.href=location.href + '/game/'" class="btn btn-outline-success">Play</a>
</div>
</body>
</html>
