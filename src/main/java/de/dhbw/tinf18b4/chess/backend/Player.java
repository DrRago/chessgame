package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Player {
    private boolean isWhite;
    @Getter
    private User user;

    public Player(boolean isWhite, @NotNull User user) {
        this.isWhite = isWhite;
        this.user = user;
    }

    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public boolean equals(Object o) {
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
        return String.format("%s (%s)", user.getUsername(), isWhite ? "W" : "B");
    }
}
