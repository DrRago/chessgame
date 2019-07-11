package de.dhbw.tinf18b4.chess.backend;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * This class is used to store all the moves made by the player of a game.
 * It allows adding and peeking operations which are backed by a stack internally.
 * Additionally it can be used to obtain a complete listing of all the moves.
 */
public class History {
    private Stack<Move> history = new Stack<>();

    /**
     * Add a move to history to make it the most recent move
     *
     * @param move the move
     */
    void addMove(Move move) {
        history.add(move);
    }

    /**
     * Return the most recent move without removing it from history
     *
     * @return the Move
     */
    public Move lastMove() {
        try {
            return history.peek();
        } catch (EmptyStackException empty) {
            return null;
        }
    }

    /**
     * Returns all moves
     *
     * @return the moves
     */
    public Stream<Move> stream() {
        return history.stream();
    }
}
