package de.dhbw.tinf18b4.chess.backend.user;

import de.dhbw.tinf18b4.chess.backend.utility.UserUtility;
import lombok.Getter;
import lombok.Setter;

/**
 * A object holding a user, who wants to login or is logged in
 *
 * @author Leonhard Gahr
 */
@Getter
@Setter
public class User {
    private String username; // login name
    private String displayName; // a display name
    private String password; // login password
    private final String ID; // session ID
    private Permission permission; // the permission level

    public User(String username, String password, String ID) {
        this(username, password, ID, Permission.USER);
    }

    public User(String username, String password, String ID, Permission permission) {
        this.username = username;
        this.displayName = username; // TODO change this
        this.password = password;
        this.ID = ID;
        this.permission = permission;
    }

    /**
     * Override for the equals method for a user
     * <p>
     * two users are equal if
     * - the username is the same
     * - or if a username is guest, if the session ids are the same
     *
     * @param obj the object to compare
     * @return whether the object is the same as this
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user2 = (User) obj;
            if (user2.username.equalsIgnoreCase("guest") ||
                    this.username.equalsIgnoreCase("guest")) {
                return user2.ID.equals(this.ID);
            }
            return user2.username.equalsIgnoreCase(this.username);
        }
        return false;
    }

    /**
     * Check whether the user credentials are correct
     * and the user exists with that password combination
     *
     * @return the validity check (true for valid, false for invalid)
     */
    public boolean validateLogin() {
        return UserUtility.login(this.username, this.password);
    }
}
