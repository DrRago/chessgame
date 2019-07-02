package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

/**
 * The lobby for a game
 * <p>
 * it is able to:
 * <ul>
 * <li>hold two players</li>
 * <li>disallow join of a user</li>
 * <li>start a game</li>
 * </ul>
 *
 * @author Leonhard Gahr
 */
public class Lobby {

    /**
     * Whether the lobby is public
     */
    @Getter
    @Setter
    private boolean publicLobby = false;

    /**
     * the players in this lobby
     */
    @Getter
    private Player[] players = new Player[2];

    /**
     * The game that will be played
     */
    @Getter
    private Game game;

    /**
     * The current status of the game (may be WAITING or STARTED, no errors)
     */
    @Getter
    private LobbyStatus status = LobbyStatus.WAITING_FOR_START;

    public Lobby(User creator) {
        this.players[0] = new Player(true, creator);
    }

    public LobbyStatus startGame() {
        if (Arrays.stream(players).anyMatch(Objects::isNull)) {
            return LobbyStatus.NOT_ENOUGH_PLAYERS;
        }
        game = new Game(players[0], players[1]);

        this.status = LobbyStatus.GAME_STARTED;
        return LobbyStatus.GAME_STARTED;
    }

    /**
     * Let a player join the lobby
     *
     * @param player the player to join (not null)
     * @return whether the join procedure was successful (false if lobby is full)
     */
    public boolean join(Player player) {
        if (players[0] == null) {
            players[0] = player;
        } else if (players[1] == null) {
            players[1] = player;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Remove a player by it's user object from the lobby
     *
     * @param user the user object (not null)
     */
    public void leave(User user) {
        for (int i = 0; i < 2; i++) {
            if (players[i] != null && players[i].getUser().equals(user)) {
                players[i] = null;
            }
        }
    }

    /**
     * Remove a player by it's ID from the lobby
     *
     * @param ID the ID (not null)
     */
    public void leave(String ID) {
        for (int i = 0; i < 2; i++) {
            if (players[i] != null && players[i].getUser().getID().equals(ID)) {
                players[i] = null;
            }
        }
    }

    /**
     * Check whether the lobby contains a specific user
     *
     * @param user the user to check
     * @return true if the user is in this lobby, false if not
     */
    public boolean hasUser(User user) {
        return Arrays.stream(players).anyMatch(player -> player.getUser().equals(user));
    }
}
