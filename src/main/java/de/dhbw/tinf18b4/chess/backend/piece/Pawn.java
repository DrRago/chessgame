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
        return getValidCaptureMoves(board, true);
    }


    public Stream<Position> getValidCaptureMoves(Board board, boolean careAboutEnPassant) {
        if (careAboutEnPassant) {
            Position enPassant = calculateEnPassantPossibility(board);
            if (enPassant != null) {
                return Stream.of(enPassant);
            }
        }

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

        return Stream.concat(captureLeft, captureRight);
    }


    private Position calculateEnPassantPossibility(Board board) {
        Move lastMove = board.getGame().getHistory().lastMove();

        // If there is no first move en passant is not possible
        if (lastMove == null) {
            return null;
        }

        boolean enemyPawnMovedFromStartingPoint = 1 == numberOfMoves(board.getGame());
        boolean enemyPawnMovedTwoSquares = 2 == Math.abs(lastMove.getDestination().getFile() - lastMove.getOrigin().getFile());

        // find the intercept position where the en passant capture happens
        // it's behind the pawn to be captured (which was involved in the last move)
        Position enPassantCapturePosition = !white
                ? lastMove.getPiece().getPosition().bottomNeighbor()
                : lastMove.getPiece().getPosition().topNeighbor();

        // find if any of the pawns could capture the enemy pawn if it only moved on square
        boolean enPassantPossible = getValidCaptureMoves(board, false)
                .anyMatch(position -> position.equals(enPassantCapturePosition));

        // if all of these predicates are true this pawn can do an en passant move
        // so we return the position where it should move to do the en passant move
        return enemyPawnMovedFromStartingPoint
                && enemyPawnMovedTwoSquares
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
