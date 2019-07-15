package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class QueenTest {
    @NotNull
    private User user1 = new User("player1", "", "");
    @NotNull
    private User user2 = new User("player2", "", "");
    @NotNull
    private Player white = new Player(true, user1);
    @NotNull
    private Player black = new Player(false, user2);

    private Board board = new Board(new Game(white, black), new Piece[]{});

    @Test
    public void moveTest() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position position = new Position((char) ('a' + i), 1 + j);
                Set<Position> queenMoves = new Queen(true, position).getValidMoves(board).collect(Collectors.toSet());

                Set<Position> bishopMoves = new Bishop(true, position).getValidMoves(board).collect(Collectors.toSet());
                bishopMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't move to %s but should because bishop can", bishopMoves), bishopMoves.isEmpty());

                Set<Position> rookMoves = new Rook(true, position).getValidMoves(board).collect(Collectors.toSet());
                rookMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't move to %s but should because rook can", rookMoves), rookMoves.isEmpty());

                Board board = new Board(new Game(white, black), generateFullBoard());
                queenMoves = new Queen(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());

                board = new Board(new Game(white, black), generateFullBoard());
                bishopMoves = new Bishop(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());
                bishopMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't capture at %s but should because bishop can", bishopMoves), bishopMoves.isEmpty());

                board = new Board(new Game(white, black), generateFullBoard());
                rookMoves = new Rook(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());
                rookMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't capture at %s but should because rook can", rookMoves), rookMoves.isEmpty());
            }
        }
    }

    @Test
    public void captureTest() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position position = new Position((char) ('a' + i), 1 + j);

                Board board = new Board(new Game(white, black), generateFullBoard());
                Set<Position> queenMoves = new Queen(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());

                board = new Board(new Game(white, black), generateFullBoard());
                Set<Position> bishopMoves = new Bishop(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());
                bishopMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't capture at %s but should because bishop can", bishopMoves), bishopMoves.isEmpty());

                board = new Board(new Game(white, black), generateFullBoard());
                Set<Position> rookMoves = new Rook(true, position).getValidCaptureMoves(board).collect(Collectors.toSet());
                rookMoves.removeAll(queenMoves);
                assertTrue(String.format("Queen can't capture at %s but should because rook can", rookMoves), rookMoves.isEmpty());
            }
        }
    }

    @NotNull
    private Piece[] generateFullBoard() {
        Stream.Builder<Piece> builder = Stream.builder();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position((char) ('a' + i), 1 + j);
                builder.accept(new Pawn(false, p));
            }
        }

        return builder.build().toArray(Piece[]::new);
    }
}
