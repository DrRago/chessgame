package de.dhbw.tinf18b4.chess.frontend.controller;


import de.dhbw.tinf18b4.chess.frontend.user.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * @author Leonhard Gahr
 */
@WebServlet("/DoLoginOrRegister")
public class LoginController extends HttpServlet {
    Logger logger = Logger.getLogger(LoginController.class.getName());

    /**
     * Determine whether a login or register request has been made,
     * perform that action and forward the user to a page, depending
     * on the request type and success/failure
     *
     * @param req  the user request
     * @param resp the response to send back to the user
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        String sendTarget; // the page the user will be redirect to later

        // validate the correctness of the request parameters
        // redirect back to the login page and display an error
        if (!validateParameters(req)) {
            sendTarget = String.format("login.jsp?username=%s&error=1", username);
        } else {
            HttpSession currSession = req.getSession(); // the current session object

            // determine function
            switch (req.getParameter("function")) {
                case "login":
                    User user = new User(username, password, currSession.getId());
                    if (user.validateLogin()) {
                        // user exists
                        currSession.setAttribute("loginUser", user);
                        sendTarget = "index.jsp";
                    } else {
                        // invalid credentials
                        sendTarget = String.format("login.jsp?username=%s&error=2", username);
                    }
                    break;
                case "register":
                    // TODO: 01/07/2019 do registration
                    sendTarget = "registered.jsp";
                    break;
                default:
                    sendTarget = String.format("login.jsp?username=%s&error=3", username);
                    break;
            }
        }

        resp.sendRedirect(sendTarget);
    }

    /**
     * perform parameter checks like: do all required parameters exist?
     *
     * @param req the http request
     * @return whether all parameters exist
     */
    private boolean validateParameters(HttpServletRequest req) {
        final String[] REQUIRED_PARAMS = {"username", "password", "function"};

        for (String param : REQUIRED_PARAMS) {
            String paramValue = req.getParameter(param);
            // invalid request if the param doesn't exist or the value is empty
            if (paramValue == null || paramValue.isEmpty()) return false;
        }

        return true;
    }
}
