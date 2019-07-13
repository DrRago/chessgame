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

    public Move(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Piece piece) {
        if (player.isWhite() != piece.isWhite()) {
            String message = String.format("Player %s can't move piece %s of another player %s", player, piece, player);
            throw new IllegalArgumentException(message);
        }

        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.piece = piece;
    }

    public Move(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        this(player, origin, destination, getPiece(player, origin, destination, board));
    }

    @NotNull
    private static Piece getPiece(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        Piece piece = board.findPieceByPosition(origin);
        Objects.requireNonNull(piece, String.format("Player %s can't move a piece from origin position %s to destination %s: there is no piece at the origin", player, origin, destination));

        return piece;
    }

    @Override
    public String toString() {
        return String.format("%s moved %s to %s", player.getUser().getDisplayName(), piece.getClass().getSimpleName(), destination);
    }
}
