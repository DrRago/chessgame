package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Move;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Pawn implements Piece {
    private final boolean white;
    @Getter
    @Setter
    private Position position;

    public Pawn(boolean white, Position position) {
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

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        Stream<Position> capturePositions = getPossibleCaptureMoves()
                .filter(position1 -> Optional.ofNullable(board.findPieceByPosition(position1))
                        .map(piece -> piece.isWhite() != white)
                        .orElse(false));

        return Stream.concat(Stream.ofNullable(calculateEnPassantPossibility(board)), capturePositions);
    }


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


    private Position calculateEnPassantPossibility(Board board) {
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
