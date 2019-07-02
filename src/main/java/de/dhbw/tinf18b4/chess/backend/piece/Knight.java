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
        return white ? 'N' : 'n';
    }
}
