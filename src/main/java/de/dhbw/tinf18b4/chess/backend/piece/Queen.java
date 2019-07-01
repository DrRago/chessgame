package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Queen implements Piece {
    private final boolean white;
    private Position position;

    public Queen(boolean white, Position position) {
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
        return Stream.of(new Rook(white, position).getValidMoves(board), new Bishop(white, position).getValidMoves(board))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Position> getValidCaptureMoves(Board board) {
        return getValidMoves(board);
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
