package de.dhbw.tinf18b4.chess.states;

import de.dhbw.tinf18b4.chess.backend.Player;

public enum GameState {
    STALEMATE,
    KING_VS_BISHOP,
    KING_VS_KING,
    KING_BISHOP_VS_KING,
    KING_KNIGHT_VS_KING,
    KING_BISHOP_VS_KING_BISHOP,
    WON,
    ONGOING;

    public static Player winner = null;

    /**
     * Indicate that a game has finished and a player could be determined as a winner.
     *
     * @return whether the game is in the ongoing state
     */
    public boolean isWon() {
        return this == WON;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * Indicate that a game is has finished but no player could claim the win.
     *
     * @return whether the game is in the ongoing state
     */
    public boolean isDraw() {
        switch (this) {
            case STALEMATE:
            case KING_VS_BISHOP:
            case KING_VS_KING:
            case KING_BISHOP_VS_KING:
            case KING_KNIGHT_VS_KING:
            case KING_BISHOP_VS_KING_BISHOP:
                return true;
            default:
                return false;
        }
    }

    /**
     * Indicate that a game is currently ongoing. This means there are moves left for the player.
     *
     * @return whether the game is in the ongoing state
     */
    public boolean isOngoing() {
        return this == ONGOING;
    }
}
