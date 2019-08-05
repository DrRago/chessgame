<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Manual</title>

    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>

</head>
<body>

<c:set var="pageTitle" value="Manual"/>
<%@include file="parts/nav.jsp" %>
<div class="container mt-2 pt-5">
    <div class="text-justify">
        <h3>How to use our application?</h3>
        <h4>First entry</h4>
        <p>
            If you're new here you may want to change your name so that others may see, who you really are (if you
            want).
            However you can do that by clicking the button on the top right corner of the menu bar. You will be prompted
            to enter a new name. <br>
            As long as you don't clear your cookies you will keep that name on this browser. You can change your name as
            often and whenever you want.
        </p>
        <div class="alert alert-info"><a>You may need to reload completely in order to fully apply that change.</a>
        </div>

        <h4>Get into a lobby</h4>
        <p>If you want to start to play chess with someone you need to create a lobby or join an existing one.</p>
        <h5>Create a lobby</h5>
        <p>
            If you decide to create a new lobby you can do that by just clicking the "Create Lobby"-button on the
            <a href="${pageContext.request.contextPath}/lobby">lobby list page</a>. You will join that lobby
            automatically.
        </p>

        <h5>Join a lobby</h5>
        <p>
            In case you want to join a lobby you either need the exact URL of that lobby (e.g.
            https://chess.gahr.dev/lobby/X8-MnCfJPg) or you click "join" to a lobby you can find in the <a
                href="${pageContext.request.contextPath}/lobby">lobby-list</a>. If that list is empty there are no
            public lobbies with a free player-slot, so you either need the exact URL or create a new lobby.
        </p>

        <h4>The lobby</h4>
        <p>
            You are now in a existing lobby. If you are still alone in that lobby you can toggle the switch whether you
            want a private or a public lobby. This option sets the visibility of your lobby in the lobby-list, but with
            the exact URL you can always join.
        </p>
        <h5>Start the game</h5>
        <p>
            If you are in the lobby with another player (you can see this in the player-list on the left side) you are
            able to click "Start the game". By doing that you will be redirected to the game page.
        </p>

        <h4>How to play?</h4>
        <div class="alert alert-info"><b>Note: </b>We wont explain the chess rules here.</div>
        <h5>The game page</h5>
        <p>
            On the game page you can see the player-list on the left side again but the main content is the game itself.
            The color you are playing is always on the bottom. You are now able to play chess according to the normal
            chess rules except you can not claim a draw. The end of the game will only be determined by the server.
        </p>
        <p>
            You can toggle the "Autoplay" switch. You won't be able to control your figures now anymore, you will
            instantly make a <b>random(!)</b> move, but if any of your figures can capture an enemy this action has a
            66% chance of happening. These random moves aren't following any logic, you will most likely loose if you do
            that.
        </p>
        <p>
            You are also able to go back to the lobby. Note that this will exit the game and the game can not be
            recreated.
            If you leave the lobby the game will exit too and the other player will be returned to the lobby view.
        </p>
        <p>
            However closing your browser during a game will not kick you out of the game. You are welcome to come back
            at any time and continue playing. But keep in mind that a random opponent might not like that.<br>
            You can see whether your opponent is connected in the player-list. If the name is gray he is disconnected.
        </p>
        <p>If you were absent a long time you can see a complete log of all moves on the bottom of the page</p>
    </div>
</div>
</body>
</html>