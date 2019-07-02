package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.user.User;
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
        assertEquals("Expected lobby users to be max of 2", 2, lobby.getPlayers().length);
        assertEquals("Expected user 0 to be me", me, lobby.getPlayers()[0].getUser());
        assertNull("Expected user 1 to be null", lobby.getPlayers()[1]);

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
