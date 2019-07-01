package de.dhbw.tinf18b4.chess.backend.piece;

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

    /**
     * Move the piece to the given position. Returns true if the move was valid, false if it wasn't
     *
     * @param position the destination position
     * @return whether the move was made or not
     */
    @Override
    public boolean moveTo(Position position) {
        this.position = position;
        return true;
    }

    /**
     * Get the position of the piece on the board
     *
     * @return where the piece is
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Get a list of all possible moves for the piece. Dies not include kill moves
     *
     * @return a list of all possible moves
     */
    @Override
    public List<Position> getValidMoves() {
        return Stream.of(new Rook(white, position).getValidMoves(), new Bishop(white, position).getValidMoves())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all possible capture options
     *
     * @return all capture options
     */
    @Override
    public List<Position> getValidCaptureMoves() {
        return getValidMoves();
    }

    /**
     * Get the color of the piece
     *
     * @return whether the piece is white or not
     */
    @Override
    public boolean isWhite() {
        return white;
    }

    /**
     * Get the capture state of the piece
     *
     * @return whether the piece has been captured or not
     */
    @Override
    public boolean isCaptured() {
        return false;
    }
}
