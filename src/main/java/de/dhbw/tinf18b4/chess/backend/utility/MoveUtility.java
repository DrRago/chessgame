package de.dhbw.tinf18b4.chess.backend.utility;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.Move;
import de.dhbw.tinf18b4.chess.backend.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class MoveUtility {
    /**
     * check if the {@link Move} format is correct
     * <p>
     * a {@link Move} should be: '{source}-{target}' e.g.: 'a1-a2'
     *
     * @param move the {@link Move} string defined as above
     * @return whether the format is valid or not
     */
    public static boolean checkMoveFormat(@Nullable String move) {
        if (move == null) {
            return false;
        }
        String[] moveArray = move.split("-");
        Stream<String> moveStream = Arrays.stream(moveArray);
        if (move.length() != 5 || moveArray.length != 2) {
            return false;
        }

        return moveStream.allMatch(m -> {
            if (m.length() != 2) {
                return false;
            }
            char file = m.charAt(0);
            int rank = Character.getNumericValue(m.charAt(1));

            return file >= 'a' && file <= 'h' && rank >= 1 && rank <= 8;
        });
    }

    /**
     * Build the {@link Move} according to a valid {@link Move} format defined in checkMoveFormat()
     *
     *
     * @return
     */
    public static @NotNull Move buildMove(String move, Board board, Player player) {
        return null;
    }
}
