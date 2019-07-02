package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;

public class Player {
    private boolean isWhite;
    @Getter
    private User user;

    public Player(boolean isWhite, User user) {
        this.isWhite = isWhite;
        this.user = user;
    }

    boolean isWhite() {
        return isWhite;
    }

    boolean isSamePlayer(Player other) {
        return equals(other);
    }
}
