package de.dhbw.tinf18b4.chess.backend;


import de.dhbw.tinf18b4.chess.backend.piece.Bishop;
import de.dhbw.tinf18b4.chess.backend.piece.King;
import de.dhbw.tinf18b4.chess.backend.piece.Knight;
import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */

public class Game {
    /**
     * The Board instance modeling the state of the Game
     */
    @NotNull
    @Getter
    private final Board board;
    @Getter
    private final History history = new History();
    private Player player1;
    private Player player2;

    public Game(@NotNull Player player1, @NotNull Player player2) {
        board = new Board(this);
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game(@NotNull Player player1, @NotNull Player player2, @NotNull Piece[] pieces) {
        board = new Board(this, pieces);
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempt to perform a move on the board
     *
     * @param move The move
     * @return whether it the move was applied
     */
    public boolean makeMove(@NotNull Move move) {
        Move lastMove = history.lastMove();

        // Prevent player from making a move if they ...

        // ... don't belong in this game, ...
        if (!(player1.equals(move.getPlayer())
                || player2.equals(move.getPlayer()))) {
            return false;

        }

        // ... is not the white player when making the first move or ...
        if (lastMove == null && !move.getPlayer().isWhite()) {
            return false;
        }

        // ... have just made a move
        if (lastMove != null && move.getPlayer().equals(lastMove.getPlayer())) {
            return false;
        }

        // Otherwise check if their move is possible
        // and apply the move eventually
        if (board.checkMove(move)) {
            board.applyMove(move);

            history.addMove(move);

            return true;
        }

        return false;
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
        board.getPieces().forEach(piece -> {
            if (piece.isCaptured()) return;
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
     * Check if the game is a draw. The following rules are implemented:
     * <ul>
     * <li>stalemate</li>
     * <li>insufficient material</li>
     * </ul>
     * <p>
     * TODO check if is checkmate
     *
     * @return whether the game is a draw or not
     */
    public boolean isDraw() {
        Supplier<Stream<Piece>> pieces = () -> getBoard().getPieces().filter(Objects::nonNull);

        // check if is stalemate
        final Move lastMove = getHistory().lastMove(); // the player whose turn is now
        final Player currentPlayer;
        if (lastMove == null) {
            currentPlayer = player1.isWhite() ? player1 : player2;
        } else {
            currentPlayer = lastMove.getPlayer() == player1 ? player2 : player1;
        }

        // stalemate: when the player is not able to perform any valid move and the king is not in check
        final boolean stalemate = pieces.get()
                .filter(piece -> piece.isWhite() == currentPlayer.isWhite())
                .allMatch(piece -> Stream.concat(piece.getValidMoves(getBoard()), piece.getValidCaptureMoves(getBoard())).count() == 0)

                && !(currentPlayer.isWhite() ? getBoard().whiteKing : getBoard().blackKing).isInCheck(getBoard());

        // draw by insufficient material is when one of these combinations occur
        final boolean onlyKingAndBishop = pieces.get().allMatch(piece -> piece instanceof King || piece instanceof Bishop);

        // king vs king
        final boolean isKingVsKing = pieces.get().count() == 2;

        // king and bishop vs king
        final boolean isKingBishopVsKing = onlyKingAndBishop && pieces.get().count() == 3;

        // king and knight vs king
        final boolean isKingKnightVsKing = pieces.get()
                .allMatch(piece -> piece instanceof King || piece instanceof Knight)
                && pieces.get().count() == 3;

        // king and bishop vs king and bishop with bishops of the same color
        final boolean isKingBishopVsKingBishop;
        if (!onlyKingAndBishop) {
            isKingBishopVsKingBishop = false;
        } else if (pieces.get().count() != 4) {
            isKingBishopVsKingBishop = false;
        } else {
            isKingBishopVsKingBishop = pieces.get().filter(piece -> piece instanceof Bishop)
                    .map(Piece::getPosition)
                    .map(Game::isWhiteSquare)
                    .distinct().count() == 1;
        }

        return stalemate || isKingVsKing || isKingBishopVsKing || isKingKnightVsKing | isKingBishopVsKingBishop;
    }

    private static boolean isWhiteSquare(@NotNull Position p) {
        return (p.getFile() * 35 + p.getRank()) % 2 == 0;
    }
}
