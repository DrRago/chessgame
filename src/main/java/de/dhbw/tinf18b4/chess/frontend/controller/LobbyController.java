package de.dhbw.tinf18b4.chess.frontend.controller;

import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

;

/**
 * Controller for {@link Lobby}-related requests
 *
 * @author Leonhard Gahr
 */

@WebServlet({"/lobby", "/lobby/*"})
public class LobbyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // validate that the user is authenticated
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.validateLogin()) {
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
        } else {
            // show a lobby
            String lobbyID = reqPath.substring(1);
            Lobby lobby = LobbyManager.getLobbies().get(lobbyID);
            if (lobby != null) {
                // lobby exists
                if (lobby.hasUser(user)) {
                    // forward to lobby
                    req.getRequestDispatcher("/lobby.jsp?id=" + lobbyID).forward(req, resp);
                } else {
                    Player player = lobby.join(user);
                    if (player == null) {
                        resp.sendRedirect("/lobby/?error=lobby_full");
                    }

                    // forward to lobby
                    req.getRequestDispatcher("/lobby.jsp?id=" + lobbyID).forward(req, resp);
                }
            } else {
                // lobby not found
                req.getRequestDispatcher("/error/404.jsp").forward(req, resp);
            }
        }
    }
}
