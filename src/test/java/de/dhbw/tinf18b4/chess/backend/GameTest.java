package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import junit.framework.AssertionFailedError;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class GameTest {
    @NotNull
    private User user1 = new User("user1", "", "");
    @NotNull
    private User user2 = new User("user2", "", "");

    @NotNull
    private Player white = new Player(true, user1);
    @NotNull
    private Player black = new Player(false, user2);

    @Test
    public void fenTest() {
        Game game = new Game(white, black);
        assertEquals("Initial game FEN should be", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", game.asFen());
    }

    @Test
    public void drawFalseTest() {

        Game game = new Game(white, black);
        assertFalse("Game shouldn't be in draw", game.isDraw());
    }

    // this test may fail due to incomplete implementation of getValidMoves
    @Test(expected = AssertionFailedError.class)
    public void stalemateTest() {
        Piece[] pieces = new Piece[]{
                new King(false, new Position("d8")),
                new Pawn(true, new Position("d7")),
                new King(true, new Position("d6"))
        };
        Game game = new Game(white, black, pieces);
        assertTrue("Game should be in stalemate", game.isDraw());
    }

    @Test
    public void drawByInsufficientMaterialTest() {
        Piece[] kingVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new King(true, new Position("a8"))
        };
        Game game = new Game(white, black, kingVsKing);

        assertTrue("Game should be in draw - king vs king", game.isDraw());

        Piece[] kingBishopVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingBishopVsKing);
        assertTrue("Game should be in draw - king and bishop vs king", game.isDraw());

        Piece[] kingKnightVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Knight(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingKnightVsKing);
        assertTrue("Game should be in draw - king and knight vs king", game.isDraw());

        Piece[] kingBishopVsKingBishop = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(false, new Position("a2")),

                new King(true, new Position("a8")),
                new Bishop(true, new Position("b1"))
        };
        game = new Game(white, black, kingBishopVsKingBishop);
        assertTrue("Game should be in draw - king and bishop vs king and bishop", game.isDraw());
    }
}
