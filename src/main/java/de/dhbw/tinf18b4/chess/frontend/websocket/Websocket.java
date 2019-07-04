package de.dhbw.tinf18b4.chess.frontend.websocket;


import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.backend.utility.MoveUtility;
import de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler;
import de.dhbw.tinf18b4.chess.frontend.SessionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.buildAnswerTemplate;
import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.parseMessage;

/**
 * @author Leonhard Gahr
 */
@ServerEndpoint("/websocketendpoint")
public class Websocket extends HttpServlet {
    private static List<Session> sessionList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Websocket.class.getName());


    @OnOpen
    public void onOpen(Session session) {
        logger.info("Incoming socket connection.. ");
        sessionList.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection ...");
        sessionList.remove(session);
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


        switch (contentType) {
            case "ACK":
                // clients first connection on page load; send acknowledge
                JSONObject acknowledge = buildAnswerTemplate("ACK", "OK");
                sendToSession(session, acknowledge);
                break;
            case "move":
                // TODO: 04/07/2019 perform move operations
                String move = (String) parsedMessage.get("value");
                // received move should be in a valid format
                if (!MoveUtility.checkMoveFormat(move)) {
                    sendErrorMessageToClient("Invalid move format", session, "error");
                }

                break;
            default:
                break;
        }
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public void sendToSession(Session session, String message) throws IOException {
        JSONObject answer = buildAnswerTemplate();
        answer.put("data", message);

        session.getBasicRemote().sendText(answer.toJSONString());
    }

    public void sendToSession(Session session, JSONObject jsonObject) throws IOException {
        logger.info("sending to client...");
        session.getBasicRemote().sendText(jsonObject.toJSONString());
    }

    private void sendErrorMessageToClient(String message, Session session, String type) throws IOException {
        JSONObject answer = JSONHandler.buildAnswerTemplate();
        answer.put("content", type);
        answer.put("value", message);
        sendToSession(session, answer);

        // close connection on fatal error
        if (type.equals("fatal")) {
            session.close();
        }
    }

    private Lobby verifyRequest(Session session, String sessionID, String lobbyID, String contentType) throws IOException {
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
        logger.info("User part of lobby");

        return playerLobby;
    }
}