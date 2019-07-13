package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BishopTest {
    private User user1 = new User("player1", "", "");
    private User user2 = new User("player2", "", "");
    private Player white = new Player(true, user1);
    private Player black = new Player(false, user2);

    @Test
    public void captureTest() {
        Game game = new Game(white, black);

        assertTrue("Could not move P2b", game.makeMove(game.getBoard().buildMove("b2-b3", white)));
        assertTrue("Could not move P7d", game.makeMove(game.getBoard().buildMove("d7-d6", black)));
        assertTrue("Could not move B1c", game.makeMove(game.getBoard().buildMove("c1-a3", white)));
        assertTrue("Could not move P7a", game.makeMove(game.getBoard().buildMove("a7-a6", black)));
        assertTrue("Could not move B1c", game.makeMove(game.getBoard().buildMove("a3-d6", white)));
    }
}
