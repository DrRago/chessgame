package de.dhbw.tinf18b4.chess.backend.piece;


import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Bishop implements Piece {
    private final boolean white;
    private Position position;

    public Bishop(boolean white, Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        // The bishop can move diagonally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        return Utils.directionalIterator(position, board,
                Position::upperLeftNeighbor,
                Position::upperRightNeighbor,
                Position::lowerLeftNeighbor,
                Position::lowerRightNeighbor);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
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
        return white ? 'B' : 'b';
    }
}
