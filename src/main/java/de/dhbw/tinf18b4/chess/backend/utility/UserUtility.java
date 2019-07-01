package de.dhbw.tinf18b4.chess.backend.utility;

/**
 * @author Leonhard Gahr
 */
public class UserUtility {
    /**
     * Create a user with the given username and password
     * if the user already exists, return false, otherwise true
     *
     * @param username the username to create
     * @param password the password to create the user with
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
