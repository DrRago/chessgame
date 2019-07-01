package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;

/**
 * @author Leonhard.Gahr
 */
public interface Piece {

    /**
     * Move the piece to the given position. Returns true if the move was valid, false if it wasn't
     *
     * @param position the destination position
     * @return whether the move was made or not
     */
    boolean moveTo(Position position);

    /**
     * Get the position of the piece on the board. For captured pieces, the return value is unreliable
     *
     * @return where the piece is
     */
    Position getPosition();

    /**
     * Get a list of all possible moves for the piece. Dies not include kill moves
     *
     * @param board the board
     * @return a list of all possible moves
     */
    List<Position> getValidMoves(Board board);

    /**
     * Get a list of all possible capture options
     *
     * @param board the board
     * @return all capture options
     */
    List<Position> getValidCaptureMoves(Board board);

    /**
     * Get the color of the piece
     *
     * @return whether the piece is white or not
     */
    boolean isWhite();

    /**
     * Get the capture state of the piece
     *
     * @return whether the piece has been captured or not
     */
    boolean isCaptured();
}
