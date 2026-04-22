package Controllers;

import Models.User;

public class AdminProfileController extends BaseUserController {

    public String registerUser(User user) {
        if (isNullOrEmpty(user.getUserID()) || isNullOrEmpty(user.getFname()) || 
            isNullOrEmpty(user.getEmail()) || user.getDateOfBirth() == null) {
            return "VALIDATION_ERROR: Please fill in all fields.";
        }

        if (dao.createUser(user)) {
            return "SUCCESS: User saved successfully!";
        } else {
            return "DATABASE_ERROR: Failed to save. ID or Email may exist.";
        }
    }

    public User fetchUser(String id) {
        return getUserData(id);
    }

    @Override
    public String updateProfile(User user) {
        if (isNullOrEmpty(user.getFname()) || isNullOrEmpty(user.getEmail()) || user.getDateOfBirth() == null) {
            return "VALIDATION_ERROR: Basic information is missing.";
        }
        
        return dao.updateUser(user) ? "SUCCESS: All details updated!" : "DATABASE_ERROR: Failed to save.";
    }

    public String deleteUser(String id) {
        if (isNullOrEmpty(id)) return "VALIDATION_ERROR: ID is required.";
        if (!dao.userExists(id)) return "NOT_FOUND: User does not exist.";

        return dao.deleteUser(id) ? "SUCCESS: User deleted." : "DATABASE_ERROR: Delete failed.";
    }
}