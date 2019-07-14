package de.dhbw.tinf18b4.chess.backend.user;

import de.dhbw.tinf18b4.chess.backend.utility.UserUtility;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A object holding a user, who wants to login or is logged in.<br>
 * Performs functions like validate the 
 */
@Getter
@Setter
public class User {
    private String username; // login name
    private String displayName; // a display name
    private String password; // login password
    private final String ID; // session ID
    private Permission permission; // the permission level

    public User(@NotNull String username, @NotNull String password, @NotNull String ID) {
        this(username, password, ID, Permission.USER);
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String ID, @NotNull Permission permission) {
        this.username = username;
        this.displayName = username; // TODO change this
        this.password = password;
        this.ID = ID;
        this.permission = permission;
    }

    /**
     * Override for the equals method for a {@link User}
     * <p>
     * two users are equal if
     * <ul>
     * <li>the username is the same</li>
     * <li>or if a username is guest, if the session ids are the same</li>
     * </ul>
     * if the argument is a {@link String} it is compared with the user ID
     *
     * @param obj the object to compare
     * @return whether the object is the same as this
     */
    @Override
    public boolean equals(@Nullable Object obj) {
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
     * Check whether the {@link User} credentials are correct
     * and the {@link User} exists with that password combination
     *
     * @return the validity check (true for valid, false for invalid)
     */
    public boolean validateLogin() {
        if (username.equalsIgnoreCase("guest")) return true;
        return UserUtility.login(this.username, this.password);
    }

    @Override
    public String toString() {
        return String.format("User '%s' - %s", username, permission);
    }
}
