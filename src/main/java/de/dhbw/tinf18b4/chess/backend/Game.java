package de.dhbw.tinf18b4.chess.backend;


import lombok.Getter;

/**
 * @author Leonhard Gahr
 */

public class Game {
    /**
     * The Board instance modeling the state of the Game
     */
    @Getter
    private final Board board = new Board();
    private final History history = new History();
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempt to perform a move on the board
     *
     * @param move The move
     * @return whether it the move was applied
     */
    public boolean makeMove(Move move) {
        Move lastMove = history.peekingPop();

        // Prevent player from making a move if they ...

        // ... don't belong in this game or ...
        if (player1.isSamePlayer(move.getPlayer())
                || player2.isSamePlayer(move.getPlayer())) {
            return false;

        }

        // ... have just made a move
        if (lastMove != null
                && move.getPlayer().isSamePlayer(lastMove.getPlayer())) {
            return false;
        }

        // Otherwise check if their move is possible
        // and apply the move eventually
        if (board.checkMove(move)) {
            board.applyMove(move);
            history.push(move);
            return true;
        }

        return false;
    }

    /**
     * We don't have a real History class yet
     * TODO: Implement the History class and remove this.
     */
    class History {
        /**
         * Add a move to history to make it the most recent move
         *
         * @param move The move
         */
        void push(Move move) {

        }

        /**
         * Return the most recent move without removing it from history
         *
         * @return The Move
         */
        Move peekingPop() {
            return null;
        }
    }
}
