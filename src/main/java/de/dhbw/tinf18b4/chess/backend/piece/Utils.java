package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class Utils {
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

    static Stream<Position> directionalIteratorUntilEdge(Position initialPiecePosition, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull);
    }

    static Stream<Position> directionalIteratorUntilOccupied(Position initialPiecePosition, Board board, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull)
                // or when we reach another piece
                .takeWhile(position -> !board.isOccupied(position));
    }

    static Optional<Position> directionalIteratorFirstEnemy(Position initialPiecePosition, Board board, boolean white, UnaryOperator<Position> direction) {
        return Stream.iterate(initialPiecePosition, direction)
                // skip the first element which is the position of this piece
                .skip(1)
                // stop when we reach the end of the board
                .takeWhile(Objects::nonNull)
                .filter(board::isOccupied)
                .filter(position -> board.findPieceByPosition(position).isWhite() != white)
                .findFirst();
    }
}
