<%@ page contentType="text/html;charset=UTF-8" %>

<%-- validate that the user is logged in--%>
<%@ include file="scripts/check_login.jsp" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Game</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file="parts/javascript.jsp" %>

    <script>
        const websocketID = `<c:out value="${pageContext.session.id}" />`
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/websocket.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chessboard-1.0.0.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/lobby.css">

    <script src="${pageContext.request.contextPath}/assets/js/chessboard-1.0.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/game.js"></script>
</head>
<body>

<c:set value="Game" var="pageTitle"/>
<%@include file="parts/nav.jsp" %>
<div class="container-fluid mt-2 pt-5">
    <div class="row mx-auto" style="width: 800px;">
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
        <div class="col" style="max-width: 540px;">
            <div class="card">
                <div class="card-header">
                    <div>
                        <h4 class="float-left">Chess</h4>
                        <div class="btn-group float-right">
                            <a href="javascript:backToLobby()" class="btn btn-outline-warning ">Back to Lobby</a>
                            <a href="javascript:leaveLobby()" class="btn btn-outline-danger">Leave</a>
                        </div>
                    </div>
                </div>
                <div class="card-body pb-0" id="settings">
                    <div class="row">
                        <div id="board1" style="width: 500px"></div>
                    </div>
                    <div class="row">
                        <div class="card w-100" id="logs">
                            <div class="card-header" data-toggle="collapse" role="button" aria-expanded="false"
                                 aria-controls="collapseExample"
                                 onclick="$('#collapseExample').collapse('toggle');$(this).find('.fas').toggleClass('rotate-180')">
                                <h4 class="float-left">Logs</h4>
                                <span class="ml-2 align-middle" id="lastLog">(no move s)</span>
                                <i class="fas fa-angle-double-down fa-2x float-right"></i>
                            </div>
                            <div class="collapse" id="collapseExample">
                                <div class="card-body" style="overflow-y: scroll;">
                                    <table class="w-100">

                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
