package de.dhbw.tinf18b4.chess.frontend.beans;

import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import org.jetbrains.annotations.NotNull;

/**
 * Helper bean for the frontend to get a list of all lobbies
 */
public class LobbyHelper {
    /**
     * class variable that is not really used for anything but necessary for jsp to call the getter
     */
    @SuppressWarnings("unused")
    private String[] lobbyNames;

    /**
     * Generate a list of all lobby ids
     *
     * @return the array of lobby ids
     */
    @NotNull
    public String[] getLobbyNames() {
        return LobbyManager.getPublicNotFullLobbies().keySet().toArray(new String[0]);
    }
}
