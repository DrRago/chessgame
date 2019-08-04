package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An move made by a player on board. When it is applied onto a board it will move a piece
 * from its current position on the board to the destination position.
 * To prevent invalid moves, one must first check this move against the game rules
 * for this piece and the current board state.
 */
public class Move {
    /**
     * player associated with this move
     */
    @NotNull
    @Getter
    final private Player player;

    /**
     * the original position of this move
     */
    @NotNull
    @Getter
    final private Position origin;

    /**
     * the destination position of this move
     */
    @NotNull
    @Getter
    final private Position destination;

    /**
     * the piece associated with this move
     */
    @NotNull
    @Getter
    final private Piece piece;

    /**
     * Create a new Move instance from the {@link Player}, origin {@link Position}, destination {@link Position} and the {@link Piece}
     *
     * @param player      the {@link Player} who wants to perform the move operation
     * @param origin      the origin {@link Position}
     * @param destination the destination {@link Position}
     * @param piece       the {@link Piece} to move
     */
    private Move(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Piece piece) {
        if (player.isWhite() != piece.isWhite()) {
            String message = String.format("Player %s can't move piece %s of another player %s", player, piece, player);
            throw new IllegalArgumentException(message);
        }

        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.piece = piece;
    }

    /**
     * Create a new Move instance from the {@link Player}, origin {@link Position}, destination {@link Position} and {@link Board}
     *
     * @param player      the {@link Player} who wants to perform the move operation
     * @param origin      the origin {@link Position}
     * @param destination the destination {@link Position}
     * @param board       the {@link Board}
     */
    public Move(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        this(player, origin, destination, getPiece(player, origin, destination, board));
    }

    /**
     * Get the {@link Piece} at a {@link Position} from a {@link Board}.
     * Throws {@link IllegalArgumentException} if there is not piece at the origin {@link Position}
     *
     * @param player      the {@link Player} who want's to move the {@link Piece}
     * @param origin      the origin {@link Position} of the {@link Piece} to move
     * @param destination the destination {@link Position} to move the {@link Piece} to
     * @param board       the {@link Board} to perform the {@link Move} on
     * @return the {@link Piece} that shall be moved
     */
    @NotNull
    private static Piece getPiece(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        Piece piece = board.findPieceByPosition(origin);
        Objects.requireNonNull(piece, String.format("Player %s can't move a piece from origin position %s to destination %s: there is no piece at the origin", player, origin, destination));

        return piece;
    }

    /**
     * Get the move as fen string
     * @return the move fen string
     */
    @NotNull
    public String toFenMove() {
        return String.format("%s-%s", origin, destination);
    }

    @Override
    public String toString() {
        return String.format("%s moved %s from %s to %s", player.getUser().getDisplayName(), piece.getClass().getSimpleName(), origin, destination);
    }
}
