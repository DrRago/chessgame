package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class King implements Piece {
    private final boolean white;
    private Position position;

    public King(boolean white, Position position) {
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
    public Stream<Position> getValidMoves(Board board) {
        // TODO: 01.07.2019 Implement castling special move
        return Stream.of(
                Stream.ofNullable(position.topNeighbor()),
                Stream.ofNullable(position.bottomNeighbor()),
                Stream.ofNullable(position.leftNeighbor()),
                Stream.ofNullable(position.rightNeighbor()))
                .flatMap(s -> s);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(Board board) {
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

    @Override
    public char getFenIdentifier() {
        return white ? 'K' : 'k';
    }
}
