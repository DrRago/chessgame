package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.user.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class GameTest {
    private User test = new User("", "", "");
    private Game game = new Game(new Player(false, test), new Player(true, test));

    @Test
    public void fenTest() {
        assertEquals("Initial game FEN should be", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", game.asFen());
    }
}
