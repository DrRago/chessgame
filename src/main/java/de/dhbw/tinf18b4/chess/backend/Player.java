package de.dhbw.tinf18b4.chess.backend;

class Player {
    private boolean isWhite;

    Player(boolean isWhite) {
        this.isWhite = isWhite;
    }

    boolean isWhite() {
        return isWhite;
    }

    boolean isSamePlayer(Player other) {
        return equals(other);
    }
}
