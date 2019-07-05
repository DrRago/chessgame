package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.piece.Piece;
import de.dhbw.tinf18b4.chess.backend.position.Position;

/**
 * An move made by a player on board. When it is applied onto a board it will move a piece
 * from its current position on the board to the destination position.
 * To prevent invalid moves, one must first check this move against the game rules
 * for this piece and the current board state.
 */
public class Move {
    final private Player player;
    final private Position destination;
    final private Piece piece;

    public Move(Player player, Position destination, Piece piece) {
        if (player.isWhite() != piece.isWhite()) {
            String message = String.format("Player %s can't move piece %s of another player %s", player, piece, player);
            throw new IllegalArgumentException(message);
        }

        this.player = player;
        this.destination = destination;
        this.piece = piece;
    }

    /**
     * Return the player associated with this move
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Return the position associated with this move
     *
     * @return the destination position
     */
    public Position getDestination() {
        return destination;
    }

    /**
     * Return the piece associated with this move
     *
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }
}
