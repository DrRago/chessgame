package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;
import java.util.stream.Collectors;

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
        // The rook can move vertically or horizontally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        return Utils.directionalIterator(position, board,
                Position::topNeighbor,
                Position::bottomNeighbor,
                Position::leftNeighbor,
                Position::rightNeighbor)
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

    @Override
    public char getFenIdentifier() {
        return white ? 'R' : 'r';
    }
}
