package de.dhbw.tinf18b4.chess.backend.position;

import de.dhbw.tinf18b4.chess.backend.Board;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The position in a chess game contains a file (a-h) and a rank (1-8)
 * <p>
 * This class handles some utility functions for a position
 */
public class Position {
    /**
     * The rank of the position on a {@link Board chessboard} (1-8)
     */
    @Getter
    private int rank;

    /**
     * The file of the position on a {@link Board chessboard} (a-h)
     */
    @Getter
    private char file;

    /**
     * Initialize the position with a valid field on a {@link Board chessboard}
     *
     * @param rank the rank or "row"
     * @param file the file or "column"
     */
    public Position(char file, int rank) {
        // validate the point to meet chess requirements
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8)
            throw new IllegalArgumentException(Character.toString(rank) + file + " is invalid");
        this.rank = rank;
        this.file = file;
    }

    /**
     * Initialize the position with a valid field on a {@link Board chessboard}
     *
     * @param rank the rank or "row"
     * @param file the file or "column"
     */
    public Position(int rank, char file) {
        this(file, rank);
    }

    /**
     * Initialize a position from a 2 digit string
     * <p>
     * may be {rank}{file}
     *
     * @param an the position string
     */
    public Position(@NotNull String an) {
        this(parseFile(an), parseRank(an));

        if (an.length() != 2) {
            throw new IllegalArgumentException(String.format("Cannot parse string '%s' as position", an));
        }
    }

    /**
     * Parse the file from a position string, no matter on which position it is
     *
     * @param an The position string
     * @return the file of the position
     */
    private static char parseFile(@NotNull String an) {
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

    /**
     * Parse the rank from a position string, no matter on which position it is
     *
     * @param an The position string
     * @return the rank of the position
     */
    private static int parseRank(@NotNull String an) {
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

    /**
     * A two positions are equal if the rank and the file are equal
     *
     * @param o the object to compare to
     * @return whether the positions are the same
     */
    @Override
    public boolean equals(@Nullable Object o) {
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

    @NotNull
    @Override
    public String toString() {
        return String.valueOf(file) + rank;
    }

    /**
     * Get the left neighbor of the position or null if it's off-board
     *
     * @return the left neighbor or null
     */
    @Nullable
    public Position leftNeighbor() {
        try {
            return new Position(rank, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the right neighbor of the position or null if it's off-board
     *
     * @return the right neighbor or null
     */
    @Nullable
    public Position rightNeighbor() {
        try {
            return new Position(rank, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the top neighbor of the position or null if it's off-board
     *
     * @return the top neighbor or null
     */
    @Nullable
    public Position topNeighbor() {
        try {
            return new Position(rank + 1, file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the bottom neighbor of the position or null if it's off-board
     *
     * @return the bottom neighbor or null
     */
    @Nullable
    public Position bottomNeighbor() {
        try {
            return new Position(rank - 1, file);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the upper left neighbor of the position or null if it's off-board
     *
     * @return the upper left neighbor or null
     */
    @Nullable
    public Position upperLeftNeighbor() {
        try {
            return new Position(rank + 1, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the upper right neighbor of the position or null if it's off-board
     *
     * @return the upper right neighbor or null
     */
    @Nullable
    public Position upperRightNeighbor() {
        try {
            return new Position(rank + 1, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the lower left neighbor of the position or null if it's off-board
     *
     * @return the lower left neighbor or null
     */
    @Nullable
    public Position lowerLeftNeighbor() {
        try {
            return new Position(rank - 1, (char) (file - 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the lower right neighbor of the position or null if it's off-board
     *
     * @return the lower right neighbor or null
     */
    @Nullable
    public Position lowerRightNeighbor() {
        try {
            return new Position(rank - 1, (char) (file + 1));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
