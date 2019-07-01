package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Rook implements Piece {
    private final boolean white;
    private Position position;

    public Rook(boolean white, Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public boolean moveTo(Position position) {
        this.position = position;
        return true;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        // The bishop can move diagonally as far as he wants but he can't leap over other pieces
        return Stream.of(
                Stream.iterate(position, Position::topNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::bottomNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::leftNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::rightNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull))
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    @Override
    public List<Position> getValidCaptureMoves(Board board) {
        return null;
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public boolean isCaptured() {
        return false;
    }
}
