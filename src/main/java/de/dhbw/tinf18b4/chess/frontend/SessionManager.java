package de.dhbw.tinf18b4.chess.frontend;

import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * static class to manage all http sessions to the server
 *
 * @author Leonhard Gahr
 */
public class SessionManager {
    /**
     * the list holding all logged in sessions
     */
    @NotNull
    private static List<HttpSession> sessions = new ArrayList<>();

    /**
     * Add a session to the list
     *
     * @param session the session to add
     */
    public static void addSession(HttpSession session) {
        sessions.add(session);
    }

    /**
     * find a session with a specific ID
     *
     * @param sessionID the sessionID to find
     * @return the session or null
     */
    public static HttpSession findSessionByID(String sessionID) {
        return sessions.stream().filter(s -> s.getId().equals(sessionID)).findAny().orElse(null);
    }
}
