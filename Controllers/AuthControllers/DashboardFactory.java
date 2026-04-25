package Controllers.AuthControllers;

import GUI.admin.AdminDashboard;
import GUI.lecturer.LecturerDashboard;
import GUI.student.StudentDashboard;
import GUI.to.TechnicalOfficerDashboard;
import Models.User;

import javax.swing.JFrame;

public class DashboardFactory {
    public JFrame createDashboard(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        switch (user.getRoleKey()) {
            case "admin":
                return new AdminDashboard(user);
            case "lecturer":
                return new LecturerDashboard(user);
            case "student":
                return new StudentDashboard(user);
            case "techofficer":
                return new TechnicalOfficerDashboard(user);
            default:
                throw new IllegalArgumentException("Unsupported role: " + user.getDisplayRoleName());
        }
    }
}
