package de.dhbw.tinf18b4.chess.backend.utility;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class MoveUtility {
    /**
     * check if the move format is correct
     * <p>
     * a move should be: '{source}-{target}' e.g.: 'a1-a2'
     *
     * @param move the move string defined as above
     * @return whether the format is valid or not
     */
    public static boolean checkMoveFormat(String move) {
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
}
