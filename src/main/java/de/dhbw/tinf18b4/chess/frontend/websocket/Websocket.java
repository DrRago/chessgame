package de.dhbw.tinf18b4.chess.frontend.websocket;


import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.dhbw.tinf18b4.chess.frontend.JSON.JSONHandler.buildAnswerTemplate;

/**
 * @author Leonhard Gahr
 */
@ServerEndpoint("/websocketendpoint")
public class Websocket extends HttpServlet {
    private static List<Session> sessionList = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Open Connection ...");
        sessionList.add(session);

        // generate UUID and send it to the client
        sendToSession(session, String.valueOf(UUID.randomUUID()));
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection ...");
        sessionList.remove(session);
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("Message from the client: " + message);
        return "Echo from the server : " + message;
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
        session.getBasicRemote().sendText(jsonObject.toJSONString());
    }
}