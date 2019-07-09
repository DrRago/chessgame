package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Move;
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
    private User test = new User("", "", "");
    private Player white = new Player(true, test);
    private Player black = new Player(false, test);

    @Test
    public void enPassantTest() {
        Game game = new Game(white, black);

        Pawn whiteP2a = (Pawn) game.getBoard().findPieceByPosition(new Position('a', 2));
        Pawn whiteP2b = (Pawn) game.getBoard().findPieceByPosition(new Position('b', 2));
        Pawn blackP7b = (Pawn) game.getBoard().findPieceByPosition(new Position('b', 7));

        assertTrue("Could not move P7b", game.makeMove(new Move(black, blackP7b.getPosition(), blackP7b.getPosition().bottomNeighbor().bottomNeighbor(), blackP7b)));
        assertTrue("Could not move p2b", game.makeMove(new Move(white, whiteP2b.getPosition(), whiteP2b.getPosition().topNeighbor(), whiteP2b)));
        assertTrue("Could not move P7b", game.makeMove(new Move(black, blackP7b.getPosition(), blackP7b.getPosition().bottomNeighbor(), blackP7b)));
        assertTrue("Could not move p7b", game.makeMove(new Move(white, whiteP2a.getPosition(), whiteP2a.getPosition().topNeighbor().topNeighbor(), whiteP2a)));


        Position capturePosition = new Position('a', 3);
        assertTrue("En passant not recognized", blackP7b.getValidCaptureMoves(game.getBoard())
                .anyMatch(position -> position.equals(capturePosition)));
    }
}
