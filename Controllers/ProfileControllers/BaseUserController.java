package Controllers.ProfileControllers;

import DAO.UserDAO;
import Models.User;

public abstract class BaseUserController {

    protected final UserDAO dao;

    public BaseUserController() {
        this.dao = new UserDAO();
    }

    //get user data for all type of users (inheritance)
    /// @author dilusha
    public User getUserData(String userId) {
        if (userId == null || userId.trim().isEmpty())
            return null;

        return dao.getUserById(userId.trim());
    }

    // for subclasses
    public abstract String updateProfile(User user);
    
    // for chrck null or empty
    protected boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
