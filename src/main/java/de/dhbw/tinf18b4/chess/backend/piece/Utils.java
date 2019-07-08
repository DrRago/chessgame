package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.Objects;
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
                        .takeWhile(board::isOccupied))
                .flatMap(s -> s);
    }
}
