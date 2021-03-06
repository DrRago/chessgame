package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * The queen implementation of the chess {@link Piece}
 * <p>
 * The queen moves horizontally, vertically or diagonally, through any number of unoccupied squares
 */
public class Queen implements Piece {
    /**
     * whether the queen is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the queen
     */
    @Getter
    @Setter
    @NotNull
    private Position position;

    /**
     * Initialize the queen with a {@link Position} and whether it is white
     *
     * @param white    whether the queen is white
     * @param position the {@link Position}
     */
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
    public boolean equals(Object obj) {
        if (!(obj instanceof Queen)) return false;
        Piece piece = (Piece) obj;
        return piece.getPosition() == position && piece.isWhite() == white;
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'Q' : 'q';
    }

    @Override
    public String toString() {
        return toPieceName();
    }
}
