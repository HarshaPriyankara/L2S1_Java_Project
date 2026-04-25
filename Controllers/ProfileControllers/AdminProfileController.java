package Controllers.ProfileControllers;

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

        User existing = dao.getUserById(user.getOriginalUserID());
        if (existing == null) {
            return "DATABASE_ERROR: User not found.";
        }

        if (!existing.getUserID().equals(user.getUserID())) {
            return "VALIDATION_ERROR: Changing User ID is not supported here.";
        }

        if (!normalizeRole(existing.getRole()).equals(normalizeRole(user.getRole()))) {
            return "VALIDATION_ERROR: Changing user role is not supported here.";
        }

        return dao.updateUser(user) ? "SUCCESS: All details updated!" : "DATABASE_ERROR: Failed to save.";
    }

    public String deleteUser(String id) {
        if (isNullOrEmpty(id)) return "VALIDATION_ERROR: ID is required.";
        if (!dao.userExists(id)) return "NOT_FOUND: User does not exist.";

        return dao.deleteUser(id) ? "SUCCESS: User deleted." : "DATABASE_ERROR: Delete failed.";
    }

    private String normalizeRole(String role) {
        if (role == null) return "";
        String value = role.trim().toLowerCase();
        if (value.equals("lecture")) return "lecturer";
        if (value.equals("technical officer")) return "techofficer";
        return value;
    }
}
