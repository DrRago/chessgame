<%@ page contentType="text/html;charset=UTF-8" %>

<%-- validate that the user is logged in--%>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Lobby <c:out value="${param.id}"/></title>

    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>

    <script>
        const websocketID = `<c:out value="${pageContext.session.id}" />`
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/websocket.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/lobby.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/lobby.css">

</head>
<body>
<div class="container mt-5 pt-5">
    <h2 class="text-center">Lobby</h2>
    <div class="row">
        <div class="col">
            <a href="javascript:leaveLobby()" class="btn btn-outline-danger float-right">Leave</a>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-lg-3 col-sm-5">
            <div class="card">
                <div class="card-header"><h4>Players</h4></div>
                <div class="card-body">
                    <div class="row">
                        <div class="col">
                            <ul class="list-group list-group-flush" id="playerList">
                                <li class="list-group-item">will be removed by javascript</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-9">
            <div class="card">
                <div class="card-header"><h4>Current Settings</h4></div>
                <div class="card-body" id="settings">
                    <table class="table table-striped" id="settingsTable">
                        <tbody>
                        <tr>
                            <td>
                                <label class="switch" id="privacy">
                                    <input type="checkbox">
                                    <span class="slider round"></span>
                                </label>
                                <label class="center align-items-center">Private Lobby</label>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="checkbox" id="checkbox2"> <label for="checkbox2">Checkbox, check it if you
                                want</label></td>
                        </tr>
                        <tr>
                            <td><input type="checkbox" id="checkbox3"> <label for="checkbox3">Checkbox, check it if you
                                want</label></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="card-footer">
                    <div class="row">
                        <div class="col">
                            <a href="javascript:startGame()" class="btn btn-outline-success float-right">Start the
                                game</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
