<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- validate that the user is logged in--%>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Lobbies</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>

</head>
<body>
<jsp:useBean id="lobbies" class="de.dhbw.tinf18b4.chess.frontend.beans.LobbyHelper" scope="request"/>

<%@include file="parts/nav.jsp" %>
<div class="container mt-5 pt-5">
    <%@include file="parts/errors.jsp" %>
    <table class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th>ID</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${lobbies.lobbyNames}" var="lobby">
            <tr>
                <td><a href='/lobby/<c:out value="${lobby}"/>'><c:out value="${lobby}"/></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/lobby/create" class="btn btn-outline-success">Create Lobby</a>
</div>
</body>
</html>
