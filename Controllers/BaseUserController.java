package Controllers;

import DAO.UserDAO;
import Models.User;

public abstract class BaseUserController {
    // Encapsulation: Protect the DAO instance
    protected final UserDAO dao;

    public BaseUserController() {
        this.dao = new UserDAO();
    }

    // Inheritance: Common method for all user types
    public User getUserData(String userId) {
        if (userId == null || userId.trim().isEmpty())
            return null;

        return dao.getUserById(userId.trim());
    }

    // Abstraction & Polymorphism: Subclasses must implement specific profile update logic
    public abstract String updateProfile(User user);
    
    // Encapsulation: Reusable helper method
    protected boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
