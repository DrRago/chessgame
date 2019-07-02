package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.user.User;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Lobby manager should store all current lobbies
 * <p>
 * there is the opportunity to:
 * <ul>
 * <li>get a list of all public lobbies</li>
 * <li>generate a new {@link Lobby}</li>
 * <li>remove a {@link Lobby}</li>
 * </ul>
 *
 * @author Leonhard Gahr
 */
public class LobbyManager {
    /**
     * A {@link HashMap} with all active lobbies
     */
    static Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Create a new {@link Lobby} with a random ID and a {@link User} as creator
     *
     * @param creator the {@link User} who created the lobby
     * @return the ID of {@link Lobby} that has been created
     */
    public static String createLobby(@NotNull User creator) {
        String lobbyID;

        // make sure to generate an ID that doesn't exist
        do {
            lobbyID = generateRandomString();
        } while (lobbies.get(lobbyID) != null);

        Lobby lobby = new Lobby(creator);
        lobbies.put(lobbyID, lobby);

        return lobbyID;
    }

    /**
     * Get a {@link List} of all public lobbies
     *
     * @return the {@link List} of all public lobbies
     */
    public static List<Lobby> getPublicLobbies() {
        return lobbies.values().stream().filter(Lobby::isPublicLobby).collect(Collectors.toList()); // magic
    }

    /**
     * Remove a {@link Lobby} by its ID
     *
     * @param ID the ID of the {@link Lobby} to be removed
     */
    public static void removeLobby(@NotNull String ID) {
        lobbies.remove(ID);
    }

    /**
     * Generate a random {@link String} with the length of 7
     *
     * @return the random {@link String}
     */
    private static String generateRandomString() {
        byte[] array = new byte[7]; // fix size of 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
