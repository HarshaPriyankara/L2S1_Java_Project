package Controllers.ProfileControllers;

import Models.User;

public class LecTOProfileController extends BaseUserController {

    @Override
    public String updateProfile(User updatedUser) {
        if (isNullOrEmpty(updatedUser.getFname()) || isNullOrEmpty(updatedUser.getEmail()) || updatedUser.getDateOfBirth() == null) {
            return "VALIDATION_ERROR: Name, Email, and DOB are required.";
        }

        User existing = dao.getUserById(updatedUser.getUserID());
        if (existing != null) {
            updatedUser.setPassword(existing.getPassword()); // Keep existing password
            updatedUser.setOriginalUserID(existing.getUserID());
        }

        // Check if Email is taken by another user
        if (existing != null && !updatedUser.getEmail().equalsIgnoreCase(existing.getEmail()) && dao.emailExists(updatedUser.getEmail())) {
            return "VALIDATION_ERROR: This email is already in use.";
        }

        if (dao.updateUser(updatedUser)) {
            return "SUCCESS: Profile updated successfully!";
        } else {
            return "DATABASE_ERROR: Could not save changes.";
        }
    }
}