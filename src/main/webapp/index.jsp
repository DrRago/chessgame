<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- validate that the user is logged in--%>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess</title>

    <%@include file='parts/styles.jsp' %>
    <link rel="stylesheet" href="assets/css/landing.css">
</head>
<body class="text-center">
<form class="form-signin">
    <img class="mb-4" src="https://upload.wikimedia.org/wikipedia/commons/9/98/Font_Awesome_5_solid_chess.svg" alt=""
         width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Play Chess <c:out value="${sessionScope.user.displayName}"/></h1>
    <label for="inputName" class="sr-only">Player Name</label>
    <input type="text" id="inputName" class="form-control" placeholder="Player name" required autofocus>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Play</button>
</form>
</body>
</html>
