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
        </div>
    </div>
    <div class="row mt-4">
        <div class="col" id="playerCol">
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
        <div class="col">
            <div class="card">
                <div class="card-header">
                    <div>
                        <h4 class="float-left">Current Settings</h4>
                        <a href="javascript:leaveLobby()" class="btn btn-outline-danger float-right">Leave Lobby</a>
                    </div>
                </div>
                <div class="card-body" id="settings">
                    <table class="table table-striped" id="settingsTable">
                        <tbody>
                        <tr>
                            <td class="align-items-center row">
                                <label class="switch my-auto" id="privacy">
                                    <input type="checkbox">
                                    <span class="slider round"></span>
                                </label>
                                <div class="center ml-3">Private Lobby</div>
                            </td>
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
