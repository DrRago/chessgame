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
    @NotNull
    private String displayName; // a display name
    @NotNull
    private final String ID; // session ID

    public User(@NotNull String ID) {
        this.displayName = UserUtility.getDisplayName(ID);
        this.ID = ID;
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
            if (user2.displayName.equalsIgnoreCase(this.displayName)) {
                return user2.ID.equals(this.ID);
            }
            return user2.displayName.equalsIgnoreCase(this.displayName);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("User '%s'", displayName);
    }
}
