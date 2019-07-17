package de.dhbw.tinf18b4.chess.frontend.controller;

import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyStatus;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.frontend.SessionManager;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for {@link Lobby lobby-related} http requests
 */

@WebServlet({"/lobby", "/lobby/*"})
public class LobbyController extends HttpServlet {
    /**
     * Determine the action the user wants to proceed. Possible uri-paths are:
     * <ul>
     * <li>/lobby</li>
     * <li>/lobby/{ID}/</li>
     * <li>/lobby/{ID}/game</li>
     * </ul>
     * This controller forwards to the specific jsp page.
     * If the user is not logged in, he gets redirected to the login page
     *
     * @param req  the user request
     * @param resp the response to send back to the user
     */
    @Override
    protected void doGet(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) throws IOException, ServletException {
        // validate that the user is authenticated
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            user = new User("guest", "", req.getSession().getId());
            req.getSession().setAttribute("user", user);
            SessionManager.addSession(req.getSession());
        } else if (!user.validateLogin()) {
            req.getSession().setAttribute("user", null);
            resp.sendRedirect("/login.jsp");
            return;
        }


        // determine the site the user should see
        String reqPath = req.getPathInfo();
        if (reqPath == null || reqPath.equals("/")) {
            // lobby list
            req.getRequestDispatcher("/lobbies.jsp").forward(req, resp);
        } else if (reqPath.equals("/create")) {
            // new lobby and redirect to it
            String lobbyID = LobbyManager.createLobby((User) req.getSession().getAttribute("user"));
            resp.sendRedirect("/lobby/" + lobbyID);
        } else if (reqPath.matches("/.*/game/?")) {
            String lobbyID = reqPath.split("/")[1];
            req.getRequestDispatcher("/game.jsp?id=" + lobbyID).forward(req, resp);
        } else {
            // show a lobby
            String lobbyID = reqPath.substring(1);
            Lobby lobby = LobbyManager.getLobbies().get(lobbyID);
            if (lobby != null) {
                // lobby exists
                // user may not join if he already joined the lobby under another name
                if (lobby.hasUser(user) && lobby.getPlayerByUser(user).getUser().getID().equals(user.getID())) {
                    // check whether the game has already started and redirect in that case
                    if (lobby.getStatus() == LobbyStatus.GAME_STARTED) {
                        resp.sendRedirect("/lobby" + reqPath + "/game");
                        return;
                    }
                    // forward to lobby
                    req.getRequestDispatcher("/lobby.jsp?id=" + lobbyID).forward(req, resp);
                } else if (!lobby.hasUser(user)){
                    Player player = lobby.join(user);
                    if (player == null) {
                        resp.sendRedirect("/lobby/?error=lobby_full");
                    } else {

                        // forward to lobby
                        req.getRequestDispatcher("/lobby.jsp?id=" + lobbyID).forward(req, resp);
                    }
                } else {
                    resp.sendRedirect("/lobby/?error=already_in_lobby");
                }
            } else {
                // lobby not found
                req.getRequestDispatcher("/error/404.jsp").forward(req, resp);
            }
        }
    }
}
