package de.dhbw.tinf18b4.chess.backend.piece;

import de.dhbw.tinf18b4.chess.backend.Board;
import de.dhbw.tinf18b4.chess.backend.position.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Leonhard Gahr
 */
public class Rook implements Piece {
    private final boolean white;
    private Position position;

    public Rook(boolean white, Position position) {
        this.white = white;
        this.position = position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Stream<Position> getValidMoves(@NotNull Board board) {
        // The rook can move vertically or horizontally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        return Stream.of(
                Utils.directionalIteratorUntilOccupied(position, board, Position::topNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::bottomNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::leftNeighbor),
                Utils.directionalIteratorUntilOccupied(position, board, Position::rightNeighbor))
                .flatMap(s -> s);
    }

    @Override
    public Stream<Position> getValidCaptureMoves(@NotNull Board board) {
        // The rook can move vertically or horizontally as far as he wants but he can't leap over other pieces.
        // Thus, we iterate over the diagonal positions in each of the 4 possible directions.
        // To obtain the capture positions find the nearest enemy piece along all directions.
        return Stream.of(
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::topNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::bottomNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::leftNeighbor),
                Utils.directionalIteratorFirstEnemy(position, board, white, Position::rightNeighbor))
                .flatMap(Optional::stream);
    }

    @Override
    public boolean isWhite() {
        return white;
    }

    @Override
    public boolean isCaptured() {
        return false;
    }

    @Override
    public char getFenIdentifier() {
        return white ? 'R' : 'r';
    }
}
