package de.dhbw.tinf18b4.chess.backend;


import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leonhard Gahr
 */

public class Game {
    /**
     * The Board instance modeling the state of the Game
     */
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

    public String asFen() {
        StringBuilder stringBuilder = new StringBuilder();

        // FEN: 1. board state
        List<Piece> b = board.getPieces().sorted((left, right) -> {
            Position leftPosition = left.getPosition();
            Position rightPosition = right.getPosition();

            char rightRank = rightPosition.getRank();
            char leftRank = leftPosition.getRank();
            int leftFile = leftPosition.getFile();
            int rightFile = rightPosition.getFile();

            if (leftRank < rightRank || leftFile < rightFile) {
                return -1;
            } else if (leftRank > rightRank || leftFile > rightFile) {
                return 1;
            }

            return 0;
        }).collect(Collectors.toList());

        int rank = 1;
        int file = 1;

        for (Piece piece : b) {
            Position position = piece.getPosition();
            if (rank > position.getRank() || file > position.getFile()) {
                int skippedRanks = (rank - position.getRank() + 8 * (file - position.getFile())) % 8;
                int skippedFiles = (rank - position.getRank() + 8 * (file - position.getFile())) / 8;

                if (skippedFiles > 0) {
                    for (int i = 0; i < skippedFiles; i++) {
                        stringBuilder.append("/").append(8);
                    }
                }

                if (skippedRanks == 0) {
                    String pieceIdentifier = piece.getClass().getSimpleName().subSequence(0, 1).toString();
                    stringBuilder.append(piece.isWhite() ? pieceIdentifier : pieceIdentifier.toLowerCase());
                } else {
                    stringBuilder.append(skippedRanks);
                }
            }

            file = position.getFile();
            rank = position.getRank();
        }


        stringBuilder.append(" ")
                // FEN 2. active color
                .append(history.peekingPop().getPlayer().isWhite() ? "b" : "w")
                .append(" ")
                // FEN 3. castling availability
                // TODO: Replace when castling detection is done
                .append("-")
                .append(" ")
                // FEN 4. en passant target
                // TODO: Replace when en passant detection is done
                .append("-")
                .append(" ")
                // FEN 5. halfmove clock
                .append(getHalfMoveClock())
                .append(" ")
                // FEN 6. fullmove number
                .append(getFullMoves());
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
