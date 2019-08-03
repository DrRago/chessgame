package de.dhbw.tinf18b4.chess.frontend.controller;

import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.backend.utility.UserUtility;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Helper class to allow a user to change it's username. <br>
 * This method is fully session based
 */
@WebServlet("/user/changeName")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) return;
        String newName = req.getParameter("name");
        if (newName == null) return;
        System.out.println(newName);

        user.setDisplayName(newName);
        UserUtility.updateDisplayName(user.getID(), newName);
    }
}
