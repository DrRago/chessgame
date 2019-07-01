package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.User;
import lombok.Getter;
import lombok.Setter;

/**
 * The lobby for a game
 * <p>
 * it is able to:
 * <ul>
 * <li>hold two users</li>
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
     * the users in this lobby
     */
    @Getter
    private User[] users = new User[2];

    public Lobby(User creator) {
        this.users[0] = creator;
    }
}
