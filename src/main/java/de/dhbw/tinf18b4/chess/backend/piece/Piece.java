package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author Leonhard.Gahr
 */
public interface Piece {

    /**
     * Get the position of the piece on the board. For captured pieces, the return value is unreliable
     *
     * @return where the piece is
     */
    Position getPosition();

    /**
     * Move the piece to the given position
     *
     * @param position the destination position
     */
    void setPosition(Position position);

    /**
     * Get a list of all possible moves for the piece. Dies not include kill moves
     *
     * @param board the board
     * @return a list of all possible moves
     */
    Stream<Position> getValidMoves(@NotNull Board board);

    /**
     * Get a list of all possible capture options
     *
     * @param board the board
     * @return all capture options
     */
    Stream<Position> getValidCaptureMoves(@NotNull Board board);

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

    /**
     * Get the FEN identifier of the piece
     *
     * @return the FEN identifier
     */
    char getFenIdentifier();

    default boolean hasEverMoved(Game game) {
        return 0 == numberOfMoves(game);
    }

    default int numberOfMoves(Game game) {
        long count = game.getHistory().stream()
                .filter(move -> move.getPiece().equals(this))
                .count();

        return Math.toIntExact(count);
    }
}
