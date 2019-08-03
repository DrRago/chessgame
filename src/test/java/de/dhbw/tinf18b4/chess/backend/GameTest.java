package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.*;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import de.dhbw.tinf18b4.chess.states.GameState;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class GameTest {
    @NotNull
    private User user1 = new User("user1");
    @NotNull
    private User user2 = new User("user2");

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
        assertFalse(game.evaluateGame().isDraw());
    }

    @Test
    public void stalemateTest() {
        Piece[] pieces = new Piece[]{
                new King(true, new Position("e1")),
                new Pawn(false, new Position("e2")),
                new King(false, new Position("e3"))
        };
        Game game = new Game(white, black, pieces);
        assertSame("Game should be in stalemate", GameState.STALEMATE, game.evaluateGame());
    }

    @Test
    public void drawByInsufficientMaterialTest() {
        Piece[] kingVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new King(true, new Position("a8"))
        };
        Game game = new Game(white, black, kingVsKing);

        assertSame("Game should be in draw - king vs king", GameState.KING_VS_KING, game.evaluateGame());

        Piece[] kingBishopVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingBishopVsKing);
        assertSame("Game should be in draw - king and bishop vs king", GameState.KING_BISHOP_VS_KING, game.evaluateGame());

        Piece[] kingKnightVsKing = new Piece[]{
                new King(false, new Position("a1")),
                new Knight(true, new Position("a7")),
                new King(true, new Position("a8"))
        };
        game = new Game(white, black, kingKnightVsKing);
        assertSame("Game should be in draw - king and knight vs king", GameState.KING_KNIGHT_VS_KING, game.evaluateGame());

        Piece[] kingBishopVsKingBishop = new Piece[]{
                new King(false, new Position("a1")),
                new Bishop(false, new Position("a2")),

                new King(true, new Position("a8")),
                new Bishop(true, new Position("b1"))
        };
        game = new Game(white, black, kingBishopVsKingBishop);
        assertSame("Game should be in draw - king and bishop vs king and bishop", GameState.KING_BISHOP_VS_KING_BISHOP, game.evaluateGame());
    }

    @Test(expected = AssertionError.class)
    public void checkmateTest() {
        Piece[] checkmate = new Piece[]{
                new King(true, new Position("f5")),
                new Rook(true, new Position("h1")),

                new King(false, new Position("h5"))
        };

        Game game = new Game(white, black, checkmate);
        assertEquals("White player should have won", white, game.isCheckmate());
    }

    @Test
    public void initialSetupTest() {
        Game game = new Game(white, black);

        game.getBoard().getPieces()
                .filter(piece -> piece instanceof Rook || piece instanceof Bishop || piece instanceof King || piece instanceof Queen)
                .forEach(piece -> {
                    assertEquals(0, piece.getValidMoves(game.getBoard()).count());
                    assertEquals(0, piece.getValidCaptureMoves(game.getBoard()).count());
                });
        game.getBoard().getPieces()
                .filter(piece -> piece instanceof Knight || piece instanceof Pawn)
                .forEach(piece -> {
                    assertEquals(2, piece.getValidMoves(game.getBoard()).count());
                    assertEquals(0, piece.getValidCaptureMoves(game.getBoard()).count());
                });
    }
}
