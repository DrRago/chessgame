package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.stream.Stream;

class Board {
    final private Piece[] pieces = initialSetup();

    /**
     * Create an array with pieces at their initial positions
     *
     * @return the pieces
     */
    private Piece[] initialSetup() {
        return new Piece[]{
                // white pieces
                new Pawn(true, new Position('a', 2)),
                new Pawn(true, new Position('b', 2)),
                new Pawn(true, new Position('c', 2)),
                new Pawn(true, new Position('d', 2)),
                new Pawn(true, new Position('e', 2)),
                new Pawn(true, new Position('f', 2)),
                new Pawn(true, new Position('g', 2)),
                new Rook(true, new Position('a', 1)),
                new Rook(true, new Position('h', 1)),
                new Knight(true, new Position('b', 1)),
                new Knight(true, new Position('g', 1)),
                new Bishop(true, new Position('c', 1)),
                new Bishop(true, new Position('f', 1)),
                new King(true, new Position('d', 1)),
                new Queen(true, new Position('e', 1)),

                // black pieces
                new Pawn(false, new Position('a', 7)),
                new Pawn(false, new Position('b', 7)),
                new Pawn(false, new Position('c', 7)),
                new Pawn(false, new Position('d', 7)),
                new Pawn(false, new Position('e', 7)),
                new Pawn(false, new Position('f', 7)),
                new Pawn(false, new Position('g', 7)),
                new Rook(false, new Position('a', 7)),
                new Rook(false, new Position('h', 7)),
                new Knight(false, new Position('b', 8)),
                new Knight(false, new Position('g', 8)),
                new Bishop(false, new Position('c', 8)),
                new Bishop(false, new Position('f', 8)),
                new King(false, new Position('d', 8)),
                new Queen(false, new Position('e', 8))
        };
    }

    /**
     * Check whether a move is allowed on this board
     * <p>
     * TODO: Implement
     *
     * @param move The move
     * @return whether is possible to make the move
     */
    boolean checkMove(Move move) {
        boolean isCaptured = move.getPiece().isCaptured();
        boolean isAllowedMovement = move.getPiece()
                .getValidMoves()
                .stream()
                .anyMatch(position -> position.equals(move.getDestination()));
        boolean isAllowedCaptureMove = move.getPiece()
                .getValidCaptureMoves()
                .stream()
                .anyMatch(position -> position.equals(move.getDestination()));
        boolean isEmptyField = getOccupiedPositions()
                .noneMatch(position -> position.equals(move.getDestination()));

        return !isCaptured
                && isEmptyField
                && (isAllowedMovement
                || isAllowedCaptureMove);
    }

    /**
     * Returns all the pieces on this board
     *
     * @return The pieces
     */
    Stream<Piece> getPieces() {
        return Stream.of(pieces);
    }

    /**
     * Returns all the positions which are occupied by a piece on this board
     *
     * @return The positions
     */
    Stream<Position> getOccupiedPositions() {
        return getPieces().map(Piece::getPosition);
    }

    /**
     * Apply a move to the board
     *
     * @param move The move
     */
    void applyMove(Move move) {
        getPieces()
                .filter(piece -> piece.equals(move.getPiece()))
                .findFirst()
                .orElseThrow()
                .moveTo(move.getDestination());
    }
}
