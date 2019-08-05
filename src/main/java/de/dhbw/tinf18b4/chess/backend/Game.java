package de.dhbw.tinf18b4.chess.backend;


import de.dhbw.tinf18b4.chess.backend.moves.Move;
import de.dhbw.tinf18b4.chess.backend.piece.Bishop;
import de.dhbw.tinf18b4.chess.backend.piece.King;
import de.dhbw.tinf18b4.chess.backend.piece.Knight;
import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.states.GameState;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The Game is the main entrance point for someone to use the backend of this chess game.<br>
 * This class provides the functionality to play the game and provides the {@link Board}
 * which is used for operations like building a {@link Move}.<br>
 * The Game class also provides checks if the game is over (checkmate or draw)
 * and an export of the current game state as
 * <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN</a> string
 */

public class Game {
    /**
     * The {@link Board} instance modeling the state of the Game
     */
    @NotNull
    @Getter
    private final Board board;
    /**
     * The move {@link History} containing all applied moves
     */
    @Getter
    private final History history = new History();

    /**
     * {@link Player} 1 of the game
     */
    private final Player player1;
    /**
     * {@link Player} 2 of the game
     */
    private final Player player2;

    /**
     * Initialize a game with the default {@link Board} layout
     *
     * @param player1 {@link Player} 1
     * @param player2 {@link Player} 2
     */
    public Game(@NotNull Player player1, @NotNull Player player2) {
        board = new Board(this);
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Initialize the game with a custom {@link Board} layout
     *
     * @param player1 {@link Player} 1
     * @param player2 {@link Player} 2
     * @param pieces  the {@link Board} layout
     */
    public Game(@NotNull Player player1, @NotNull Player player2, @NotNull Piece[] pieces) {
        board = new Board(this, pieces);
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Return whether the color of a square is white or not (has nothing to do with the {@link Player players} color)
     *
     * @param p the {@link Position} to check
     * @return whether the square is white or not
     */
    private static boolean isWhiteSquare(@NotNull Position p) {
        return (p.getFile() * 35 + p.getRank()) % 2 == 0;
    }

    /**
     * Attempt to perform a move on the board. Returns an optional.
     * If a move could be made the optional contains the resulting state of the game.
     * Otherwise the move was deemed invalid and the state of the game doesn't change
     * which will be indicated by a empty value.
     *
     * @param move The move
     * @return an optional
     */
    public Optional<GameState> makeMove(@NotNull Move move) {
        Move lastMove = history.lastMove();

        // Prevent player from making a move if they ...

        // ... don't belong in this game, ...
        if (!(player1.equals(move.getPlayer())
                || player2.equals(move.getPlayer()))) {
            return Optional.empty();

        }

        // ... is not the white player when making the first move or ...
        if (lastMove == null && !move.getPlayer().isWhite()) {
            return Optional.empty();
        }

        // ... have just made a move
        if (lastMove != null && move.getPlayer().equals(lastMove.getPlayer())) {
            return Optional.empty();
        }

        // Otherwise check if their move is possible
        // and apply the move eventually
        if (board.checkMove(move)) {
            board.applyMove(move);

            history.addMove(move);

            return Optional.of(evaluateGame());
        }

        return Optional.empty();
    }

    /**
     * Get the {@link Player} who's up to turn
     *
     * @return the {@link Player}
     */
    @NotNull
    public Player whoseTurn() {
        Move lastMove = history.lastMove();

        // it's whites turn
        if (lastMove == null) {
            return player1.isWhite() ? player1 : player2;
        }

        // the player who hasn't moved last
        return lastMove.getPlayer().equals(player1) ? player2 : player1;
    }

    /**
     * convert the {@link Board} to a FEN string <br>
     * this string only contains information about the {@link Position} of the {@link Piece}
     *
     * @return the FEN string
     * @see <a href='https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation'>FEN Wikipedia</a>
     */
    @NotNull
    public String asFen() {
        StringBuilder stringBuilder = new StringBuilder();

        // build a 8x8 board with pieces as char name
        char[][] fenBoard = new char[8][8];
        board.getPieces()
                .filter(Objects::nonNull)
                .forEach(piece -> {
                    int y = piece.getPosition().getRank() - 1;
                    int x = piece.getPosition().getFile() - 'a';

                    fenBoard[y][x] = piece.getFenIdentifier();
                });

        // iterate backwards from 8 to 1
        for (int y = 7; y >= 0; y--) {
            char[] row = fenBoard[y];
            int counter = 0; // counter for empty fields
            for (char c : row) {
                if (c != 0) {
                    if (counter != 0) {
                        // if a row contains empty fields and pieces
                        stringBuilder.append(counter);
                        counter = 0;
                    }
                    stringBuilder.append(c);
                } else {
                    counter++;
                }
            }

            if (counter != 0) {
                stringBuilder.append(counter);
            }
            stringBuilder.append("/");
        }

        // remove the last "/"
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * Check if the game is a draw, has been won or neither. The following draw rules are implemented:
     * <ul>
     * <li>stalemate</li>
     * <li>insufficient material</li>
     * </ul>
     * <p>
     *
     * @return whether the game is a draw or not
     */
    public GameState evaluateGame() {
        GameState.winner = isCheckmate();
        if (GameState.winner != null) return GameState.WON;

        Supplier<Stream<Piece>> pieces = () -> getBoard().getPieces().filter(Objects::nonNull);

        // draw by insufficient material is when one of these combinations occur
        final boolean onlyKingAndBishop = pieces.get().allMatch(piece -> piece instanceof King || piece instanceof Bishop);

        // king vs king
        final long piecesLeft = pieces.get().count();
        final boolean isKingVsKing = piecesLeft == 2;
        if (isKingVsKing) {
            return GameState.KING_VS_KING;
        }

        // king and bishop vs king
        final boolean isKingBishopVsKing = onlyKingAndBishop && piecesLeft == 3;
        if (isKingBishopVsKing) {
            return GameState.KING_BISHOP_VS_KING;
        }

        // king and knight vs king
        final boolean isKingKnightVsKing = pieces.get()
                .allMatch(piece -> piece instanceof King || piece instanceof Knight)
                && piecesLeft == 3;
        if (isKingKnightVsKing) {
            return GameState.KING_KNIGHT_VS_KING;
        }

        // king and bishop vs king and bishop with bishops of the same color
        final boolean isKingBishopVsKingBishop;
        if (!onlyKingAndBishop) {
            isKingBishopVsKingBishop = false;
        } else if (piecesLeft != 4) {
            isKingBishopVsKingBishop = false;
        } else {
            isKingBishopVsKingBishop = pieces.get().filter(piece -> piece instanceof Bishop)
                    .map(Piece::getPosition)
                    .map(Game::isWhiteSquare)
                    .distinct().count() == 1;
        }

        if (isKingBishopVsKingBishop) {
            return GameState.KING_BISHOP_VS_KING_BISHOP;
        }

        // check if is stalemate
        final Player currentPlayer = whoseTurn();

        // stalemate: when the player is not able to perform any valid move and the king is not in check
        King king = currentPlayer.isWhite() ? getBoard().whiteKing : getBoard().blackKing;
        Objects.requireNonNull(king, "Couldn't determine draw: king is null");
        boolean canMove = board.getAllPossibleMoves().stream()
                .mapToLong(pieceStreamMap -> pieceStreamMap.values().stream().flatMap(s -> s).count()).sum() > 0;
        final boolean stalemate = !canMove && !king.isInCheck(getBoard());
        if (stalemate) {
            return GameState.STALEMATE;
        }

        return GameState.ONGOING;
    }

    /**
     * Check if the game is over.<br>
     * A player is checkmate if he is in check, it's his turn and he can not make legal move to escape the check
     *
     * @return the player who won or null
     */
    @Nullable
    Player isCheckmate() {
        Player currentPlayer = whoseTurn();
        King playersKing = currentPlayer.isWhite() ? getBoard().getWhiteKing() : getBoard().getBlackKing();
        assert playersKing != null;

        long possibleMoves = getBoard().getAllPossibleMoves().stream().mapToLong(map -> map.values().stream().mapToLong(Stream::count).sum()).sum();

        if (playersKing.isInCheck(board) && possibleMoves == 0) {
            // return the winning player
            return player1.equals(currentPlayer) ? player2 : player1;
        }

        // neither player is in check
        return null;
    }
}
