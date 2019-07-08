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
    <script src="${pageContext.request.contextPath}/assets/js/websocket.js" async></script>
    <script src="${pageContext.request.contextPath}/assets/js/lobby-and-game.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chessboard-1.0.0.min.css">
    <script src="${pageContext.request.contextPath}/assets/js/chessboard-1.0.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/game.js"></script>
</head>
<body>
<div class="container mt-5 pt-5">
    <h2 class="text-center">Game</h2>
    <div class="row">
        <div class="col">
            <a href="javascript:leaveLobby()" class="btn btn-outline-danger float-right">Leave</a>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col">
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
                <div class="card-header"><h4>Chess</h4></div>
                <div class="card-body" id="settings">
                    <div class="row">
                        <div class="col">
                            <div id="board1" style="width: 500px"></div>
                        </div>
                        <div class="col">
                            <div class="card" style="height: 500px !important;">
                                <div class="card-header">
                                    <h4>Logs</h4>
                                </div>
                                <div class="card-body" style="overflow-y: scroll;" id="chat">
                                    <table>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>

                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
                                        <tr>
                                            <td>Data</td>
                                        </tr>
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
