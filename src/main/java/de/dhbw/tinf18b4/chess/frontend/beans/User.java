package de.dhbw.tinf18b4.chess.frontend.beans;

/**
 * @author Leonhard Gahr
 */
public class User {
    private String userName = "";
    private String password = "";
    private boolean isLoggedIn = false;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsLoggedIn() {
        System.out.println(!(password.isEmpty() && userName.isEmpty()));
        return !(password.isEmpty() && userName.isEmpty());
    }
}
