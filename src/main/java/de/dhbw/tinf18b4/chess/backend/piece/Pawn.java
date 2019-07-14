package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Move;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The pawn implementation of the chess {@link Piece}
 * <p>
 * The pawn moves a single square, but the first time a pawn moves,
 * it has the option of advancing two squares but not jump over an occupied square.
 * He cannot move backwards.
 * <p>
 * Also he is only allowed to capture an enemy diagonally left or right.
 */
public class Pawn implements Piece {
    /**
     * whether the pawn is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the pawn
     */
    @Getter
    @Setter
    private Position position;

    /**
     * Initialize the pawn with a {@link Position} and whether it is white
     *
     * @param white    whether the pawn is white
     * @param position the {@link Position}
     */
    public Pawn(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        Position singleMove = white ? position.topNeighbor() : position.bottomNeighbor();
        Position doubleMove = null;

        // if this pawn has not moved allow moving two fields forward
        // allow a moving two fields at once
        if (hasEverMoved(board.getGame())) {
            if (white) {
                doubleMove = Optional.ofNullable(singleMove)
                        .map(Position::topNeighbor)
                        .orElse(null);
            } else {
                doubleMove = Optional.ofNullable(singleMove)
                        .map(Position::bottomNeighbor)
                        .orElse(null);
            }

            // prevent pawn from jumping over other pieces
            if (singleMove != null && board.isOccupied(singleMove)) {
                doubleMove = null;
            }
        }

        return Stream.concat(Stream.ofNullable(singleMove), Stream.ofNullable(doubleMove))
                .filter(position -> !board.isOccupied(position));
    }

    @NotNull
    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        Stream<Position> capturePositions = getPossibleCaptureMoves()
                .filter(position1 -> Optional.ofNullable(board.findPieceByPosition(position1))
                        .map(piece -> piece.isWhite() != white)
                        .orElse(false));

        return Stream.concat(Stream.ofNullable(calculateEnPassantPossibility(board)), capturePositions);
    }


    /**
     * Get all possible moves of a pawn on a {@link Board chessboard} without considering any other {@link Piece}
     *
     * @return all move possibilities
     */
    @NotNull
    private Stream<Position> getPossibleCaptureMoves() {
        Stream<Position> captureLeft;
        Stream<Position> captureRight;

        // Because white pawns move "up" (towards rank 8) and black pawns move "down"
        // (towards rank 1) we need to check for the color of the piece
        if (white) {
            captureLeft = Stream.ofNullable(position.upperLeftNeighbor());
            captureRight = Stream.ofNullable(position.upperRightNeighbor());
        } else {
            captureLeft = Stream.ofNullable(position.lowerLeftNeighbor());
            captureRight = Stream.ofNullable(position.lowerRightNeighbor());
        }

        return Stream.concat(captureLeft, captureRight);
    }


    /**
     * The pawn has a special capture move called en passant.
     * This move allows a pawn to capture another pawn,
     * if this pawn just moved next to him with a two-square move.
     * <p>
     * This pawn then walks diagonally behind the enemy pawn and the enemy pawn is then considered as captured.
     *
     * @param board the current {@link Board chessboard}
     * @return the {@link Position} the pawn can possibly do an en passant to
     */
    private @Nullable Position calculateEnPassantPossibility(@NotNull Board board) {
        Move lastMove = board.getGame().getHistory().lastMove();

        // If there is no first move en passant is not possible
        if (lastMove == null) {
            return null;
        }

        // only pawns on their fifth rank (which for black pawns is rank 4) can do en passant
        boolean isOnFifthRank = (white ? 5 : 4) == getPosition().getRank();

        Piece enemyPawn = lastMove.getPiece();
        boolean lastMoveWasPawnMove = enemyPawn instanceof Pawn;
        boolean lastMoveWasDoubleMove = 2 == Math.abs(lastMove.getDestination().getRank() - lastMove.getOrigin().getRank());

        // find the intercept position where the en passant capture happens
        // it's behind the pawn to be captured (which was involved in the last move)
        Position enPassantCapturePosition = !white
                ? enemyPawn.getPosition().bottomNeighbor()
                : enemyPawn.getPosition().topNeighbor();

        // find if any of the pawns could capture the enemy pawn if it only moved on square
        boolean enPassantPossible = getPossibleCaptureMoves()
                .anyMatch(position -> position.equals(enPassantCapturePosition));

        // if all of these predicates are true this pawn can do an en passant move
        // so we return the position where it should move to do the en passant move
        return isOnFifthRank
                && lastMoveWasPawnMove
                && lastMoveWasDoubleMove
                && enPassantPossible
                ? enPassantCapturePosition
                : null;
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'P' : 'p';
    }
}
