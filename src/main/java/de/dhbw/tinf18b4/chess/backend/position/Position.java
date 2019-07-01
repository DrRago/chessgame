package de.dhbw.tinf18b4.chess.backend.position;

/**
 * @author Leonhard Gahr
 */
public class Position {
    private char rank;
    private int file;

    /**
     * initialize the position with a valid field on a chessboard
     *
     * @param rank the rank or "row"
     * @param file the file or "column"
     */
    public Position(char rank, int file) {
        // validate the point to meet chess requirements
        if (file > 8 || file < 1 || rank < 'a' || rank > 'h')
            throw new IllegalArgumentException(Character.toString(rank) + file + "is invalid");
        this.rank = rank;
        this.file = file;
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
        int result = (int) rank;
        result = 31 * result + file;
        return result;
    }

    @Override
    public String toString() {
        return Character.toString(rank) + file;
    }

    public Position leftNeighbor() {
        try {
            return new Position(rank, file - 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position rightNeighbor() {
        try {
            return new Position(rank, file + 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position topNeighbor() {
        try {
            return new Position((char) (rank + 1), file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position bottomNeighbor() {
        try {
            return new Position((char) (rank - 1), file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position upperLeftNeighbor() {
        try {
            return new Position((char) (rank + 1), file - 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position upperRightNeighbor() {
        try {
            return new Position((char) (rank + 1), file + 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position lowerLeftNeighbor() {
        try {
            return new Position((char) (rank - 1), file - 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Position lowerRightNeighbor() {
        try {
            return new Position((char) (rank - 1), file + 1);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public char getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }
}
