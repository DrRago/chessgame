package de.dhbw.tinf18b4.chess.backend.position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {
    @Test
    public void fromStringTest() {
        Position fromString = new Position("a3");
        assertEquals(fromString, new Position('a', 3));
        fromString = new Position("3a");
        assertEquals(fromString, new Position('a', 3));

        fromString = new Position("a3");
        assertEquals(fromString, new Position(3, 'a'));
        fromString = new Position("3a");
        assertEquals(fromString, new Position(3, 'a'));
    }
}
