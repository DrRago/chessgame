package de.dhbw.tinf18b4.chess.backend.moves;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.piece.King;
import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.piece.Rook;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class CastlingMove extends Move {
    @Getter
    private Rook rook;
    private Position rookOrigin;
    private Position rookDestination;

    /**
     * Create a new Move instance from the {@link Player}, origin {@link Position}, destination {@link Position} and {@link Board}
     *
     * @param player          the {@link Player} who wants to perform the move operation
     * @param kingOrigin      the origin {@link Position}
     * @param kingDestination the destination {@link Position}
     * @param board           the {@link Board}
     */
    public CastlingMove(@NotNull Player player, @NotNull Position kingOrigin, @NotNull Position kingDestination, @NotNull Board board) {
        super(player, kingOrigin, kingDestination, findKing(player, kingOrigin, kingDestination, board));

        Position rookDestination = kingDestination;
        String initialPosition;

        int distance = kingDestination.getFile() - kingOrigin.getFile();

        if (distance == 2) {
            if (player.isWhite()) {
                // queenside castling
                rookDestination = rookDestination.rightNeighbor();
                initialPosition = "a1";
            } else {
                // kingside castling
                rookDestination = rookDestination.leftNeighbor();
                initialPosition = "h8";
            }
        } else if (distance == -2) {
            if (player.isWhite()) {
                // kingside castling
                rookDestination = rookDestination.leftNeighbor();
                initialPosition = "h1";
            } else {
                // queenside castling
                rookDestination = rookDestination.rightNeighbor();
                initialPosition = "a8";
            }
        } else {
            throw new IllegalStateException("Attempted to create castling move but king isn't moving exactly two square file-wise");
        }

        Position rookOrigin = new Position(initialPosition);
        Piece rook = board.findPieceByPosition(rookOrigin);

        if (!(rook instanceof Rook)) {
            throw new IllegalStateException("Attempted to create castling move without rook");
        }

        this.rook = (Rook) rook;
        this.rookDestination = rookDestination;
        this.rookOrigin = rookOrigin;
    }

    private static @NotNull King findKing(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        Piece king = getPiece(player, origin, destination, board);

        if (king instanceof King) {
            return (King) king;
        }

        throw new IllegalArgumentException("Attempted to create castling move without king");
    }

    public @NotNull King getKing() {
        return (King) getPiece();
    }

    public @NotNull Position getKingOrigin() {
        return getOrigin();
    }

    public @NotNull Position getKingDestination() {
        return getDestination();
    }

    public @NotNull Position getRookOrigin() {
        return rookOrigin;
    }

    public @NotNull Position getRookDestination() {
        return rookDestination;
    }

    @Override
    public String toString() {
        return String.format("%s moved %s from %s to %s and %s from %s to %s", getPlayer().getUser().getDisplayName(), getKing().getClass().getSimpleName(), getKingOrigin(), getKingDestination(), getRook().getClass().getSimpleName(), getRookOrigin(), getRookDestination());
    }
}
