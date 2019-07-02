package de.dhbw.tinf18b4.chess.frontend.controller;


import de.dhbw.tinf18b4.chess.backend.utility.UserUtility;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.jetbrains.annotations.NotNull;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * Controller to perform login and registration actions
 * it separates the following login actions:
 * - login: logging in with a username and password
 * - register: register a {@link User} with a username and password
 * - guest: logging in a {@link User} as guest
 *
 * @author Leonhard Gahr
 */
@WebServlet("/DoLoginOrRegister")
public class LoginController extends HttpServlet {
    private Logger logger = Logger.getLogger(LoginController.class.getName());

    /**
     * Determine whether a login or register request has been made,
     * perform that action and forward the user to a page, depending
     * on the request type and success/failure
     *
     * @param req  the user request
     * @param resp the response to send back to the user
     */
    @Override
    protected void doPost(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp) throws IOException {
        logger.info("Request received from " + req.getRemoteAddr());

        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        String sendTarget; // the page the user will be redirect to later

        // validate the correctness of the request parameters
        // redirect back to the login page and display an error
        if (!validateParameters(req) && !req.getParameter("function").equalsIgnoreCase("guest")) {
            logger.info("action failed due to invalid request parameters");

            sendTarget = String.format("login.jsp?username=%s&error=invalid_request", username);
        } else {
            HttpSession currSession = req.getSession(); // the current session object

            // determine function
            switch (req.getParameter("function")) {
                case "login":
                    logger.info("Logging in user: " + username);

                    User user = new User(username, password, currSession.getId());
                    if (user.validateLogin()) {
                        logger.info("User authenticated");

                        // user exists
                        currSession.setAttribute("user", user);
                        sendTarget = "index.jsp";
                    } else {
                        logger.info("user authentication failed");
                        // invalid credentials
                        sendTarget = String.format("login.jsp?username=%s&error=login_failed", username);
                    }
                    break;

                case "register":
                    logger.info("Registering new user: " + username);
                    // perform a register for the user
                    // check the password confirmation field
                    if (password.equals(req.getParameter("password-confirm"))) {
                        logger.info("Registration failed due to password mismatch");

                        sendTarget = String.format("login.jsp?username=%s&error=password_mismatch", username);
                    } else {
                        if (UserUtility.createUser(username, password)) {
                            // user is registered
                            logger.info("User registered");

                            User registeredUser = new User(username, password, currSession.getId());
                            currSession.setAttribute("user", registeredUser);

                            sendTarget = "registered.jsp";
                        } else {
                            logger.info("Registration failed due to username conflicts");

                            sendTarget = String.format("login.jsp?username=%s&error=username_conflict", username);
                        }
                    }
                    break;

                case "guest":
                    // logging user in as guest
                    logger.info("Logging in as guest");

                    User guestUser = new User("guest", "", currSession.getId());
                    currSession.setAttribute("user", guestUser);
                    sendTarget = "index.jsp";
                    break;

                default:
                    sendTarget = String.format("login.jsp?username=%s&error=invalid_method", username);
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
    private boolean validateParameters(@NotNull HttpServletRequest req) {
        final String[] REQUIRED_PARAMS = {"username", "password", "function"};

        for (String param : REQUIRED_PARAMS) {
            String paramValue = req.getParameter(param);
            // invalid request if the param doesn't exist or the value is empty
            if (paramValue == null || paramValue.isEmpty()) return false;
        }

        return true;
    }
}
