package de.dhbw.tinf18b4.chess.backend.lobby;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonhard Gahr
 *
 * Lobby manager should store all current lobbies
 * there should be the opportunity to:
 *  - get a list of all public lobbies
 *  - generate a new lobby
 *  - remove a lobby
 */
public class LobbyManager {
    private static Map<String, Lobby> lobbies = new HashMap<>();

}
