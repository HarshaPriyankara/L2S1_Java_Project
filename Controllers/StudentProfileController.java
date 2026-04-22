package Controllers;

import DAO.UserDAO;
import Models.User;

public class StudentProfileController {
    private final UserDAO dao = new UserDAO();

    public User getStudentData(String userId) {
        return dao.getUserById(userId);
    }

    public String updateStudentProfile(String userId, String email, String contact, String address, String photoPath) {
        // Validation for new allowed field: Email
        if (email.trim().isEmpty() || contact.trim().isEmpty() || address.trim().isEmpty()) {
            return "VALIDATION_ERROR: Email, Contact, and Address are required.";
        }

        User existing = dao.getUserById(userId);
        if (existing == null) return "DATABASE_ERROR: User not found.";

        // Logic: Check if the new email is already taken by someone else
        if (!email.equalsIgnoreCase(existing.getEmail()) && dao.emailExists(email)) {
            return "VALIDATION_ERROR: This email is already in use by another account.";
        }

        // Update allowed fields
        existing.setEmail(email.trim());
        existing.setContactNo(contact.trim());
        existing.setAddress(address.trim());
        existing.setProfilePicPath(photoPath.trim());
        existing.setOriginalUserID(userId);

        if (dao.updateUser(existing)) {
            return "SUCCESS: Profile details updated!";
        } else {
            return "DATABASE_ERROR: Failed to save changes.";
        }
    }
}