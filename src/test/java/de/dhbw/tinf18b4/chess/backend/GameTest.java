package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class GameTest {
    private Game game = new Game(new Player(false), new Player(true));

    @Test
    public void fenTest() {
        System.out.println(Arrays.toString(game.getBoard().getPieces().map((Piece t) -> t.getClass().getSimpleName()).toArray()));
        assertEquals("Initial game FEN should be", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.asFen());
    }
}
