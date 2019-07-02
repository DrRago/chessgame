package de.dhbw.tinf18b4.chess.backend.position;

import lombok.Getter;

/**
 * @author Leonhard Gahr
 */
public class Position {
    @Getter
    private int rank;

    @Getter
    private char file;

    /**
     * initialize the position with a valid field on a chessboard
     *
     * @param rank the rank or "row"
     * @param file the file or "column"
     */
    public Position(char file, int rank) {
        // validate the point to meet chess requirements
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8)
            throw new IllegalArgumentException(Character.toString(rank) + file + "is invalid");
        this.rank = rank;
        this.file = file;
    }

    private Position(int rank, char file) {
        this(file, rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position other = (Position) o;

        return rank == other.rank && file == other.file;
    }

    @Override
    public int hashCode() {
        int result = rank;
        result = 31 * result + file;
        return result;
    }

    @Override
    public String toString() {
        return Character.toString(rank) + file;
    }

    public Position leftNeighbor() {
        try {
            return new Position(rank, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position rightNeighbor() {
        try {
            return new Position(rank, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position topNeighbor() {
        try {
            return new Position(rank + 1, file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position bottomNeighbor() {
        try {
            return new Position(rank - 1, file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position upperLeftNeighbor() {
        try {
            return new Position(rank + 1, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position upperRightNeighbor() {
        try {
            return new Position(rank + 1, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position lowerLeftNeighbor() {
        try {
            return new Position(rank - 1, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position lowerRightNeighbor() {
        try {
            return new Position(rank - 1, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
