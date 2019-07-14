package de.dhbw.tinf18b4.chess.backend;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Leonhard Gahr
 */
public class BoardTest {
    @Test
    public void checkMoveFormatTest() {
        assertFalse("Format '2e-e3' is valid", Board.checkMoveFormat("2e-e3"));
        assertFalse("Format 'test' is valid", Board.checkMoveFormat("test"));
        assertFalse("Format '-e3' is valid", Board.checkMoveFormat("-e3"));
        assertFalse("Format 'e2-' is valid", Board.checkMoveFormat("e2-"));

        assertTrue("Format 'a8-a2' is invalid", Board.checkMoveFormat("a8-a2"));
    }
}
