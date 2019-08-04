package de.dhbw.tinf18b4.chess.backend.lobby;

import de.dhbw.tinf18b4.chess.backend.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.*;
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
 */
public class LobbyManager {
    /**
     * A {@link HashMap} with all active lobbies
     */
    @NotNull
    @Getter
    static Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Create a new {@link Lobby} with a random ID and a {@link User} as creator
     *
     * @param creator the {@link User} who created the lobby
     * @return the ID of {@link Lobby} that has been created
     */
    @NotNull
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
     * Get all lobbies of a {@link User} with a started {@link de.dhbw.tinf18b4.chess.backend.Game}
     *
     * @param user the {@link User}
     * @return the lobbies the {@link User} is in
     */
    @NotNull
    public static Map<String, Lobby> getLobbiesByUser(@NotNull User user) {
        return lobbies.entrySet().stream()
                .filter(entry -> entry.getValue().hasUser(user))
                .filter(entry -> entry.getValue().getStatus() == LobbyStatus.GAME_STARTED)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get a {@link Map} of all public lobbies
     *
     * @return the {@link Map} of all public lobbies
     */
    @NotNull
    public static Map<String, Lobby> getPublicLobbies() {
        return lobbies.entrySet().stream()
                .filter(entry -> entry.getValue().isPublicLobby())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get a {@link Map} of all public lobbies that are not full
     *
     * @return the {@link Map} of all public lobbies
     */
    @NotNull
    public static Map<String, Lobby> getPublicNotFullLobbies() {
        return getPublicLobbies().entrySet().stream()
                .filter(entry -> Arrays.stream(entry.getValue().getPlayers()).anyMatch(Objects::isNull))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Remove a {@link Lobby} by its ID
     *
     * @param ID the ID of the {@link Lobby} to be removed
     */
    static void removeLobby(@NotNull String ID) {
        lobbies.remove(ID);
    }

    /**
     * Remove a {@link Lobby}
     *
     * @param lobby the {@link Lobby} to be removed
     */
    public static void removeLobby(@NotNull Lobby lobby) {
        lobbies.entrySet().removeIf(entry -> entry.getValue() == lobby);
    }

    /**
     * Generate a random {@link String} with the length of 7
     *
     * @return the random {@link String}
     */
    @NotNull
    private static String generateRandomString() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[7];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); //base64 encoding
    }
}
