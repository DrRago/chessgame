package de.dhbw.tinf18b4.chess.frontend.websocket;


import de.dhbw.tinf18b4.chess.backend.Board;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.buildAnswerTemplate;
import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.parseMessage;
import static javax.websocket.CloseReason.CloseCodes.CANNOT_ACCEPT;

/**
 * @author Leonhard Gahr
 */
@ServerEndpoint("/websocketendpoint/{lobbyID}/{sessionID}")
public class Websocket extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Websocket.class.getName());

    /**
     * mapper from websocket session to http session ID
     */
    private static Map<Session, String> sessionToSessionID = new HashMap<>();
    /**
     * mapper from websocket session to game lobby ID
     */
    private static Map<Session, String> sessionToLobbyID = new HashMap<>();
    /**
     * mapper from websocket session to game lobby
     */
    private static Map<Session, Lobby> sessionToLobby = new HashMap<>();
    /**
     * mapper from websocket session to http session ID
     */
    private static Map<Session, User> sessionToUser = new HashMap<>();


    /**
     * The onOpen method of a websocket
     * it handles the first steps to validate a client. The client has to send the lobbyID and sessionID
     * of the HTTP-Session as url parameter
     *
     * @param lobbyID   the lobbyID
     * @param sessionID the http sessionID
     * @param session   the websocket session
     * @throws IOException on session send error
     */
    @OnOpen
    public void onOpen(@PathParam("lobbyID") String lobbyID, @PathParam("sessionID") String sessionID, Session session) throws IOException {
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

        // send new playerlist to to complete lobby
        sendToLobby(playerLobby, getPlayerNames(playerLobby));

        if (playerLobby.getStatus().equals(LobbyStatus.GAME_STARTED)) {
            // send color to session
            sendToSession(session, "initGame", currentPlayer.isWhite() ? "white" : "black");
            sendToLobby(playerLobby, getMoveResponse(playerLobby));
        }
    }

    /**
     * The onClose websocket method
     * removes the session from all maps to avoid sending data to a closed websocket
     *
     * @param session the closing session
     */
    @OnClose
    public void onClose(Session session) {
        logger.info(String.format("Close connection for %s (%s)", sessionToSessionID.get(session), sessionToLobbyID.get(session)));

        // remove identifiers
        sessionToSessionID.remove(session);
        sessionToLobby.remove(session);
        sessionToLobbyID.remove(session);
        sessionToUser.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws ParseException, IOException {
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
                    Move move = playerLobby.getGame().getBoard().buildMove(moveString, playerLobby.getPlayerByUser(currentUser));

                    if (!playerLobby.getGame().makeMove(move)) {
                        logger.info("Invalid move");
                    }
                    sendToLobby(playerLobby, getMoveResponse(playerLobby));
                    sendToLobby(playerLobby, "logs", playerLobby.getGame().getHistory().lastMove().toString()); // pass color of log entry
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
                            // send new playerlist to to complete lobby
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
            default:
                sendErrorMessageToClient("Operation " + contentType + " not found", session, "error");
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private @NotNull JSONObject getMoveResponse(@NotNull Lobby lobby) {
        JSONObject moveAnswer = buildAnswerTemplate();

        JSONObject answerValue = new JSONObject();
        answerValue.put("fen", lobby.getGame().asFen()); // the fen string of the board
        answerValue.put("turn", lobby.getGame().whoseTurn().isWhite() ? "white" : "black"); // the color whose turn it is

        // get all possible moves for all pieces identified by it's position on the board
        Map<Piece, Stream<Position>> moveMap = new HashMap<>();
        Map<Piece, Stream<Position>> captureMoveMap = new HashMap<>();
        lobby.getGame().getBoard().getPieces().forEach(piece -> {
                    captureMoveMap.put(piece, piece.getValidCaptureMoves(lobby.getGame().getBoard()));
                    moveMap.put(piece, piece.getValidMoves(lobby.getGame().getBoard()));
                }
        );
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

    private @NotNull JSONObject getPlayerNames(@NotNull Lobby lobby) {
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

    @OnError
    public void onError(Session session, Throwable e) throws IOException {
        e.printStackTrace();
        sendErrorMessageToClient(e.getMessage(), session, "error");
    }

    public void sendToLobby(@NotNull Lobby lobby, @NotNull String content, @NotNull String message) throws IOException {
        // build the JSON object
        JSONObject answer = buildAnswerTemplate();
        answer.put("content", content);
        answer.put("value", message);

        // send all
        sendToLobby(lobby, answer);
    }

    public void sendToLobby(@NotNull Lobby lobby, @NotNull JSONObject jsonObject) throws IOException {
        // collect all sessions that are in the current lobby
        List<Session> lobbySessions = sessionToLobby.entrySet().stream()
                .filter(entry -> entry.getValue() == lobby).map(Map.Entry::getKey).collect(Collectors.toList());

        // send the message to all sessions
        for (Session session : lobbySessions) {
            session.getBasicRemote().sendText(jsonObject.toJSONString());
        }
    }

    public void sendToSession(@NotNull Session session, @NotNull String content, @NotNull String message) throws IOException {
        JSONObject answer = buildAnswerTemplate();
        answer.put("content", content);
        answer.put("value", message);

        session.getBasicRemote().sendText(answer.toJSONString());
    }

    public void sendToSession(@NotNull Session session, @NotNull JSONObject jsonObject) throws IOException {
        logger.info("sending to client...");
        session.getBasicRemote().sendText(jsonObject.toJSONString());
    }

    private void sendErrorMessageToClient(@NotNull String message, @NotNull Session session, @NotNull String type) throws IOException {
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
     * @throws IOException inherited from sendErrorMessageToClient()
     */
    private Lobby verifyRequest(@NotNull Session session, @Nullable String sessionID, @Nullable String lobbyID) throws IOException {
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