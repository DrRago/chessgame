package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link Game} implementation
 *
 * @author Leonhard Gahr
 */
public class PawnTest {
    private User user1 = new User("player1", "", "");
    private User user2 = new User("player2", "", "");
    private Player white = new Player(true, user1);
    private Player black = new Player(false, user2);

    @Test
    public void enPassantTest() {
        Game game = new Game(white, black);

        Pawn blackP7b = (Pawn) game.getBoard().findPieceByPosition(new Position('b', 7));

        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b2-b3", white)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b7-b5", black)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("a2-a4", white)));
        assertTrue("Could not move P7b", game.makeMove(game.getBoard().buildMove("b5-b4", black)));


        Position capturePosition = new Position('a', 3);
        assertTrue("En passant not recognized", blackP7b.getValidCaptureMoves(game.getBoard())
                .anyMatch(position -> position.equals(capturePosition)));
    }
}
