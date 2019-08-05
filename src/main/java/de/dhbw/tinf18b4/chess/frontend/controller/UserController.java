package de.dhbw.tinf18b4.chess.frontend.controller;

import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.backend.utility.UserUtility;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Helper class to allow a user to change it's username. <br>
 * This method is fully session based
 */
@WebServlet("/user/changeName")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) return;

        // due to a bug in HttpServlet implementation we need to parse the url parameter by ourselves
        String encodedURL = URLDecoder.decode(req.getQueryString(), StandardCharsets.UTF_8.toString());
        String newName = encodedURL.replace("name=", "");
        if (newName.contains("<") || newName.contains(">")) return;

        user.setDisplayName(newName);
        UserUtility.updateDisplayName(user.getID(), newName);
    }
}
