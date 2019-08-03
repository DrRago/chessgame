package de.dhbw.tinf18b4.chess.frontend.beans;

import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
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
    public LobbyPair[] getLobbyNames() {
        return LobbyManager.getPublicNotFullLobbies().entrySet().stream().map(entry -> new LobbyPair(entry.getKey(), entry.getValue())).toArray(LobbyPair[]::new);
    }


}

