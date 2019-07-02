package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
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
        Position singleMove = white ? position.topNeighbor() : position.bottomNeighbor();
        Position doubleMove = null;

        // if this pawn has not moved allow moving two fields forward
        if (white && position.getRank() == 2
                || !white && position.getRank() == 7) {
            if (white) {
                doubleMove = Optional.ofNullable(singleMove)
                        .map(Position::topNeighbor)
                        .orElse(null);
            } else {
                doubleMove = Optional.ofNullable(singleMove)
                        .map(Position::bottomNeighbor)
                        .orElse(null);
            }
        }

        return Stream.concat(Stream.ofNullable(singleMove), Stream.ofNullable(doubleMove))
                .collect(Collectors.toList());
    }

    @Override
    public List<Position> getValidCaptureMoves(Board board) {
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
        return white ? 'P' : 'p';
    }
}
