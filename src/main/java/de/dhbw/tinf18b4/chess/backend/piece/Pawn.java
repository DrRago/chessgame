package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Pawn implements Piece {
    private final boolean white;
    private Position position;

    public Pawn(boolean white, Position position) {
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
        Position singleMove = position.topNeighbor();
        Position doubleMove = null;

        // if this pawn has not moved allow moving two fields forward
        if (white && position.getRank() == 2
                || !white && position.getRank() == 7) {
            doubleMove = Optional.ofNullable(singleMove)
                    .map(Position::topNeighbor)
                    .orElse(null);
        }

        return Stream.concat(Stream.ofNullable(singleMove), Stream.ofNullable(doubleMove))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all possible capture options
     *
     * @return all capture options
     */
    @Override
    public List<Position> getValidCaptureMoves() {
        // TODO: 01.07.2019 Implement en passant special move
        Stream<Position> captureLeft;
        Stream<Position> captureRight;

        // Because white pawns move "up" (towards file 8) and black pawns move "down"
        // (towards file 1) we need to check for the color of the piece
        if (white) {
            captureLeft = Stream.ofNullable(position.upperLeftNeighbor());
            captureRight = Stream.ofNullable(position.upperRightNeighbor());
        } else {
            captureLeft = Stream.ofNullable(position.lowerLeftNeighbor());
            captureRight = Stream.ofNullable(position.lowerRightNeighbor());
        }

        return Stream.concat(captureLeft, captureRight)
                .collect(Collectors.toList());
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
