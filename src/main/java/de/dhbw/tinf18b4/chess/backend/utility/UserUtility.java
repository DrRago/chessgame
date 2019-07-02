package de.dhbw.tinf18b4.chess.backend.utility;

import de.dhbw.tinf18b4.chess.backend.user.User;

/**
 * Some utility functions for the user
 *
 * @author Leonhard Gahr
 */
public class UserUtility {
    /**
     * Create a {@link User} with the given username and password
     * if the {@link User} already exists, return false, otherwise true
     *
     * @param username the username to create
     * @param password the password to create the {@link User} with
     * @return whether the registration was successful
     */
    public static boolean createUser(String username, String password) {
        return false;
    }

    /**
     * check the username and password combination
     *
     * @param username the username
     * @param password the password
     * @return whether the combination exists or not
     */
    public static boolean login(String username, String password) {
        return true;
    }
}
