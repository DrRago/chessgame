package de.dhbw.tinf18b4.chess.backend.piece;


import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The bishop implementation of the chess {@link Piece}
 * <p>
 * The bishop moves diagonally, through any number of unoccupied squares
 */
public class Bishop implements Piece {
    /**
     * whether the bishop is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the bishop
     */
    @Getter
    @Setter
    private Position position;

    /**
     * Initialize the bishop with a {@link Position} and whether it is white
     *
     * @param white    whether the bishop is white
     * @param position the {@link Position}
     */
    public Bishop(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        // The bishop can move diagonally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        return Stream.of(
                Utils.directionalIteratorUntilOccupied(position, board, Position::upperLeftNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::upperRightNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::lowerLeftNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::lowerRightNeighbor))
                .flatMap(s -> s);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        // The bishop can move diagonally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        // To obtain the capture positions find the nearest enemy piece along all directions.
        return Stream.of(
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::upperLeftNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::upperRightNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::lowerLeftNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::lowerRightNeighbor))
                .flatMap(Optional::stream);
    }

    @Override
    public boolean isCaptured() {
        return false;
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'B' : 'b';
    }
}
