<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Lobbies</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dataTable.css">

</head>
<body>
<jsp:useBean id="lobbies" class="de.dhbw.tinf18b4.chess.frontend.beans.LobbyHelper" scope="request"/>

<c:set value="Lobby list" var="pageTitle"/>
<%@include file="parts/nav.jsp" %>
<div class="container mt-2 pt-5">
    <%@include file="parts/errors.jsp" %>
    <a href="${pageContext.request.contextPath}/lobby/create" class="btn btn-outline-success" id="create_lobby">Create Lobby</a>
    <table id="lobbyTable" class="table table-bordered table-striped table-hover">
        <thead>
        <tr>
            <th scope="col" class="col-1">#</th>
            <th scope="col">ID</th>
            <th scope="col">Owner</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${lobbies.lobbyNames}" var="lobby" varStatus="loop">
            <tr>
                <th scope="row" class="col-1"></th>
                <td><a href='/lobby/<c:out value="${lobby.ID}"/>'><c:out value="${lobby.ID}"/></a></td>
                <td>${lobby.lobby.ownerName}<a  href='/lobby/<c:out value="${lobby.ID}"/>' class="btn btn-success btn-sm float-right"><i class="fas fa-sign-in-alt"></i> Join</a> </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script src="${pageContext.request.contextPath}/assets/js/dataTable.js"></script>
<script>
    $("#lobbyTable tbody tr").each((index, row) => {
        $(row).find("th").text(index + 1);
    });

    const table = $("#lobbyTable").DataTable({
        "pageLength": 10,
        "bLengthChange": false,
        "language": {
            "emptyTable": "No public lobby found"
        },
        buttons:[{
            dom:{
                button:{
                    tag:"button",
                    className:"btn btn-secondary"
                },
                buttonLiner: {
                    tag: null
                }
            }
        }]
    });

    $("#create_lobby").appendTo($("#lobbyTable_wrapper > .row:first-child > :first-child"))
</script>
</body>
</html>
