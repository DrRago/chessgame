package de.dhbw.tinf18b4.chess.backend.figure;

import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;

/**
 * @author Leonhard Gahr
 */
public class Rook implements Figure {
    private final boolean white;
    private Position position;

    public Rook(boolean white, Position position) {
        this.white = white;
        this.position = position;
    }

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
     * Get the position of the figure on the board
     *
     * @return where the figure is
     */
    @Override
    public Position getPosition() {
        return position;
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
        return white;
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
