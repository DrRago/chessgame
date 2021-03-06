package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.moves.CastlingMove;
import de.dhbw.tinf18b4.chess.backend.moves.EnPassantMove;
import de.dhbw.tinf18b4.chess.backend.moves.Move;
import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
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

    @Override
    public String toString() {
        char[][] board = new char[8][8];
        getPieces()
                .filter(Objects::nonNull)
                .forEach(piece -> {
                    int y = piece.getPosition().getRank() - 1;
                    int x = piece.getPosition().getFile() - 'a';

                    board[y][x] = piece.getFenIdentifier();
                });

        String whiteSquare = "???";
        String blackSquare = "???";
        String whiteKing = "???";
        String whiteQueen = "???";
        String whiteRook = "???";
        String whiteBishop = "???";
        String whiteKnight = "???";
        String whitePawn = "???";
        String blackKing = "???";
        String blackQueen = "???";
        String blackRook = "???";
        String blackBishop = "???";
        String blackKnight = "???";
        String blackPawn = "???";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]) {
                    case 'P':
                        stringBuilder.append(whitePawn);
                        break;
                    case 'K':
                        stringBuilder.append(whiteKing);
                        break;
                    case 'Q':
                        stringBuilder.append(whiteQueen);
                        break;
                    case 'N':
                        stringBuilder.append(whiteKnight);
                        break;
                    case 'R':
                        stringBuilder.append(whiteRook);
                        break;
                    case 'B':
                        stringBuilder.append(whiteBishop);
                        break;
                    case 'p':
                        stringBuilder.append(blackPawn);
                        break;
                    case 'k':
                        stringBuilder.append(blackKing);
                        break;
                    case 'q':
                        stringBuilder.append(blackQueen);
                        break;
                    case 'n':
                        stringBuilder.append(blackKnight);
                        break;
                    case 'r':
                        stringBuilder.append(blackRook);
                        break;
                    case 'b':
                        stringBuilder.append(blackBishop);
                        break;
                    default:
                        stringBuilder.append((i + j) % 2 == 0 ? blackSquare : whiteSquare);
                        stringBuilder.append('???');
                }
            }

            stringBuilder.append("\n");
        }

        return "Board{\n" + stringBuilder.toString() + "}";
    }

    /**
     * Check whether a move is allowed on this board
     * <p>
     *
     * @param move The move
     * @return whether is possible to make the move
     */
    synchronized boolean checkMove(@NotNull Move move) {
        // a piece is captured if it doesn't exist on this board anymore
        boolean isCaptured = getPieces().noneMatch(piece -> piece.equals(move.getPiece()));
        boolean isAllowedMovement = getAllPossibleMoves().stream()
                .anyMatch(map -> map.entrySet().stream()
                        .filter(Objects::nonNull)
                        .filter(entry -> entry.getKey().equals(move.getPiece()))
                        .map(Map.Entry::getValue)
                        .anyMatch(positions -> positions.anyMatch(p -> p.equals(move.getDestination()))
                        )
                );

        return !isCaptured && isAllowedMovement;
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
    private Stream<Position> getOccupiedPositions() {
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
     * @return the index of the removed piece
     */
    private synchronized int removePiece(@Nullable Piece toRemove) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] != null && pieces[i].equals(toRemove)) {
                pieces[i] = null;
                return i;
            }
        }
        return -1;
    }

    /**
     * Apply a move to the board
     *
     * @param move The move
     */
    synchronized void applyMove(@NotNull Move move) {
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
        } else if (move instanceof EnPassantMove) {
            EnPassantMove enPassantMove = (EnPassantMove) move;
            removePiece(enPassantMove.getCapturedPawn());
        } else if (move instanceof CastlingMove) {
            CastlingMove castlingMove = (CastlingMove) move;
            castlingMove.getRook().setPosition(castlingMove.getRookDestination());
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

        try {
            return new CastlingMove(player, origin, destination, this);
        } catch (Exception ignored) {

        }

        try {
            return new EnPassantMove(player, origin, destination, this);
        } catch (Exception ignored) {

        }

        return new Move(player, origin, destination, this);
    }

    /**
     * Build the {@link Move} according to a valid {@link Move} format defined in {@link #checkMoveFormat(String)}
     *
     * @param player the player who performed the move
     * @return the move
     */
    @NotNull
    public Move buildMove(@NotNull Player player, @NotNull Position origin, @NotNull Position destination) {
        try {
            return new CastlingMove(player, origin, destination, this);
        } catch (Exception ignored) {

        }

        try {
            return new EnPassantMove(player, origin, destination, this);
        } catch (Exception ignored) {

        }

        return new Move(player, origin, destination, this);
    }

    /**
     * Get all possible moves and capture moves in a list with two maps,
     * the first is always the map with the moves and the second is
     * always the map with the capture moves.
     * <p>
     * To prevent interferes with multithreaded methods the moves gets
     * applied on a temporary board which is an exact copy of this board
     *
     * @return the list with the maps with all possible moves
     */
    @NotNull
    public synchronized List<Map<Piece, Stream<Position>>> getAllPossibleMoves() {
        Board tempBoard = this.copy();
        // differentiate normal moves and capture moves for performance reasons
        Map<Piece, Stream<Position>> moveMap = new HashMap<>();
        Map<Piece, Stream<Position>> captureMoveMap = new HashMap<>();
        tempBoard.getPieces()
                .filter(piece -> tempBoard.getGame().whoseTurn().isWhite() ? piece.isWhite() : piece.isBlack())
                .forEach(piece -> {
                            captureMoveMap.put(piece, piece.getValidCaptureMoves(tempBoard));
                            moveMap.put(piece, piece.getValidMoves(tempBoard));
                        }
                );

        // filter whether the king would be in check after this move is applied
        moveMap.entrySet()
                .forEach(entry -> entry.setValue(entry.getValue().filter(position -> {
                    // get the current position to reapply
                    Position prev = entry.getKey().getPosition();
                    entry.getKey().setPosition(position);

                    // check if the king is in check
                    King king = entry.getKey().isWhite() ? tempBoard.getWhiteKing() : tempBoard.getBlackKing();
                    boolean result = king == null || !king.isInCheck(tempBoard);

                    // reset the position
                    entry.getKey().setPosition(prev);

                    return result;
                })));
        captureMoveMap.entrySet()
                .forEach(entry -> entry.setValue(entry.getValue().filter(position -> {
                    // get the current position to reapply
                    Position prev = entry.getKey().getPosition();
                    Piece capturePiece = tempBoard.findPieceByPosition(position);
                    entry.getKey().setPosition(position);

                    // check whether the king is in check
                    int i = tempBoard.removePiece(capturePiece);
                    if (i < 0) {
                        Position enPassant = entry.getKey().getBackwardsPosition();
                        capturePiece = tempBoard.findPieceByPosition(enPassant);
                        i = tempBoard.removePiece(capturePiece);
                    }
                    King king = entry.getKey().isWhite() ? tempBoard.getWhiteKing() : tempBoard.getBlackKing();
                    boolean result = king == null || !king.isInCheck(tempBoard);

                    // reapply the captured piece and the moved piece's position
                    entry.getKey().setPosition(prev);
                    tempBoard.pieces[i] = capturePiece;

                    return result;
                })));

        return List.of(moveMap, captureMoveMap);
    }

    /**
     * Create a copy of this board and reinitialize all pieces with the same position
     *
     * @return a copy of this board
     */
    @NotNull
    private Board copy() {
        Piece[] pieces = getPieces().map(piece -> {
            try {
                return (Piece) piece.getClass().getDeclaredConstructors()[0].newInstance(piece.isWhite(), piece.getPosition());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }).toArray(Piece[]::new);

        return new Board(game, pieces);
    }
}
