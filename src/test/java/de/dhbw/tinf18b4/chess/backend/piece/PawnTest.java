package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class PawnTest {
    @NotNull
    private User user1 = new User("player1", "", "");
    @NotNull
    private User user2 = new User("player2", "", "");
    @NotNull
    private Player white = new Player(true, user1);
    @NotNull
    private Player black = new Player(false, user2);

    @Test
    public void enPassantTest() {
        Game game = new Game(white, black);

        Pawn black7b = (Pawn) game.getBoard().findPieceByPosition(new Position("b7"));

        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b2-b3", white)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b7-b5", black)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("c2-c3", white)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b5-b4", black)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("a2-a4", white)));

        Position capturePosition = new Position("a3");
        assertTrue("En passant not recognized", black7b.getValidCaptureMoves(game.getBoard())
                .anyMatch(position -> position.equals(capturePosition)));
    }

    @Test
    public void moveOneForwardTest() {
        Game game = new Game(white, black);

        assertTrue("Could not move P2a one square forward", game.makeMove(game.getBoard().buildMove("a2-a3", white)));
        assertTrue("Could not move P7a one square forward", game.makeMove(game.getBoard().buildMove("a7-a6", black)));
    }

    @Test
    public void moveTwoForwardTest() {
        Game game = new Game(white, black);

        assertTrue("Could not move P2a two squares forward", game.makeMove(game.getBoard().buildMove("a2-a4", white)));
        assertTrue("Could not move P7a two squares forward", game.makeMove(game.getBoard().buildMove("a7-a5", black)));

        // test case against jumping over another piece
        game = new Game(white, black);

        assertTrue("Could not move N1a", game.makeMove(game.getBoard().buildMove("b1-a3", white)));
        assertTrue("Could not move P7a", game.makeMove(game.getBoard().buildMove("a7-a6", black)));
        assertFalse("Could move P2a", game.makeMove(game.getBoard().buildMove("a2-a3", white)));
    }

    @Test
    public void moveDiagonally() {
        Game game = new Game(white, black);

        assertFalse("Could move P2b diagonally", game.makeMove(game.getBoard().buildMove("b2-a3", white)));
        assertFalse("Could move P7b diagonally", game.makeMove(game.getBoard().buildMove("b7-a6", black)));

        game = new Game(white, black);

        assertTrue("Couldn't move P3a forward", game.makeMove(game.getBoard().buildMove("a2-a4", white)));
        assertTrue("Couldn't move P7a forward", game.makeMove(game.getBoard().buildMove("c7-c5", black)));
        assertTrue("Couldn't move P3a forward", game.makeMove(game.getBoard().buildMove("a4-a5", white)));
        assertTrue("Couldn't move P7a forward", game.makeMove(game.getBoard().buildMove("c5-c4", black)));
        assertTrue("Couldn't move P3a forward", game.makeMove(game.getBoard().buildMove("a5-a6", white)));
        assertTrue("Couldn't move P7a forward", game.makeMove(game.getBoard().buildMove("c4-c3", black)));
        assertTrue("Couldn't move P2b diagonally", game.makeMove(game.getBoard().buildMove("b2-c3", white)));
        assertTrue("Couldn't move P7b diagonally", game.makeMove(game.getBoard().buildMove("b7-a6", black)));

        game = new Game(white, black);

        assertFalse("Could move P2b diagonally", game.makeMove(game.getBoard().buildMove("b2-c3", white)));
        assertFalse("Could move P7b diagonally", game.makeMove(game.getBoard().buildMove("b7-c6", black)));
    }

    @Test
    public void moveBackwards() {
        Game game = new Game(white, black);

        assertTrue("Could not move P2a two squares forward", game.makeMove(game.getBoard().buildMove("a2-a4", white)));
        assertTrue("Could not move P7a two squares forward", game.makeMove(game.getBoard().buildMove("a7-a6", black)));

        assertFalse("Could move P2b backwards", game.makeMove(game.getBoard().buildMove("a4-a3", white)));
        assertFalse("Could move P7b backwards", game.makeMove(game.getBoard().buildMove("a6-a7", black)));
    }
}
