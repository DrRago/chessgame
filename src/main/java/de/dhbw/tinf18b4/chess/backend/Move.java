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
    final private Game.Player player;
    final private Position destination;
    final private Figure figure;

    Move(Game.Player player, Position destination, Figure figure) {
        this.player = player;
        this.destination = destination;
        this.figure = figure;
    }

    /**
     * Return the player associated with this move
     *
     * @return the player
     */
    Game.Player getPlayer() {
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
