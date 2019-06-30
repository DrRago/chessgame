package de.dhbw.tinf18b4.chess.backend;

import de.dhbw.tinf18b4.chess.backend.figure.Figure;
import de.dhbw.tinf18b4.chess.backend.position.Position;

/**
 * An move made by a player on board. When it is applied onto a board it will move a figure
 * from its current position on the board to the destination position.
 * To prevent invalid moves, one must first check this move against the game rules
 * for this figure and the current board state.
 */
class Move {
    final private Player player;
    final private Position destination;
    final private Figure figure;

    Move(Player player, Position destination, Figure figure) {
        if (player.isWhite() != figure.isWhite()) {
            String message = String.format("Player %s can't move figure %s of another player %s", player, figure, player);
            throw new IllegalArgumentException(message);
        }

        this.player = player;
        this.destination = destination;
        this.figure = figure;
    }

    /**
     * Return the player associated with this move
     *
     * @return the player
     */
    Player getPlayer() {
        return player;
    }

    /**
     * Return the position associated with this move
     *
     * @return the destination position
     */
    Position getDestination() {
        return destination;
    }

    /**
     * Return the figure associated with this move
     *
     * @return the figure
     */
    Figure getFigure() {
        return figure;
    }
}
