package de.dhbw.tinf18b4.chess.backend.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Some utility functions for the user
 */
public class UserUtility {
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
