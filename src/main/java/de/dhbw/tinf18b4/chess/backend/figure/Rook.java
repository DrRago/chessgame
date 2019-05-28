package de.dhbw.tinf18b4.chess.backend.figure;

import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;

/**
 * @author Leonhard Gahr
 */
public class Rook implements Figure {
    /**
     * Move the figure to the given position. Returns true if the move was valid, false if it wasn't
     *
     * @param position the destination position
     * @return whether the move was made or not
     */
    @Override
    public boolean moveTo(Position position) {
        return false;
    }

    /**
     * Get a list of all possible moves for the figure. Dies not include kill moves
     *
     * @return a list of all possible moves
     */
    @Override
    public List<Position> getValidMoves() {
        return null;
    }

    /**
     * Get a list of all possible capture options
     *
     * @return all capture options
     */
    @Override
    public List<Position> getValidCaptureMoves() {
        return null;
    }

    /**
     * Get the color of the figure
     *
     * @return whether the figure is white or not
     */
    @Override
    public boolean isWhite() {
        return false;
    }

    /**
     * Get the capture state of the figure
     *
     * @return whether the figure has been captured or not
     */
    @Override
    public boolean isCaptured() {
        return false;
    }
}
