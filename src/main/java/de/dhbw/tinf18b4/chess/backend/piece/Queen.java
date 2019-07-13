package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Queen implements Piece {
    @Getter
    private final boolean white;

    @Getter
    @Setter
    @NotNull
    private Position position;

    public Queen(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @NotNull
    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        return Stream.concat(new Rook(white, position).getValidMoves(board), new Bishop(white, position).getValidMoves(board));
    }

    @NotNull
    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        return Stream.concat(new Rook(white, position).getValidCaptureMoves(board), new Bishop(white, position).getValidCaptureMoves(board));
    }

    @Override
    public boolean isCaptured() {
        return false;
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'Q' : 'q';
    }
}
