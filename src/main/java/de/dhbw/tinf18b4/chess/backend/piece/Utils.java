package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class Utils {
    @SafeVarargs
    @NotNull
    static Stream<Position> directionalIterator(Position initialPiecePosition, @NotNull Board board, UnaryOperator<Position>... directions) {
        return Stream.of(directions)
                .map(f -> directionalIteratorUntilEdge(initialPiecePosition, f)
                        // or when we reach another piece
                        .takeWhile(position -> !board.isOccupied(position)))
                .flatMap(s -> s);
    }

    @NotNull
    static Stream<Position> directionalIteratorUntilEdge(Position initialPiecePosition, @NotNull UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull);
    }

    @NotNull
    static Stream<Position> directionalIteratorUntilOccupied(Position initialPiecePosition, @NotNull Board board, @NotNull UnaryOperator<Position> direction) {
        return directionalIteratorUntilEdge(initialPiecePosition, direction)
                // or when we reach another piece
                .takeWhile(board::isNotOccupied);
    }

    @NotNull
    static Optional<Position> directionalIteratorFirstEnemy(Position initialPiecePosition, @NotNull Board board, boolean white, @NotNull UnaryOperator<Position> direction) {
        return directionalIteratorUntilEdge(initialPiecePosition, direction)
                .filter(board::isOccupied)
                .takeWhile(position -> board.findPieceByPosition(position).isWhite() != white)
                .findFirst();
    }
}
