package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
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
        return Stream.of(
                Utils.directionalIteratorUntilOccupied(position, board, Position::topNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::bottomNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::leftNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::rightNeighbor))
                .flatMap(s -> s);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        // The rook can move vertically or horizontally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        // To obtain the capture positions find the nearest enemy piece along all directions.
        return Stream.of(
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::topNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::bottomNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::leftNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::rightNeighbor))
                .flatMap(Optional::stream);
    }


    @Override
    public char getFenIdentifier() {
        return white ? 'R' : 'r';
    }
}
