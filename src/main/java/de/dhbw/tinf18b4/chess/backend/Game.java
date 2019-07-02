package de.dhbw.tinf18b4.chess.backend;


import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import lombok.Getter;

/**
 * @author Leonhard Gahr
 */

public class Game {
    /**
     * The Board instance modeling the state of the Game
     */
    @Getter
    private final Board board = new Board();
    private final History history = new History();
    private Player player1;
    private Player player2;

    private int halfMoveClock = 0;
    private int fullMoves = 1;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempt to perform a move on the board
     *
     * @param move The move
     * @return whether it the move was applied
     */
    public boolean makeMove(Move move) {
        Move lastMove = history.peekingPop();

        // Prevent player from making a move if they ...

        // ... don't belong in this game or ...
        if (player1.isSamePlayer(move.getPlayer())
                || player2.isSamePlayer(move.getPlayer())) {
            return false;

        }

        // ... have just made a move
        if (lastMove != null
                && move.getPlayer().isSamePlayer(lastMove.getPlayer())) {
            return false;
        }

        // Otherwise check if their move is possible
        // and apply the move eventually
        if (board.checkMove(move)) {
            board.applyMove(move);

            history.push(move);

            if (!move.getPlayer().isWhite()) {
                fullMoves++;
            }

            return true;
        }

        return false;
    }

    public int getFullMoves() {
        return fullMoves;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    /**
     * convert the {@link Board} to a FEN string <br>
     * this string only contains information about the {@link Position} of the {@link Piece}
     *
     * @return the FEN string
     * @see <a href='https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation'>FEN Wikipedia</a>
     */
    public String asFen() {
        StringBuilder stringBuilder = new StringBuilder();

        // build a 8x8 board with pieces as char name
        char[][] fenBoard = new char[8][8];
        board.getPieces().forEach(piece -> {
            int y = piece.getPosition().getFile() - 1;
            int x = piece.getPosition().getRank() - 'a';

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
     * We don't have a real History class yet
     * TODO: Implement the History class and remove this.
     */
    class History {
        /**
         * Add a move to history to make it the most recent move
         *
         * @param move The move
         */
        void push(Move move) {

        }

        /**
         * Return the most recent move without removing it from history
         *
         * @return The Move
         */
        Move peekingPop() {
            return null;
        }
    }
}
