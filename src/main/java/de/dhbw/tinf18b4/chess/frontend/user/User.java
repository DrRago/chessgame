package de.dhbw.tinf18b4.chess.frontend.user;

/**
 * @author Leonhard Gahr
 */
public class User {
    private String username;
    private String password;
    private final String ID;

    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.ID = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    /**
     * Check whether the user credentials are correct
     * and the user exists with that password combination
     *
     * @return the validity check (true for valid, false for invalid)
     */
    public boolean validateLogin() {
        // TODO: 01/07/2019 Implement
        return false;
    }

}
