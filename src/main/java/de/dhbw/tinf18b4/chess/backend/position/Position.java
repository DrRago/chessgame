package de.dhbw.tinf18b4.chess.backend.position;

/**
 * @author Leonhard Gahr
 */
public class Position {
    private char x;
    private int y;

    /**
     * initialize the position with a valid field on a chessboard
     *
     * @param x the x position
     * @param y the y position
     */
    public Position(char x, int y) {
        // validate the point to meet chess requirements
        if (y > 8 || y < 1 || x < 'a' || x > 'h')
            throw new IllegalArgumentException(Character.toString(x) + y + "is invalid");
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return Character.toString(x) + y;
    }
}
