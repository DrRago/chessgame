package de.dhbw.tinf18b4.chess.backend.utility;

import de.dhbw.tinf18b4.chess.backend.user.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Some utility functions for the user
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
        password = DigestUtils.sha512Hex(password);
        try {
            MySQLUtility.executeQuery("INSERT INTO user VALUES(?, ?, '1')", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * check the username and password combination
     *
     * @param username the username
     * @param password the password
     * @return whether the combination exists or not
     */
    public static boolean login(String username, String password) {
        password = DigestUtils.sha512Hex(password);

        try {
            ResultSet result = MySQLUtility.executeQuery("SELECT * FROM user WHERE username=? AND password=?", username, password);
            int size = 0;
            if (result != null) {
                result.last();    // moves cursor to the last row
                size = result.getRow(); // get row id
            }
            return size == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get the display name of a user who already visited the website by the session ID
     *
     * @param sessionID the HTTP session ID of the user
     * @return the display name
     */
    public static String getDisplayName(String sessionID) {
        try {
            ResultSet result = MySQLUtility.executeQuery("SELECT * FROM session WHERE ID=?", sessionID);
            if (result == null || !result.next()) {
                return "guest";
            } else {
                return result.getString("displayName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "guest";
    }

    /**
     * get the display name of a user who already visited the website by the session ID
     *
     * @param sessionID   the HTTP session ID of the user
     * @param displayName the new display name
     */
    public static void updateDisplayName(String sessionID, String displayName) {
        try {
            MySQLUtility.executeQuery("INSERT INTO session VALUES(?, ?) ON DUPLICATE KEY UPDATE displayName=?", sessionID, displayName, displayName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
