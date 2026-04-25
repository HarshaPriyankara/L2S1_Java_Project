package Controllers.AuthControllers;

import DAO.UserDAO;
import Models.User;

import javax.swing.JFrame;

public class LoginController {
    private final UserDAO userDAO;
    private final DashboardFactory dashboardFactory;

    public LoginController() {
        this(new UserDAO(), new DashboardFactory());
    }

    LoginController(UserDAO userDAO, DashboardFactory dashboardFactory) {
        this.userDAO = userDAO;
        this.dashboardFactory = dashboardFactory;
    }

    public LoginResult authenticate(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedPassword = password == null ? "" : password.trim();

        if (normalizedUsername.isEmpty() || normalizedPassword.isEmpty()) {
            return LoginResult.failure("Username and password are required.");
        }

        try {
            User user = userDAO.validateUser(normalizedUsername, normalizedPassword);
            if (user == null) {
                return LoginResult.failure("Invalid Username or Password!");
            }

            JFrame dashboard = dashboardFactory.createDashboard(user);
            return LoginResult.success("Login Successful! Role: " + user.getDisplayRoleName(), user, dashboard);
        } catch (RuntimeException ex) {
            return LoginResult.failure("Unable to complete login right now. Please try again.");
        }
    }
}
