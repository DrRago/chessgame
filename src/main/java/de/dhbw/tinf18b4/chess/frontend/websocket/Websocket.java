package de.dhbw.tinf18b4.chess.frontend.websocket;


import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Move;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyStatus;
import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler;
import de.dhbw.tinf18b4.chess.frontend.SessionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.buildAnswerTemplate;
import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.parseMessage;
import static javax.websocket.CloseReason.CloseCodes.CANNOT_ACCEPT;

/**
 * The websocket endpoint for the client to play the game
 * <p>
 * This class handles everything around managing the lobby or the game for any player
 */
@ServerEndpoint("/websocketendpoint/{lobbyID}/{sessionID}")
public class Websocket extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Websocket.class.getName());

    /**
     * mapper from websocket session to http session ID
     */
    @NotNull
    private static final Map<Session, String> sessionToSessionID = new HashMap<>();
    /**
     * mapper from websocket session to game lobby ID
     */
    @NotNull
    private static final Map<Session, String> sessionToLobbyID = new HashMap<>();
    /**
     * mapper from websocket session to game lobby
     */
    @NotNull
    private static final Map<Session, Lobby> sessionToLobby = new HashMap<>();
    /**
     * mapper from websocket session to http session ID
     */
    @NotNull
    private static final Map<Session, User> sessionToUser = new HashMap<>();

    /**
     * The onOpen method of a websocket
     * <p>
     * it handles the first steps to validate a client. The client has to send the lobbyID and sessionID
     * of the HTTP-Session as url parameter
     *
     * @param lobbyID   the lobbyID
     * @param sessionID the http sessionID
     * @param session   the websocket session
     * @throws IOException on session send error
     */
    @OnOpen
    public void onOpen(@PathParam("lobbyID") String lobbyID, @PathParam("sessionID") String sessionID, @NotNull Session session) throws IOException {
        logger.info(String.format("Incoming socket connection from %s (%s)...", sessionID, lobbyID));

        Lobby playerLobby;
        if ((playerLobby = verifyRequest(session, sessionID, lobbyID)) == null) {
            // if verifyRequest is null, the clients connection gets closed, so we can just return at this point
            return;
        }

        // cannot be null because the lobby must have the user,
        // otherwise the verifyRequest function would have returned null
        // and we wouldn't even reach this point of code
        Player currentPlayer = Arrays.stream(playerLobby.getPlayers())
                .filter(Objects::nonNull)
                .filter(player -> player.getUser().getID().equals(sessionID))
                .findFirst().orElseThrow();

        sessionToSessionID.put(session, sessionID);
        sessionToLobby.put(session, playerLobby);
        sessionToLobbyID.put(session, lobbyID);
        sessionToUser.put(session, currentPlayer.getUser());

        // send new player list to to complete lobby
        sendToLobby(playerLobby, getPlayerNames(playerLobby));

        if (playerLobby.getStatus().equals(LobbyStatus.GAME_STARTED)) {
            // send color to session
            sendToLobby(playerLobby, "connect", currentPlayer.getUser().getDisplayName());
            sendToSession(session, "initGame", currentPlayer.isWhite() ? "white" : "black");
            sendToSession(session, getCompleteLogHistory(playerLobby.getGame()));
            sendToLobby(playerLobby, getMoveResponse(playerLobby));
        }
    }

    /**
     * The onClose websocket method
     * <p>
     * removes the session from all maps to avoid sending data to a closed websocket
     *
     * @param session the closing session
     */
    @OnClose
    public void onClose(@NotNull Session session) throws IOException {
        logger.info(String.format("Closed connection for %s (%s)", sessionToSessionID.get(session), sessionToLobbyID.get(session)));

        // remove identifiers
        sessionToSessionID.remove(session);
        Lobby lobby = sessionToLobby.remove(session);
        sessionToLobbyID.remove(session);
        User user = sessionToUser.remove(session);

        if (lobby != null && lobby.getStatus() == LobbyStatus.GAME_STARTED) {
            // send other clients the disconnect message
            sendToLobby(lobby, "disconnect", user.getDisplayName());
        }
        // leave the lobby if the game hasn't started yet
        if (lobby != null && lobby.getStatus() != LobbyStatus.GAME_STARTED) {
            lobby.leave(user);
            sendToLobby(lobby, getPlayerNames(lobby));
        }
    }

    /**
     * The onError websocket method
     *
     * @param e the error that occurred
     */
    @OnError
    public void onError(@NotNull Throwable e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
    }

    /**
     * The onMessage websocket method
     * <p>
     * it handles everything from determining the request type of the user to control the game
     *
     * @param session the users session
     * @param message the message the user sent (json string)
     * @throws ParseException on invalid json format
     * @throws IOException    on send error to client
     */
    @OnMessage
    public void onMessage(@NotNull Session session, @NotNull String message) throws ParseException, IOException {
        logger.info(String.format("Got message from client %s (%s):", sessionToSessionID.get(session), sessionToLobbyID.get(session)));
        logger.info(message);
        JSONObject parsedMessage = parseMessage(message);

        // parse required information
        String contentType = (String) parsedMessage.get("content");

        Lobby playerLobby = sessionToLobby.get(session);
        String lobbyID = sessionToLobbyID.get(session);
        User currentUser = sessionToUser.get(session);


        switch (contentType) {
            case "move":
                String moveString = (String) parsedMessage.get("value");
                // received move should be in a valid format
                if (!Board.checkMoveFormat(moveString)) {
                    sendErrorMessageToClient("Invalid move format", session, "error");
                    return;
                }
                try {
                    Player currentPlayer = playerLobby.getPlayerByUser(currentUser);
                    if (currentPlayer == null) {
                        sendErrorMessageToClient("Not in lobby anymore", session, "fatal");
                        return;
                    }

                    final Game game = playerLobby.getGame();
                    if (game == null) return;
                    Move move = game.getBoard().buildMove(moveString, currentPlayer);

                    if (!game.makeMove(move)) {
                        logger.info("Invalid move");
                    } else {
                        // cant be null due to successful make move result
                        sendToLobby(playerLobby, "logs", moveToJSON(move).toJSONString());
                    }
                    sendToLobby(playerLobby, getMoveResponse(playerLobby));
                } catch (IllegalArgumentException e) {
                    sendErrorMessageToClient(e.getMessage(), session, "error");
                }

                break;
            case "lobbyAction":
                switch ((String) parsedMessage.get("value")) {
                    case "startGame":
                        switch (playerLobby.startGame()) {
                            case GAME_STARTED:
                                sendToLobby(playerLobby, "redirect", String.format("/lobby/%s/game", lobbyID));
                                break;
                            case NOT_ENOUGH_PLAYERS:
                                sendToSession(session, "error", "Not enough players");
                                break;
                        }
                        break;
                    case "leave":
                        // only remove session from the lobby map to avoid errors in the next steps
                        // the rest will be removed on connection close
                        sessionToLobby.remove(session);

                        if (playerLobby.leave(currentUser)) {
                            sendToLobby(playerLobby, "redirect", "/lobby/" + lobbyID);
                        } else {
                            // send new player list to to complete lobby
                            sendToLobby(playerLobby, getPlayerNames(playerLobby));
                        }

                        sendToSession(session, "redirect", "/");
                        // lobby empty check
                        if (Arrays.stream(playerLobby.getPlayers()).allMatch(Objects::isNull)) {
                            LobbyManager.removeLobby(playerLobby);
                        }
                        break;
                }
                break;
            case "lobbyPrivacy":
                playerLobby.setPublicLobby(!(boolean) parsedMessage.get("value"));
                break;
            default:
                sendErrorMessageToClient("Operation " + contentType + " not found", session, "error");
                break;
        }
    }

    private JSONObject moveToJSON(@NotNull Move move) {
        JSONObject obj = new JSONObject();
        obj.put("player", move.getPlayer().isWhite() ? "white" : "black");
        obj.put("entry", move.toString());

        return obj;
    }

    private JSONObject getCompleteLogHistory(@NotNull Game game) {
        JSONObject logHistory = buildAnswerTemplate();
        JSONArray logArray = new JSONArray();
        logArray.addAll(game.getHistory().stream()
                .map(this::moveToJSON)
                .collect(Collectors.toList()));

        logHistory.put("content", "logs");
        logHistory.put("value", logArray);

        return logHistory;
    }

    /**
     * Build the response for the clients in order to what moves are possible and whose turn it is
     *
     * @param lobby the lobby to build the answer for
     * @return the json response object
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private JSONObject getMoveResponse(@NotNull Lobby lobby) {
        JSONObject moveAnswer = buildAnswerTemplate();

        JSONObject answerValue = new JSONObject();
        Game game = lobby.getGame();
        if (game == null) return moveAnswer;

        answerValue.put("fen", game.asFen()); // the fen string of the board
        answerValue.put("turn", game.whoseTurn().isWhite() ? "white" : "black"); // the color whose turn it is

        // get all possible moves for all pieces identified by it's position on the board
        List<Map<Piece, Stream<Position>>> allMoves = lobby.getGame().getBoard().getAllPossibleMoves();
        Map<Piece, Stream<Position>> moveMap = allMoves.get(0);
        Map<Piece, Stream<Position>> captureMoveMap = allMoves.get(1);


        JSONArray possibilitiesArray = new JSONArray();

        moveMap.forEach((key, value) -> {
            JSONObject positionObject = new JSONObject();
            JSONArray positionArray = new JSONArray();
            positionArray.addAll(value.map(Position::toString).collect(Collectors.toList()));

            JSONArray captureArray = new JSONArray();
            captureArray.addAll(captureMoveMap.get(key).map(Position::toString).collect(Collectors.toList()));

            positionObject.put("piece", key.getPosition().toString());
            positionObject.put("color", key.isWhite() ? "white" : "black");

            positionObject.put("possibilities", positionArray);
            positionObject.put("capturePossibilities", captureArray);

            possibilitiesArray.add(positionObject);
        });

        answerValue.put("possibilities", possibilitiesArray); // the possible moves
        moveAnswer.put("content", "move");
        moveAnswer.put("value", answerValue);

        return moveAnswer;
    }

    /**
     * Get the json answer for the player name list of a lobby
     *
     * @param lobby the lobby to build the answer for
     * @return the json answer object
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private JSONObject getPlayerNames(@NotNull Lobby lobby) {
        JSONArray nameArray = new JSONArray();

        Arrays.stream(lobby.getPlayers())
                .filter(Objects::nonNull)
                .forEach(player -> {
                    JSONObject name = new JSONObject();
                    name.put("color", player.isWhite() ? "white" : "black");
                    name.put("name", player.getUser().getDisplayName());
                    nameArray.add(name);
                });

        JSONObject nameAnswer = buildAnswerTemplate();
        nameAnswer.put("content", "updatePlayerNames");
        nameAnswer.put("value", nameArray);

        return nameAnswer;
    }

    /**
     * Send a string message to all participants of a lobby
     *
     * @param lobby   the lobby to send the message to
     * @param content the content field of the json message
     * @param message the value field of the json message
     * @throws IOException on send error to client
     */
    @SuppressWarnings("unchecked")
    private void sendToLobby(@NotNull Lobby lobby, @NotNull String content, @NotNull String message) throws IOException {
        // build the JSON object
        JSONObject answer = buildAnswerTemplate();
        answer.put("content", content);
        answer.put("value", message);

        // send all
        sendToLobby(lobby, answer);
    }

    /**
     * Send a json object to all participants of a lobby
     *
     * @param lobby      the lobby to send the object to
     * @param jsonObject the json object to send
     * @throws IOException on send error to client
     */
    private void sendToLobby(@NotNull Lobby lobby, @NotNull JSONObject jsonObject) throws IOException {
        // collect all sessions that are in the current lobby
        List<Session> lobbySessions = sessionToLobby.entrySet().stream()
                .filter(entry -> entry.getValue() == lobby).map(Map.Entry::getKey).collect(Collectors.toList());

        // send the message to all sessions
        for (Session session : lobbySessions) {
            sendToSession(session, jsonObject);
        }
    }

    /**
     * Send a string message to a session
     *
     * @param session the session to send the message to
     * @param content the content field of the json object
     * @param message the value field of the json object
     * @throws IOException on send error to client
     */
    @SuppressWarnings("unchecked")
    private void sendToSession(@NotNull Session session, @NotNull String content, @NotNull String message) throws IOException {
        JSONObject answer = buildAnswerTemplate();
        answer.put("content", content);
        answer.put("value", message);

        sendToSession(session, answer);
    }

    /**
     * Send a json object to a session
     *
     * @param session    the session to send the object to
     * @param jsonObject the json object to send
     */
    private void sendToSession(@NotNull Session session, @NotNull JSONObject jsonObject) {
        if (!session.isOpen()) return;
        logger.info(String.format("sending to client %s (%s)", sessionToSessionID.get(session), sessionToLobbyID.get(session)));
        session.getAsyncRemote().sendText(jsonObject.toJSONString());
    }

    /**
     * Send an error message to the client. If the type is fatal, the session gets closed
     *
     * @param message the message to send
     * @param session the session to send the error to
     * @param type    the type of the error
     * @throws IOException on send error to client
     */
    @SuppressWarnings("unchecked")
    private void sendErrorMessageToClient(@NotNull String message, @NotNull Session session, @NotNull String type) throws IOException {
        System.out.println(message);
        JSONObject answer = JSONHandler.buildAnswerTemplate();
        answer.put("content", type);
        answer.put("value", message);
        sendToSession(session, answer);

        // close connection on fatal error
        if (type.equals("fatal")) {
            session.close(new CloseReason(CANNOT_ACCEPT, message));
        }
    }

    /**
     * Check if a request is verified: <br>
     * The passed sessionID must be part of a valid {@link Session}<br>
     * The {@link Lobby} the {@link User} tries to operate on has to exist<br>
     * The {@link User} must be part of the {@link Lobby} he tries to operate on<br>
     *
     * @param session   the websocket {@link Session}
     * @param sessionID the HTTPServlet session ID of the user
     * @param lobbyID   the lobby ID the user wants to operate on
     * @return the {@link Lobby} of the request
     * @throws IOException on send error to client
     */
    private @Nullable Lobby verifyRequest(@NotNull Session session, @Nullable String sessionID, @Nullable String lobbyID) throws IOException {
        if (sessionID == null || lobbyID == null) {
            sendErrorMessageToClient("Unallowed call to server", session, "fatal");
            return null;
        }

        HttpSession userSession = SessionManager.findSessionByID(sessionID);
        if (userSession == null) {
            sendErrorMessageToClient("Session invalid", session, "fatal");
            return null;
        }
        logger.info("Session valid");
        User user = (User) userSession.getAttribute("user");
        // user cannot be null, because this entry is set on login so:
        // if the session exists, the user entry must be set

        // validate that the user is in the lobby
        // this may not be necessary for every request, but it costs nearly no resources
        // and the player may be kicked from a lobby in further versions
        Lobby playerLobby = LobbyManager.getLobbies().get(lobbyID);
        if (playerLobby == null) {
            sendErrorMessageToClient("Not a lobby", session, "fatal");
            return null;
        }
        logger.info("Lobby exists");

        if (!playerLobby.hasUser(user)) {
            sendErrorMessageToClient("Not member of lobby", session, "fatal");
            return null;
        }
        logger.info("Request verified - User is eligible to operate in this lobby");

        return playerLobby;
    }
}