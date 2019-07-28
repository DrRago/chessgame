package de.dhbw.tinf18b4.chess.backend.moves;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.piece.Pawn;
import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnPassantMove extends Move {
    private Pawn capturedPawn;

    public EnPassantMove(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        super(player, origin, destination, findPawn(player, origin, destination, board));

        Position capturePosition = player.isWhite() ? destination.bottomNeighbor() : destination.topNeighbor();
        Piece pawn = board.findPieceByPosition(Objects.requireNonNull(capturePosition));

        if (!(pawn instanceof Pawn)) {
            throw new IllegalStateException("Attempted to create en passant move without pawn");
        }

        this.capturedPawn = (Pawn) pawn;

        // Prevent en passant from capturing own pieces
        if (getCapturedPawn().isOwnedBySamePlayer(player)) {
            throw new IllegalArgumentException("Attempted to capture own pawn with en passant move");
        }

        if (getCapturingPawn().getValidCaptureMoves(board).noneMatch(position -> position.equals(destination))) {
            throw new IllegalArgumentException("Attempted to create en passant move when not possible");
        }
    }

    private static @NotNull Pawn findPawn(@NotNull Player player, @NotNull Position origin, @NotNull Position destination, @NotNull Board board) {
        Piece pawn = getPiece(player, origin, destination, board);

        if (pawn instanceof Pawn) {
            return (Pawn) pawn;
        }

        throw new IllegalArgumentException("Attempted to create en passant move without pawn");
    }

    public @NotNull Pawn getCapturingPawn() {
        return (Pawn) getPiece();
    }

    public @NotNull Pawn getCapturedPawn() {
        return capturedPawn;
    }
}
