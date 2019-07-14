package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.Game;
import de.dhbw.tinf18b4.chess.backend.Player;
import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * The lobby for a game
 * <p>
 * it is able to:
 * <ul>
 * <li>hold two players</li>
 * <li>disallow join of a {@link User}</li>
 * <li>start a {@link Game}</li>
 * </ul>
 */
public class Lobby {

    /**
     * Whether the {@link Lobby} is public
     */
    @Getter
    @Setter
    private boolean publicLobby = true;

    /**
     * the players in this {@link Lobby}
     */
    @Getter
    private Player[] players = new Player[2];

    /**
     * The {@link Game} that will be played
     */
    @Nullable
    @Getter
    private Game game = null;

    /**
     * The current status of the {@link Game} (may be WAITING or STARTED, no errors)
     */
    @NotNull
    @Getter
    private LobbyStatus status = LobbyStatus.WAITING_FOR_START;

    public Lobby(@NotNull User creator) {
        this.players[0] = new Player(true, creator);
    }

    @NotNull
    public LobbyStatus startGame() {
        if (Arrays.stream(players).anyMatch(Objects::isNull)) {
            return LobbyStatus.NOT_ENOUGH_PLAYERS;
        }
        game = new Game(players[0], players[1]);

        this.status = LobbyStatus.GAME_STARTED;
        return LobbyStatus.GAME_STARTED;
    }

    /**
     * Let a new {@link User} join the {@link Lobby}
     *
     * @param user the user (not null)
     * @return the {@link Player} that has been created
     */
    @Nullable
    public Player join(@NotNull User user) {
        if (players[0] == null) {
            players[0] = new Player(!players[1].isWhite(), user);
            return players[0];
        } else if (players[1] == null) {
            players[1] = new Player(!players[0].isWhite(), user);
            return players[1];
        } else {
            return null;
        }
    }

    /**
     * Remove a {@link Player} by it's user object from the {@link Lobby}
     *
     * @param user the {@link User} object (not null)
     * @return whether the game of the lobby hat to be stopped or not
     */
    public boolean leave(@NotNull User user) {
        for (int i = 0; i < 2; i++) {
            if (players[i] != null && players[i].getUser().equals(user)) {
                players[i] = null;
            }
        }

        // stop the game if it was active
        if (status == LobbyStatus.GAME_STARTED) {
            status = LobbyStatus.WAITING_FOR_START;
            game = null;
            return true;
        }
        return false;
    }

    /**
     * Check whether the {@link Lobby} contains a specific {@link User}
     *
     * @param user the {@link User} to check
     * @return true if the {@link User} is in this {@link Lobby}, false if not
     */
    public boolean hasUser(@NotNull User user) {
        return getPlayerByUser(user) != null;
    }

    /**
     * Get the according {@link Player} of a specific {@link User}
     *
     * @param user the {@link User} to get the player from
     * @return the {@link Player} or null, if the user doesn't have one
     */
    @Nullable
    public Player getPlayerByUser(@NotNull User user) {
        return Arrays.stream(players)
                .filter(Objects::nonNull)
                .filter(player -> player.getUser().equals(user))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return String.format("Lobby (%d/%d) - %s", Arrays.stream(players).filter(Objects::isNull).count(), 2, status);
    }
}
