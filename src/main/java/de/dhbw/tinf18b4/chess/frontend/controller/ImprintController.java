package de.dhbw.tinf18b4.chess.frontend.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This controller simply maps the url /imprint to imprint.jsp
 */
@WebServlet("/imprint")
public class ImprintController  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("imprint.jsp").forward(req, resp);
    }
}
