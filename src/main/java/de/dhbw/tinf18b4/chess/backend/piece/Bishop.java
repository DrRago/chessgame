package de.dhbw.tinf18b4.chess.backend.piece;


import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
        // The bishop can move diagonally as far as he wants but he can't leap over other pieces
        return Stream.of(
                Stream.iterate(position, Position::upperLeftNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::upperRightNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::lowerLeftNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull),
                Stream.iterate(position, Position::lowerRightNeighbor)
                        .skip(1)
                        .takeWhile(Objects::nonNull))
                .flatMap(s -> s)
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
