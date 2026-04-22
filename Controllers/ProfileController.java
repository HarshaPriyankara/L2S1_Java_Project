package Controllers;

import DAO.UserDAO;
import Models.User;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ProfileController {
    private final UserDAO dao = new UserDAO();

    public User getProfileData(String userId) {
        return dao.getUserById(userId);
    }

    public String updateLecturerProfile(User updatedUser, String rawDob) {
        if (rawDob.isEmpty() || updatedUser.getFname().isEmpty() || updatedUser.getEmail().isEmpty()) {
            return "VALIDATION_ERROR: Name, Email, and DOB are required.";
        }

        try {
            updatedUser.setDob(LocalDate.parse(rawDob.trim()));

            // SECURITY: Keep the ID and Password the same
            User existing = dao.getUserById(updatedUser.getUserID());
            if (existing != null) {
                updatedUser.setPassword(existing.getPassword()); // Keep existing password
                updatedUser.setOriginalUserID(existing.getUserID());
            }

            // Check if Email is taken by another user
            if (!updatedUser.getEmail().equalsIgnoreCase(existing.getEmail()) && dao.emailExists(updatedUser.getEmail())) {
                return "VALIDATION_ERROR: This email is already in use.";
            }

            if (dao.updateUser(updatedUser)) {
                return "SUCCESS: Profile updated successfully!";
            } else {
                return "DATABASE_ERROR: Could not save changes.";
            }
        } catch (DateTimeParseException e) {
            return "VALIDATION_ERROR: Invalid date format. Use YYYY-MM-DD.";
        }
    }
}