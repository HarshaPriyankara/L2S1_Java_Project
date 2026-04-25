package Controllers.AuthControllers;

import Models.User;

import javax.swing.JFrame;

public class LoginResult {
    private final boolean success;
    private final String message;
    private final User user;
    private final JFrame dashboard;

    private LoginResult(boolean success, String message, User user, JFrame dashboard) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.dashboard = dashboard;
    }

    public static LoginResult success(String message, User user, JFrame dashboard) {
        return new LoginResult(true, message, user, dashboard);
    }

    public static LoginResult failure(String message) {
        return new LoginResult(false, message, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public JFrame getDashboard() {
        return dashboard;
    }
}
