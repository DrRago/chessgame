package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The knight implementation of the chess {@link Piece}
 * <p>
 * It moves to a square two squares away horizontally and one
 * square vertically, or two squares vertically and one square horizontally.
 * <p>
 * Additionally he can jump over {@link Piece pieces}, meaning he doesn't care about other {@link Piece pieces} on his move
 */
public class Knight implements Piece {
    /**
     * whether the knight is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the knight
     */
    @Getter
    @Setter
    @NotNull
    private Position position;

    /**
     * Initialize the knight with a {@link Position} and whether it is white
     *
     * @param white    whether the knight is white
     * @param position the {@link Position}
     */
    public Knight(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @NotNull
    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        return getPossibleMoves().filter(board::isNotOccupied);
    }

    /**
     * Get all possible moves of a knight on a {@link Board chessboard} without considering any other {@link Piece}
     *
     * @return all move possibilities
     */
    @NotNull
    private Stream<Position> getPossibleMoves() {
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

    @NotNull
    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        return getPossibleMoves()
                // only consider moves to occupied squares
                .filter(board::isOccupied)
                // only enemy pieces can be captured
                .filter(position -> isOwnedByEnemy(board.findPieceByPosition(position)));

    }

    @Override
    public char getFenIdentifier() {
        return white ? 'N' : 'n';
    }

    @Override
    public String toString() {
        return toPieceName();
    }
}
