package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.frontend.user.User;

/**
 * @author Leonhard Gahr
 *
 * The lobby for a game
 *
 * it is able to:
 *  - hold two users
 *  - disallow join of a user
 *  - start a game
 */
public class Lobby {
    private String ID;
    private boolean publicLobby = false;

    private User[] users = new User[2];
}
