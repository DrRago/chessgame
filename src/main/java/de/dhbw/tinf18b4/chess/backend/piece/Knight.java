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
public class Knight implements Piece {
    private final boolean white;
    private Position position;

    public Knight(boolean white, Position position) {
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
     * @param board
     */
    @Override
    public List<Position> getValidMoves(Board board) {
        // Knights always move 3 squares. First they 2 vertical or horizontal and then they make a turn.
        // Afterwards they move one square orthogonally.
        Position topTurningPoint = Optional.ofNullable(position.topNeighbor())
                .map(Position::topNeighbor)
                .orElse(null);

        Position leftTurningPoint = Optional.ofNullable(position.leftNeighbor())
                .map(Position::leftNeighbor)
                .orElse(null);

        Position rightTurningPoint = Optional.ofNullable(position.rightNeighbor())
                .map(Position::rightNeighbor)
                .orElse(null);

        Position bottomTurningPoint = Optional.ofNullable(position.bottomNeighbor())
                .map(Position::bottomNeighbor)
                .orElse(null);

        return Stream.of(
                Stream.ofNullable(topTurningPoint)
                        .map(Position::leftNeighbor),
                Stream.ofNullable(topTurningPoint)
                        .map(Position::rightNeighbor),

                Stream.ofNullable(bottomTurningPoint)
                        .map(Position::leftNeighbor),
                Stream.ofNullable(bottomTurningPoint)
                        .map(Position::rightNeighbor),

                Stream.ofNullable(leftTurningPoint)
                        .map(Position::topNeighbor),
                Stream.ofNullable(leftTurningPoint)
                        .map(Position::bottomNeighbor),

                Stream.ofNullable(rightTurningPoint)
                        .map(Position::topNeighbor),
                Stream.ofNullable(rightTurningPoint)
                        .map(Position::bottomNeighbor))
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all possible capture options
     *
     * @return all capture options
     * @param board
     */
    @Override
    public List<Position> getValidCaptureMoves(Board board) {
        return getValidMoves(board);
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
