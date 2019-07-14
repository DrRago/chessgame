package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * The board represents the status of the pieces and manages the {@link Move moves} on the it
 */
public class Board {
    /**
     * A representation of the current {@link Piece pieces} placed on the board. <br>
     * The {@link Position positions} are stored in the {@link Piece pieces} itself,
     * so the array could have null values or less than 32 entries
     */
    final private Piece[] pieces;
    /**
     * A reference back to the {@link Game} object of this board
     */
    @NotNull
    @Getter
    final private Game game;

    /**
     * The white {@link King}
     */
    @Nullable
    @Getter
    King whiteKing = null;
    /**
     * The black {@link King}
     */
    @Nullable
    @Getter
    King blackKing = null;

    /**
     * Create a new board instance with the initial setup of a chessgame
     *
     * @param game the {@link Game} of the board
     */
    public Board(@NotNull Game game) {
        this(game, initialSetup());
    }

    /**
     * Create a new board instance with a custom {@link Piece} layout
     *
     * @param game   the {@link Game} of this board
     * @param pieces the custom {@link Piece} layout
     */
    public Board(@NotNull Game game, @NotNull Piece[] pieces) {
        this.pieces = pieces;
        this.game = game;

        for (Piece piece : pieces) {
            if (piece instanceof King) {
                if (piece.isWhite()) {
                    whiteKing = (King) piece;
                } else {
                    blackKing = (King) piece;
                }
            }
        }
    }

    /**
     * check if the {@link Move} format is correct
     * <p>
     * a {@link Move} should be: '{source}-{target}' e.g.: 'a1-a2'
     *
     * @param move the {@link Move} string defined as above
     * @return whether the format is valid or not
     */
    public static boolean checkMoveFormat(@Nullable String move) {
        if (move == null) {
            return false;
        }
        String[] moveArray = move.split("-");
        Stream<String> moveStream = Arrays.stream(moveArray);
        if (move.length() != 5 || moveArray.length != 2) {
            return false;
        }

        return moveStream.allMatch(m -> {
            if (m.length() != 2) {
                return false;
            }
            char file = m.charAt(0);
            int rank = Character.getNumericValue(m.charAt(1));

            return file >= 'a' && file <= 'h' && rank >= 1 && rank <= 8;
        });
    }

    /**
     * Create an array with pieces at their initial positions
     *
     * @return the pieces
     */
    @NotNull
    private static Piece[] initialSetup() {
        return new Piece[]{
                // white pieces
                new Pawn(true, new Position('a', 2)),
                new Pawn(true, new Position('b', 2)),
                new Pawn(true, new Position('c', 2)),
                new Pawn(true, new Position('d', 2)),
                new Pawn(true, new Position('e', 2)),
                new Pawn(true, new Position('f', 2)),
                new Pawn(true, new Position('g', 2)),
                new Pawn(true, new Position('h', 2)),
                new Rook(true, new Position('a', 1)),
                new Rook(true, new Position('h', 1)),
                new Knight(true, new Position('b', 1)),
                new Knight(true, new Position('g', 1)),
                new Bishop(true, new Position('c', 1)),
                new Bishop(true, new Position('f', 1)),
                new King(true, new Position('e', 1)),
                new Queen(true, new Position('d', 1)),

                // black pieces
                new Pawn(false, new Position('a', 7)),
                new Pawn(false, new Position('b', 7)),
                new Pawn(false, new Position('c', 7)),
                new Pawn(false, new Position('d', 7)),
                new Pawn(false, new Position('e', 7)),
                new Pawn(false, new Position('f', 7)),
                new Pawn(false, new Position('g', 7)),
                new Pawn(false, new Position('h', 7)),
                new Rook(false, new Position('a', 8)),
                new Rook(false, new Position('h', 8)),
                new Knight(false, new Position('b', 8)),
                new Knight(false, new Position('g', 8)),
                new Bishop(false, new Position('c', 8)),
                new Bishop(false, new Position('f', 8)),
                new King(false, new Position('e', 8)),
                new Queen(false, new Position('d', 8))
        };
    }

    /**
     * Check whether a move is allowed on this board
     * <p>
     *
     * @param move The move
     * @return whether is possible to make the move
     */
    boolean checkMove(@NotNull Move move) {
        // a piece is captured if it doesn't exist on this board anymore
        boolean isCaptured = getPieces().noneMatch(piece -> piece.equals(move.getPiece()));
        boolean isAllowedMovement = move.getPiece()
                .getValidMoves(this)
                .anyMatch(position -> position.equals(move.getDestination()));
        boolean isAllowedCaptureMove = move.getPiece()
                .getValidCaptureMoves(this)
                .anyMatch(position -> position.equals(move.getDestination()));

        return !isCaptured
                && (isAllowedMovement
                || isAllowedCaptureMove);
    }

    /**
     * Returns all the pieces on this board
     *
     * @return The pieces
     */
    @NotNull
    public Stream<Piece> getPieces() {
        return Stream.of(pieces).filter(Objects::nonNull);
    }

    /**
     * Returns all the positions which are occupied by a piece on this board
     *
     * @return The positions
     */
    @NotNull
    Stream<Position> getOccupiedPositions() {
        return getPieces().map(Piece::getPosition);
    }

    /**
     * Determine whether there is a piece on the provided position
     *
     * @param position The position to check
     * @return true if the position is occupied
     */
    public boolean isOccupied(@NotNull Position position) {
        return getOccupiedPositions().anyMatch(position::equals);
    }

    /**
     * Determine whether there is no piece on the provided position
     *
     * @param position The position to check
     * @return true if the position is not occupied
     */
    public boolean isNotOccupied(@NotNull Position position) {
        return getOccupiedPositions().noneMatch(position::equals);
    }

    /**
     * Find the piece on the given position
     *
     * @param position The position
     * @return The found piece or null if there is none
     */
    @Nullable
    public Piece findPieceByPosition(@NotNull Position position) {
        return getPieces().filter(piece -> piece.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    /**
     * set a specific piece of the game as null
     *
     * @param toRemove the piece to set as null on the board
     */
    private void removePiece(@NotNull Piece toRemove) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == toRemove) {
                pieces[i] = null;
            }
        }
    }

    /**
     * Apply a move to the board
     *
     * @param move The move
     */
    void applyMove(@NotNull Move move) {
        Piece movedPiece = getPieces()
                .filter(piece -> piece.equals(move.getPiece()))
                .findFirst()
                .orElseThrow();
        Piece target = findPieceByPosition(move.getDestination());
        if (target != null) {
            removePiece(target);
        }
        movedPiece.setPosition(move.getDestination());

        // transform a pawn to queen if he reached the enemy site
        if (movedPiece instanceof Pawn && movedPiece.getPosition().getRank() == (movedPiece.isWhite() ? 8 : 1)) {
            Queen newPiece = new Queen(movedPiece.isWhite(), movedPiece.getPosition());
            for (int i = 0; i < pieces.length; i++) {
                if (pieces[i] == movedPiece) {
                    pieces[i] = newPiece;
                }
            }
        }
    }

    /**
     * Build the {@link Move} according to a valid {@link Move} format defined in {@link #checkMoveFormat(String)}
     *
     * @param move   the string representation of the move
     * @param player the player who performed the move
     * @return the move
     */
    @NotNull
    public Move buildMove(@NotNull String move, @NotNull Player player) {
        String[] moveArray = move.split("-");
        Position origin = new Position(moveArray[0]);
        Position destination = new Position(moveArray[1]);

        return new Move(player, origin, destination, this);
    }

    /**
     * Get all possible moves and capture moves in a list with two maps,
     * the first is always the map with the moves and the second is
     * always the map with the capture moves
     * <p>
     * TODO filter moves that would get the king into check
     *
     * @return the list with the maps with all possible moves
     */
    @NotNull
    public List<Map<Piece, Stream<Position>>> getAllPossibleMoves() {
        List<Map<Piece, Stream<Position>>> returnList = new ArrayList<>();

        Map<Piece, Stream<Position>> moveMap = new HashMap<>();
        Map<Piece, Stream<Position>> captureMoveMap = new HashMap<>();
        getPieces().forEach(piece -> {
                    captureMoveMap.put(piece, piece.getValidCaptureMoves(this));
                    moveMap.put(piece, piece.getValidMoves(this));
                }
        );

        returnList.add(moveMap);
        returnList.add(captureMoveMap);

        return returnList;
    }
}
