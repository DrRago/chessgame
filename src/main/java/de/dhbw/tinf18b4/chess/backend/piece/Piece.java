package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Interface of a piece on the {@link Board chessboard}
 * <p>
 * Every real piece implements this interface
 */
public interface Piece {

    /**
     * Get the position of the piece on the board. For captured pieces, the return value is unreliable
     *
     * @return where the piece is
     */
    @NotNull
    Position getPosition();

    /**
     * Move the piece to the given position
     *
     * @param position the destination position
     */
    void setPosition(@NotNull Position position);

    /**
     * Get a list of all possible moves for the piece. Dies not include kill moves
     *
     * @param board the board
     * @return a list of all possible moves
     */
    @NotNull
    Stream<Position> getValidMoves(@NotNull Board board);

    default boolean canMakeValidMove(@NotNull Board board) {
        return getValidMoves(board).count() > 0;
    }

    /**
     * Get a list of all possible capture options
     *
     * @param board the board
     * @return all capture options
     */
    @NotNull
    Stream<Position> getValidCaptureMoves(@NotNull Board board);

    default boolean canMakeValidCaptureMove(@NotNull Board board) {
        return getValidCaptureMoves(board).count() > 0;
    }

    /**
     * Get the color of the piece
     *
     * @return whether the piece is white or not
     */
    boolean isWhite();

    default boolean isBlack() {
        return !isWhite();
    }

    /**
     * Get the FEN identifier of the piece
     *
     * @return the FEN identifier
     */
    char getFenIdentifier();

    default boolean hasNeverMoved(@NotNull Game game) {
        return 0 == numberOfMoves(game);
    }

    default boolean hasEverMoved(@NotNull Game game) {
        return 0 != numberOfMoves(game);
    }

    default int numberOfMoves(@NotNull Game game) {
        long count = game.getHistory().stream()
                .filter(move -> move.getPiece().equals(this))
                .count();

        return Math.toIntExact(count);
    }

    /**
     * Check whether this piece and another are owned by the same player
     *
     * @param piece the piece to test against
     * @return true is this piece and the other are owned by the same player
     */
    default boolean isOwnedBySamePlayer(@NotNull Piece piece) {
        return isWhite() == piece.isWhite();
    }

    default boolean isOwnedBySamePlayer(@NotNull Player player) {
        return isWhite() == player.isWhite();
    }

    /**
     * Check whether this piece and another are owned by different players
     *
     * @param piece the piece to test against
     * @return true is this piece and the other are owned by different players
     */
    default boolean isOwnedByEnemy(@NotNull Piece piece) {
        return isBlack() == piece.isWhite();
    }

    default boolean isOwnedByEnemy(@NotNull Player player) {
        return isWhite() != player.isWhite();
    }

    /**
     * Move the piece perspectively backwards, so move it down if it is white and up if it's black.
     * <p>
     * Do nothing if the destination is offboard
     *
     * @return the perspectively back position
     */
    default Position getBackwardsPosition() {
        return isWhite() ? getPosition().bottomNeighbor() : getPosition().topNeighbor();
    }

    /**
     * Return the name of the piece
     *
     * @return the name
     */
    default String toPieceName() {
        return getClass().getSimpleName();
    }
}
