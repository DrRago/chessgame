package de.dhbw.tinf18b4.chess.backend.position;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {
    @Test
    public void fromStringTest() {
        assertEquals(new Position('a', 3), new Position("a3"));
        assertEquals(new Position('a', 3), new Position("3a"));

        assertEquals(new Position(3, 'a'), new Position("a3"));
        assertEquals(new Position(3, 'a'), new Position("3a"));

        assertEquals("a3", new Position("3a").toString());
        assertEquals("a3", new Position("a3").toString());
    }
}
