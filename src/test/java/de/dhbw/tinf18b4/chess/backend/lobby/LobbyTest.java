package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.user.User;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the {@link Lobby} implementation
 *
 * @author Leonhard Gahr
 */
public class LobbyTest {
    private User me = new User("me", "123", "123");

    @Test
    public void joinLeaveLobbyTest() {
        Lobby lobby = new Lobby(me);

        assertNotNull(lobby);
        assertEquals("Lobby status should be waiting", LobbyStatus.WAITING_FOR_START, lobby.getStatus());

        User second = new User("second", "123", "122");
        lobby.join(second);
        assertTrue("Player should be in lobby", lobby.hasUser(second));

        lobby.leave(me);
        assertFalse("Player shouldn't be in lobby", lobby.hasUser(me));
    }

    @Test
    public void startGameTest() {
        Lobby lobby = new Lobby(me);

        assertNotNull(lobby);
        assertEquals("Lobby shouldn't be starting", LobbyStatus.NOT_ENOUGH_PLAYERS, lobby.startGame());

        User second = new User("second", "123", "122");
        lobby.join(second);
        assertEquals("Lobby should be starting", LobbyStatus.GAME_STARTED, lobby.startGame());

        lobby.leave(second);
        assertEquals("Lobby shouldn't be not started", LobbyStatus.WAITING_FOR_START, lobby.getStatus());
    }
}
