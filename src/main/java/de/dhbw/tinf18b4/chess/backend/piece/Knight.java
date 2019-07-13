package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
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
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        return getPossibleMoves().filter(board::isNotOccupied);
    }

    /**
     * Get the moves that are possible on any chessboard without considering other pieces
     *
     * @return the possible moves
     */
    private @NotNull Stream<Position> getPossibleMoves() {
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
                .filter(Objects::nonNull);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        return getPossibleMoves()
                // only consider moves to occupied squares
                .filter(board::isOccupied)
                // only enemy pieces can be captured
                .filter(position -> isOwnedByEnemy(board.findPieceByPosition(position)));

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
