<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file="parts/javascript.jsp" %>
    <script>
        const websocketID = `<c:out value="${pageContext.session.id}" />`
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/websocket.js"></script>
</head>
<body class="text-center">
<div class="container">
    <label for="input" class="sr-only">Player Name</label>
    <input type="text" id="input" class="form-control" placeholder="Player name" required autofocus>
    <button class="btn btn-lg btn-primary btn-block" id="btn-send" type="submit">Play</button>
</div>
</body>
</html>
