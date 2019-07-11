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

    public Position(int rank, char file) {
        this(file, rank);
    }

    public Position(String an) {
        this(parseFile(an), parseRank(an));

        if (an.length() != 2) {
            throw new IllegalArgumentException(String.format("Cannot parse string '%s' as position", an));
        }
    }

    private static char parseFile(String an) {
        char candidate = an.charAt(0);

        if (candidate >= 'a' && candidate <= 'h') {
            return candidate;
        }

        candidate = an.charAt(1);
        if (candidate >= 'a' && candidate <= 'h') {
            return candidate;
        }

        throw new IllegalArgumentException(String.format("Cannot parse string '%s' as position", an));
    }

    private static int parseRank(String an) {
        char candidate = an.charAt(1);
        if (candidate >= '1' && candidate <= '8') {
            return candidate - '0';
        }

        candidate = an.charAt(0);
        if (candidate >= '1' && candidate <= '8') {
            return candidate - '0';
        }

        throw new IllegalArgumentException(String.format("Cannot parse string '%s' as position", an));
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
        return String.valueOf(file) + rank;
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
