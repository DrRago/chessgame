package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * The rook implementation of the chess {@link Piece}
 * <p>
 * The rook moves horizontally or vertically, through any number of unoccupied squares
 */
public class Rook implements Piece {
    /**
     * whether the rook is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the rook
     */
    @Getter
    @Setter
    private Position position;

    /**
     * Initialize the rook with a {@link Position} and whether it is white
     *
     * @param white    whether the rook is white
     * @param position the {@link Position}
     */
    public Rook(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        // The rook can move vertically or horizontally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        return Utils.directionalIterator(position, board,
                Position::topNeighbor,
                Position::bottomNeighbor,
                Position::leftNeighbor,
                Position::rightNeighbor);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        return getValidMoves(board);
    }


    @Override
    public char getFenIdentifier() {
        return white ? 'R' : 'r';
    }
}
