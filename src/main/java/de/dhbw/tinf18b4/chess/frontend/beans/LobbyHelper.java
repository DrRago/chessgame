package de.dhbw.tinf18b4.chess.frontend.beans;

import de.dhbw.tinf18b4.chess.backend.lobby.LobbyManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Leonhard Gahr
 */
public class LobbyHelper {
    private String[] lobbyNames;

    @NotNull
    public String[] getLobbyNames() {
        return LobbyManager.getPublicNotFullLobbies().keySet().toArray(new String[0]);
    }
}
