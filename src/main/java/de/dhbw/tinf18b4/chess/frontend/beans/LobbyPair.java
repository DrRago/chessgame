package de.dhbw.tinf18b4.chess.frontend.beans;

import de.dhbw.tinf18b4.chess.backend.lobby.Lobby;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyPair {
    private String ID;
    private Lobby lobby;

    LobbyPair(String ID, Lobby lobby) {
        this.ID = ID;
        this.lobby = lobby;
    }
}
