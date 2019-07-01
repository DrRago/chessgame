package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.User;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Leonhard Gahr
 */
public class LobbyManagerTest {
    private static User me = new User("test", "123", "123");


    @Test
    public void createRemoveLobbyTest() {
        String lobbyID = LobbyManager.createLobby(me);
        Lobby lobby = LobbyManager.lobbies.get(lobbyID);

        assertNotNull(lobbyID);
        assertNotNull(lobby);
        assertEquals("Expected lobby users to be max of 2", 2, lobby.getUsers().length);
        assertEquals("Expected user 0 to be me", me, lobby.getUsers()[0]);
        assertNull("Expected user 1 to be null", lobby.getUsers()[1]);

        LobbyManager.removeLobby(lobbyID);
        assertNull(LobbyManager.lobbies.get(lobbyID));
    }

    @Test
    public void getPublicLobbiesTest() {
        String lobbyID = LobbyManager.createLobby(me);
        Lobby lobby = LobbyManager.lobbies.get(lobbyID);

        assertEquals("Expected list size to be 0", 0, LobbyManager.getPublicLobbies().size());
        lobby.setPublicLobby(true);
        assertEquals("Expected list size to be 1", 1, LobbyManager.getPublicLobbies().size());
    }
}
