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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position other = (Position) o;

        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        int result = (int) x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return Character.toString(x) + y;
    }
}
