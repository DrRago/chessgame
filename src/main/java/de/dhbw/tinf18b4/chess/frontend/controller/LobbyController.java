package de.dhbw.tinf18b4.chess.frontend.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Leonhard Gahr
 */

@WebServlet("/lobby")
public class LobbyController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        switch (req.getParameter("function")) {
            case "create":
                // TODO: 01/07/2019 implement create lobby
                break;
            case "join":
                // TODO: 01/07/2019 implement join lobby
                break;
            default:
                // TODO: 01/07/2019 implement error response
                break;
        }
    }
}
