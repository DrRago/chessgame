package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class King implements Piece {
    private final boolean white;
    @Getter
    @Setter
    private Position position;

    public King(boolean white, Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        List<Position> castlingRookPositions = calculateCastlingPossibility(board).collect(Collectors.toList());
        Stream.Builder<Position> castlingMoves = Stream.builder();
        castlingRookPositions.stream()
                // we don't bother checking where the rook is
                // we just find the relative position and add it if it exists
                .map(rook -> Stream.of(
                        Stream.ofNullable(rook.leftNeighbor()).map(Position::leftNeighbor),
                        Stream.ofNullable(rook.rightNeighbor()).map(Position::rightNeighbor))
                        .flatMap(s -> s))
                .flatMap(s -> s)
                .forEach(castlingMoves::accept);

        return Stream.of(
                Stream.ofNullable(position.topNeighbor()),
                Stream.ofNullable(position.bottomNeighbor()),
                Stream.ofNullable(position.leftNeighbor()),
                Stream.ofNullable(position.rightNeighbor()),
                castlingMoves.build())
                .flatMap(s -> s);
    }

    /**
     * Finds the rooks towards which the castling special move can be done
     *
     * @param board the board
     * @return the rooks
     */
    private Stream<Position> calculateCastlingPossibility(Board board) {
        if (isInCheck(board)) {
            return Stream.empty();
        }

        Piece left = board.findPieceByPosition(new Position('a', white ? 1 : 8));
        Piece right = board.findPieceByPosition(new Position('h', white ? 1 : 8));

        int piecesLeft = Math.toIntExact(Stream.iterate(position, Position::leftNeighbor)
                .filter(Objects::nonNull)
                .takeWhile(position -> position.getFile() != 'a')
                .filter(position -> board.findPieceByPosition(position) != null)
                .count());
        int piecesRight = Math.toIntExact(Stream.iterate(position, Position::rightNeighbor)
                .filter(Objects::nonNull)
                .takeWhile(position -> position.getFile() != 'h')
                .filter(position -> board.findPieceByPosition(position) != null)
                .count());

        // find all position which are attackable by an enemy piece
        Supplier<Stream<Position>> capturePositions = () -> board.getPieces()
                // consider only enemy pieces
                .filter(piece -> piece.isWhite() != white)
                // find all the position where they can move in a capture
                .filter(piece -> !(piece instanceof King))
                .map(piece -> piece.getValidCaptureMoves(board))
                .flatMap(s -> s);
        boolean hasToPassThroughAnAttackedSquareLeft = Stream.iterate(position, Position::leftNeighbor)
                .takeWhile(position -> position.getFile() != 'a')
                .noneMatch(position -> capturePositions.get().anyMatch(capturePosition -> capturePosition.equals(position)));
        boolean hasToPassThroughAnAttackedSquareRight = Stream.iterate(position, Position::rightNeighbor)
                .takeWhile(position -> position.getFile() != 'h')
                .noneMatch(position -> capturePositions.get().anyMatch(capturePosition -> capturePosition.equals(position)));

        if (piecesLeft > 1 || hasToPassThroughAnAttackedSquareLeft) {
            left = null;
        }

        if (piecesRight > 1 || hasToPassThroughAnAttackedSquareRight) {
            right = null;
        }

        return Stream.of(
                Stream.ofNullable(left),
                Stream.ofNullable(right))
                .flatMap(s -> s)
                .filter(piece -> !piece.isCaptured())
                .filter(piece -> !piece.hasEverMoved(board.getGame()))
                .filter(piece -> white ? piece.getFenIdentifier() == 'R' : piece.getFenIdentifier() == 'r')
                .map(Piece::getPosition);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
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
        return white ? 'K' : 'k';
    }

    public boolean isInCheck(Board board) {
        // find all position which are attackable by an enemy piece
        Stream<Position> capturePositions = board.getPieces()
                // consider only enemy pieces
                .filter(piece -> piece.isWhite() != white)
                // find all the position where they can move in a capture
                .filter(piece -> !(piece instanceof King))
                .map(piece -> piece.getValidCaptureMoves(board))
                .flatMap(s -> s);

        return capturePositions.anyMatch(position -> position.equals(getPosition()));
    }
}
