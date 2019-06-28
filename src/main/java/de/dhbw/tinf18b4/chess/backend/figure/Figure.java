package de.dhbw.tinf18b4.chess.backend.figure;

import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;

/**
 * @author Leonhard.Gahr
 */
public interface Figure {

    /**
     * Move the figure to the given position. Returns true if the move was valid, false if it wasn't
     *
     * @param position the destination position
     * @return whether the move was made or not
     */
    boolean moveTo(Position position);

    /**
     * Get the position of the figure on the board. For captured figures, the return value is unreliable
     *
     * @return where the figure is
     */
    Position getPosition();

    /**
     * Get a list of all possible moves for the figure. Dies not include kill moves
     *
     * @return a list of all possible moves
     */
    List<Position> getValidMoves();

    /**
     * Get a list of all possible capture options
     *
     * @return all capture options
     */
    List<Position> getValidCaptureMoves();

    /**
     * Get the color of the figure
     *
     * @return whether the figure is white or not
     */
    boolean isWhite();

    /**
     * Get the capture state of the figure
     *
     * @return whether the figure has been captured or not
     */
    boolean isCaptured();
}
