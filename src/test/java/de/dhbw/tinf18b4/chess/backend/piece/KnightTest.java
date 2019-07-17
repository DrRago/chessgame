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

    @Test
    public void moveTest() {
        Game game = new Game(white, black);

        assertTrue("Could not move Rb1", game.makeMove(game.getBoard().buildMove("b1-c3", white)).isPresent());
        assertTrue("Could not move Rg8", game.makeMove(game.getBoard().buildMove("g8-f6", black)).isPresent());

        assertTrue("Could not move Rc3", game.makeMove(game.getBoard().buildMove("c3-d5", white)).isPresent());
        assertTrue("Could not move Rh6", game.makeMove(game.getBoard().buildMove("f6-d5", black)).isPresent());
    }
}
