package Controllers;

import Models.User;

public class StudentProfileController extends BaseUserController {

    @Override
    public String updateProfile(User user) {
        if (isNullOrEmpty(user.getEmail()) || isNullOrEmpty(user.getContactNo()) || isNullOrEmpty(user.getAddress())) {
            return "VALIDATION_ERROR: Email, Contact, and Address are required.";
        }

        User existing = dao.getUserById(user.getUserID());
        if (existing == null) return "DATABASE_ERROR: User not found.";

        if (!user.getEmail().equalsIgnoreCase(existing.getEmail()) && dao.emailExists(user.getEmail())) {
            return "VALIDATION_ERROR: This email is already in use by another account.";
        }

        user.setOriginalUserID(user.getUserID());
        
        // Ensure we don't mess up passwords for students (they can't change it here)
        user.setPassword(existing.getPassword());

        if (dao.updateUser(user)) {
            return "SUCCESS: Profile details updated!";
        } else {
            return "DATABASE_ERROR: Failed to save changes.";
        }
    }
}