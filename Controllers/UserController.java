package Controllers;

import DAO.UserDAO;
import Models.User;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserController {
    private final UserDAO dao = new UserDAO();

    // Logic for creating a user
    public String registerUser(User user, String rawDob) {
        if (isAnyFieldEmpty(user, rawDob)) {
            return "VALIDATION_ERROR: Please fill in all fields.";
        }

        try {
            user.setDob(LocalDate.parse(rawDob.trim()));
            if (dao.createUser(user)) {
                return "SUCCESS: User saved successfully!";
            } else {
                return "DATABASE_ERROR: Failed to save. ID or Email may exist.";
            }
        } catch (DateTimeParseException e) {
            return "VALIDATION_ERROR: Invalid date format. Use YYYY-MM-DD.";
        }
    }

    // Logic for fetching a user
    public User fetchUser(String id) {
        if (id == null || id.trim().isEmpty()) return null;
        return dao.getUserById(id.trim());
    }

    // Logic for updating a user
    public String processUpdate(User user, String rawDob, String originalId, String newPw) {
        if (user.getFname().isEmpty() || user.getEmail().isEmpty() || rawDob.isEmpty()) {
            return "VALIDATION_ERROR: Basic information is missing.";
        }
        try {
            user.setDob(LocalDate.parse(rawDob.trim()));
            user.setOriginalUserID(originalId);

            // Use existing password if the field was left blank
            if (newPw.trim().isEmpty()) {
                user.setPassword(dao.getUserById(originalId).getPassword());
            } else {
                user.setPassword(newPw.trim());
            }

            return dao.updateUser(user) ? "SUCCESS: All details updated!" : "DATABASE_ERROR: Failed to save.";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public String deleteUser(String id) {
        if (id.isEmpty()) return "VALIDATION_ERROR: ID is required.";
        if (!dao.userExists(id)) return "NOT_FOUND: User does not exist.";

        return dao.deleteUser(id) ? "SUCCESS: User deleted." : "DATABASE_ERROR: Delete failed.";
    }

    private boolean isAnyFieldEmpty(User u, String dob) {
        return u.getUserID().isEmpty() || u.getFname().isEmpty() ||
                u.getEmail().isEmpty() || dob.trim().isEmpty();
    }
}