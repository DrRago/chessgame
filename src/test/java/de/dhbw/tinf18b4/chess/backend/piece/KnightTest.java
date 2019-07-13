package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Leonhard Gahr
 */
@FixMethodOrder
public class KnightTest {
    private User user1 = new User("player1", "", "");
    private User user2 = new User("player2", "", "");
    private Player white = new Player(true, user1);
    private Player black = new Player(false, user2);

    private Game game = new Game(white, black);

    @Test
    public void moveTest() {
        assertTrue("Could not move Rb1", game.makeMove(game.getBoard().buildMove("b1-c3", white)));
        assertTrue("Could not move Rg8", game.makeMove(game.getBoard().buildMove("g8-f6", black)));
    }

    @Test
    public void captureTest() {
        assertTrue("Could not move Rc3", game.makeMove(game.getBoard().buildMove("c3-d5", white)));
        assertTrue("Could not move Rh6", game.makeMove(game.getBoard().buildMove("h6-g4", black)));
        assertTrue("Could not move Rd5", game.makeMove(game.getBoard().buildMove("d5-e3", white)));
        assertTrue("Could not move Rg4", game.makeMove(game.getBoard().buildMove("g4-e3", black)));
    }
}
