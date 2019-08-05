package de.dhbw.tinf18b4.chess.frontend.beans;

import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import de.dhbw.tinf18b4.chess.backend.user.User;
import org.jetbrains.annotations.NotNull;

/**
 * Helper bean for the frontend to get a list of all lobbies
 */
public class LobbyHelper {
    /**
     * Generate a list of all lobbies
     *
     * @return the array of lobbies
     */
    @NotNull
    public LobbyPair[] getLobbies() {
        return LobbyManager.getPublicNotFullLobbies().entrySet().stream().map(entry -> new LobbyPair(entry.getKey(), entry.getValue())).toArray(LobbyPair[]::new);
    }

    /**
     * Get all the lobbies the user is participating in
     *
     * @return the array of lobbies
     */
    @NotNull
    public LobbyPair[] getCurrentLobbies(User user) {
        return LobbyManager.getLobbiesByUser(user).entrySet().stream().map(entry -> new LobbyPair(entry.getKey(), entry.getValue())).toArray(LobbyPair[]::new);
    }
}

