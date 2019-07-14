package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class Utils {

    /**
     * I don't really know how this function actually works.
     *
     * @param initialPiecePosition the {@link Position} to start the iterator
     * @param board                the {@link Board} to check whether a {@link Position} {@link Board#isOccupied(Position) is occupied}
     * @param directions           the directions where to move the iterator
     * @return the positions the iterator found
     */
    @SafeVarargs
    static Stream<Position> directionalIterator(Position initialPiecePosition, Board board, UnaryOperator<Position>... directions) {
        return Stream.of(directions)
                .map(f -> Stream.iterate(initialPiecePosition, f)
                        // skip the first element which is the position of this piece
                        .skip(1)
                        // stop when we reach the end of the board
                        .takeWhile(Objects::nonNull)
                        // or when we reach another piece
                        .takeWhile(position -> !board.isOccupied(position)))
                .flatMap(s -> s);
    }

    /**
     * Get all {@link Position positions} from an initial {@link Position} in a direction until the {@link Board} ends
     *
     * @param initialPiecePosition the initial {@link Position}
     * @param direction            the direction to move to
     * @return All positions in a direction
     */
    static Stream<Position> directionalIteratorUntilEdge(Position initialPiecePosition, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull);
    }

    /**
     * Get all {@link Position positions} from an initial {@link Position} in a direction until the {@link Position} {@link Board#isOccupied(Position) is occupied}
     *
     * @param initialPiecePosition the initial {@link Position}
     * @param board                the {@link Board} to check whether a {@link Position} {@link Board#isOccupied(Position) is occupied}
     * @param direction            the direction to move to
     * @return All {@link Position} in a direction until a occupied one
     */
    static Stream<Position> directionalIteratorUntilOccupied(Position initialPiecePosition, Board board, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull)
                // or when we reach another piece
                .takeWhile(position -> !board.isOccupied(position));
    }

    /**
     * Get an {@link Optional optional position} of a direction from an initial {@link Position}
     *
     * @param initialPiecePosition the initial {@link Position}
     * @param board                the {@link Board} to check whether a {@link Position} {@link Board#isOccupied(Position) is occupied}
     * @param white                whether the {@link Player} is white or not
     * @param direction            the direction to check
     * @return the {@link Optional optional position}
     */
    @SuppressWarnings("ConstantConditions")
    static Optional<Position> directionalIteratorFirstEnemy(Position initialPiecePosition, Board board, boolean white, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull)
                .filter(board::isOccupied)
                .takeWhile(position -> board.findPieceByPosition(position).isWhite() != white)
                .findFirst();
    }
}
