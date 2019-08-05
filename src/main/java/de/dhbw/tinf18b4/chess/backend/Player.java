package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The player of a game <br>
 * The player has an associated {@link User} which may be the guest {@link User}
 * and whether he plays as white or not
 */
public class Player {
    /**
     * Whether tha player plays white or not
     */
    @Getter
    private final boolean white;
    /**
     * The {@link User} who plays this player
     */
    @Getter
    private final User user;

    /**
     * Create a new player instance
     *
     * @param white whether the player plays white
     * @param user  the {@link User} who plays this player
     */
    public Player(boolean white, @NotNull User user) {
        this.white = white;
        this.user = user;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return user.equals(player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", user.getDisplayName(), white ? "W" : "B");
    }
}
