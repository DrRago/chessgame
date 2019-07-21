package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The king implementation of the chess {@link Piece}
 * <p>
 * It moves one square in any direction (horizontally, vertically, or diagonally)
 * <p>
 * Additionally he can perform the special move <a href="https://en.wikipedia.org/wiki/Castling">castling</a>
 * with a rook. Castling consists of moving the king two squares towards
 * a rook on the player's first rank, then moving the rook to the square
 * over which the king crossed. Castling may only be done if the king has
 * never moved, the rook involved has never moved, the squares between the
 * king and the rook involved are unoccupied, the king is not in check, and
 * the king does not cross over or end on a square attacked by an enemy piece.
 */
public class King implements Piece {
    /**
     * whether the king is white or not
     */
    @Getter
    private final boolean white;

    /**
     * The current {@link Position} of the king
     */
    @Getter
    @Setter
    @NotNull
    private Position position;

    /**
     * Initialize the king with a {@link Position} and whether it is white
     *
     * @param white    whether the king is white
     * @param position the {@link Position}
     */
    public King(boolean white, @NotNull Position position) {
        this.white = white;
        this.position = position;
    }

    @NotNull
    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        Stream<Position> castlingMoves = calculateCastlingPossibility(board);
        return Stream.concat(castlingMoves, getPossibleMoves())
                .filter(board::isNotOccupied);
    }

    /**
     * Get the moves that are possible on any chessboard without considering other pieces
     *
     * @return the possible moves
     */
    @NotNull
    private Stream<Position> getPossibleMoves() {
        return Stream.of(
                Stream.ofNullable(position.topNeighbor()),
                Stream.ofNullable(position.bottomNeighbor()),
                Stream.ofNullable(position.leftNeighbor()),
                Stream.ofNullable(position.rightNeighbor()),
                Stream.ofNullable(position.upperRightNeighbor()),
                Stream.ofNullable(position.upperLeftNeighbor()),
                Stream.ofNullable(position.lowerRightNeighbor()),
                Stream.ofNullable(position.lowerLeftNeighbor()))
                .flatMap(s -> s);
    }

    /**
     * Finds the rooks towards which the castling special move can be done
     *
     * @param board the board
     * @return the rooks
     */
    @NotNull
    private Stream<Position> calculateCastlingPossibility(@NotNull Board board) {
        if (isInCheck(board) || hasEverMoved(board.getGame())) {
            return Stream.empty();
        }

        Piece left = board.findPieceByPosition(new Position('a', white ? 1 : 8));
        Piece right = board.findPieceByPosition(new Position('h', white ? 1 : 8));

        if (left == null && right == null) {
            return Stream.empty();
        } else if (!(left instanceof Rook)) {
            left = null;
        } else if (!(right instanceof Rook)) {
            right = null;
        }

        if (left != null && left.hasEverMoved(board.getGame())) {
            left = null;
        } else if (right != null && right.hasEverMoved(board.getGame())) {
            right = null;
        }

        // count how many pieces are left of the king including the king
        long piecesLeft = Stream.iterate(position, Position::leftNeighbor)
                .takeWhile(position -> position.getFile() != 'a')
                .filter(position -> board.findPieceByPosition(position) != null)
                .count();
        // count how many pieces are right of the king including the king
        long piecesRight = Stream.iterate(position, Position::rightNeighbor)
                .takeWhile(position -> position.getFile() != 'h')
                .filter(position -> board.findPieceByPosition(position) != null)
                .count();

        if (piecesLeft > 1) {
            left = null;
        } else if (piecesRight > 1) {
            right = null;
        }

        // find all position which are attackable by an enemy piece
        Supplier<Stream<Position>> capturePositions = () -> board.getPieces()
                // consider only enemy pieces
                .filter(this::isOwnedByEnemy)
                // find all the position where they can move in a capture
                .filter(piece -> !(piece instanceof King))
                .map(piece -> piece.getValidCaptureMoves(board))
                .flatMap(s -> s);

        boolean hasToPassThroughAnAttackedSquareLeft = Stream.iterate(position, Position::leftNeighbor)
                .takeWhile(position -> position.getFile() != 'a')
                .anyMatch(position -> capturePositions.get().anyMatch(position::equals));
        boolean hasToPassThroughAnAttackedSquareRight = Stream.iterate(position, Position::rightNeighbor)
                .takeWhile(position -> position.getFile() != 'h')
                .anyMatch(position -> capturePositions.get().anyMatch(position::equals));

        if (hasToPassThroughAnAttackedSquareLeft) {
            left = null;
        } else if (hasToPassThroughAnAttackedSquareRight) {
            right = null;
        }

        Stream<Position> newKingPositionLeft = left == null
                ? Stream.empty()
                : Optional.of(getPosition())
                .map(Position::leftNeighbor)
                .map(Position::leftNeighbor)
                .stream();

        Stream<Position> newKingPositionRight = right == null
                ? Stream.empty()
                : Optional.of(getPosition())
                .map(Position::rightNeighbor)
                .map(Position::rightNeighbor)
                .stream();

        return Stream.concat(newKingPositionLeft, newKingPositionRight);
    }

    @NotNull
    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        //noinspection ConstantConditions
        return getPossibleMoves()
                // only consider moves to occupied squares
                .filter(board::isOccupied)
                // only enemy pieces can be captured
                .filter(position -> isOwnedByEnemy(board.findPieceByPosition(position)));
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'K' : 'k';
    }

    /**
     * Check whether the king is in check or not,
     * meaning whether any enemy piece may move on the king's {@link Position}
     *
     * @param board the current {@link Board chessboard}
     * @return whether the king is in check ot not
     */
    public boolean isInCheck(@NotNull Board board) {
        // find all position which are attackable by an enemy piece
        Stream<Position> capturePositions = board.getPieces()
                // consider only enemy pieces
                .filter(this::isOwnedByEnemy)
                // find all the position where they can move in a capture
                .map(piece -> piece.getValidCaptureMoves(board))
                .flatMap(s -> s);

        return capturePositions.anyMatch(position -> position.equals(getPosition()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof King)) return false;
        Piece piece = (Piece) obj;
        return piece.getPosition() == position && piece.isWhite() == white;
    }

    @Override
    public String toString() {
        return toPieceName();
    }
}
