package de.dhbw.tinf18b4.chess.frontend.websocket;


import de.dhbw.tinf18b4.chess.backend.Move;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.backend.utility.MoveUtility;
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
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.buildAnswerTemplate;
import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.parseMessage;
import static javax.websocket.CloseReason.CloseCodes.CANNOT_ACCEPT;

/**
 * @author Leonhard Gahr
 */
@ServerEndpoint("/websocketendpoint")
public class Websocket extends HttpServlet {
    private static Map<Lobby, List<Session>> sessionLobbies = new HashMap<>();
    private static Map<Session, String> sessionToSessionID = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Websocket.class.getName());


    @OnOpen
    public void onOpen(Session session) {
        logger.info("Incoming socket connection.. ");
        // TODO: 07/07/2019 add session to equivalent lobby
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws ParseException, IOException {
        logger.info("Got message from client:");
        logger.info(message);
        JSONObject parsedMessage = parseMessage(message);

        // parse required information
        String contentType = (String) parsedMessage.get("content");
        String sessionID = (String) parsedMessage.get("ID");
        String lobbyID = (String) parsedMessage.get("lobbyID");

        Lobby playerLobby;

        // verify user integrity
        if ((playerLobby = verifyRequest(session, sessionID, lobbyID, contentType)) == null) {
            return;
        }
        logger.info("Request acknowledged");

        // cannot be null because the lobby must have the user,
        // otherwise the verifyRequest function would have returned null
        // and we wouldn't even reach this point of code

        // note: the users equals method allows comparision with a string
        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        User currentUser = Arrays.stream(playerLobby.getPlayers())
                .filter(player -> player.getUser().equals(sessionID))
                .map(Player::getUser).findFirst().orElseThrow();

        switch (contentType) {
            case "ACK":
                // clients first connection on page load
                addSessionToLobbyList(session, playerLobby);
                sessionToSessionID.put(session, sessionID);

                // send acknowledge
                JSONObject acknowledge = buildAnswerTemplate("ACK", "OK");
                sendToSession(session, acknowledge);

                // send new playerlist to to complete lobby
                JSONObject nameObject = getPlayerNames(playerLobby);
                sendToLobby(playerLobby, nameObject);
                break;
            case "move":
                // TODO: 04/07/2019 perform move operations
                String moveString = (String) parsedMessage.get("value");
                // received move should be in a valid format
                if (!MoveUtility.checkMoveFormat(moveString)) {
                    sendErrorMessageToClient("Invalid move format", session, "error");
                    return;
                }
                Move move = MoveUtility.buildMove(moveString, playerLobby.getGame().getBoard(), playerLobby.getPlayerByUser(currentUser));

                break;
            case "getPlayerNames":
                // build json answer with player names
                JSONObject nameAnswer = getPlayerNames(playerLobby);

                sendToSession(session, nameAnswer);
                break;

            case "lobbyAction":
                switch ((String) parsedMessage.get("value")) {
                    case "startGame":
                        switch (playerLobby.startGame()) {
                            case GAME_STARTED:
                                sendToLobby(playerLobby, "redirect", "/game");
                                break;
                            case NOT_ENOUGH_PLAYERS:
                                sendToSession(session, "error", "Not enough players");
                                break;
                        }
                        break;
                    case "leave":
                        break;
                }
                break;
            default:
                sendErrorMessageToClient("Operation " + contentType + " not found", session, "error");
                break;
        }
    }

    private @NotNull JSONObject getPlayerNames(@NotNull Lobby lobby) {
        List<String> names = Arrays.stream(lobby.getPlayers())
                .filter(Objects::nonNull)
                .map(player -> player.getUser().getDisplayName())
                .collect(Collectors.toList());

        JSONArray nameArray = new JSONArray();
        nameArray.addAll(names);

        JSONObject nameAnswer = buildAnswerTemplate();
        nameAnswer.put("content", "updatePlayerNames");
        nameAnswer.put("value", nameArray);

        return nameAnswer;
    }

    private void removeSessionFromLobbyList(Session session) {
        sessionLobbies.forEach((key, value) -> value.removeIf(lobbySession -> lobbySession.equals(session)));
        // filter empty lobbies from the list
        sessionLobbies.entrySet().removeIf(entry -> entry.getValue().size() == 0);
    }

    private void addSessionToLobbyList(Session session, Lobby lobby) {
        if (sessionLobbies.get(lobby) == null) {
            List<Session> sessionList = new ArrayList<>();
            sessionList.add(session);

            sessionLobbies.put(lobby, sessionList);
        } else {
            sessionLobbies.get(lobby).add(session);
        }
    }

    private @Nullable Lobby getLobbyForSession(Session session) {
        return sessionLobbies.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(lobbySession -> lobbySession.equals(session)))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public void sendToLobby(@NotNull Lobby lobby, @NotNull String content, @NotNull String message) throws IOException {
        JSONObject answer = buildAnswerTemplate();
        answer.put("content", content);
        answer.put("value", message);

        for (Session session : sessionLobbies.get(lobby)) {
            session.getBasicRemote().sendText(answer.toJSONString());
        }
    }

    public void sendToLobby(@NotNull Lobby lobby, @NotNull JSONObject jsonObject) throws IOException {
        for (Session session : sessionLobbies.get(lobby)) {
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
     * Check if a request is verified: <br />
     * The passed sessionID must be part of a valid {@link Session}<br />
     * The {@link Lobby} the {@link User} tries to operate on has to exist<br />
     * The {@link User} must be part of the {@link Lobby} he tries to operate on<br />
     *
     * @param session     the websocket {@link Session}
     * @param sessionID   the HTTPServlet session ID of the user
     * @param lobbyID     the lobby ID the user wants to operate on
     * @param contentType the operation the user wants to make
     * @return the {@link Lobby} of the request
     * @throws IOException inherited from sendErrorMessageToClient()
     */
    private Lobby verifyRequest(@NotNull Session session, @Nullable String sessionID, @Nullable String lobbyID, @Nullable String contentType) throws IOException {
        if (contentType == null || sessionID == null || lobbyID == null) {
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
            // remove session from lobby list just in case it may be present
            removeSessionFromLobbyList(session);
            sendErrorMessageToClient("Not a lobby", session, "fatal");
            return null;
        }
        logger.info("Lobby exists");

        if (!playerLobby.hasUser(user)) {
            // remove session from lobby list in case the user got kicked from the lobby
            removeSessionFromLobbyList(session);
            sendErrorMessageToClient("Not member of lobby", session, "fatal");
            return null;
        }
        logger.info("Request verified - User is eligible to operate in this lobby");

        return playerLobby;
    }
}