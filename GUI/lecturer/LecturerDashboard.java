package GUI.lecturer;

import GUI.common.BaseDashboard;
import GUI.common.ProfileManagementPanel;
import GUI.common.ViewNotice;
import javax.swing.*;
import java.awt.*;
import Models.User;

// Inheritance: LecturerDashboard inherits common GUI logic from BaseDashboard
public class LecturerDashboard extends BaseDashboard {



    public LecturerDashboard(User user) {
        // Pass the whole user object to the parent
        super("Lecture Dashboard", user);
    }

    /**
     * Abstraction: Implementing the method to register panels specific
     * to the Lecturer as required by the project document.
     */
    @Override
    protected void setupUserPanels() {
        // These are the panels for tasks a lecturer must perform
        contentPanel.add(new MarksManagement(loggedInID), "MarksManagement");
        contentPanel.add(new AddCourseMaterialPanel(loggedInID), "AddMaterial");
        contentPanel.add(new ProfileManagementPanel(loggedInID, false), "UpdateProfile");

        // Common panel for viewing notices
        contentPanel.add(new ViewNotice("Lecturer", contentPanel, cardLayout), "ViewNotice");

        contentPanel.add(new StudentDetails(loggedInID), "StudentDetails");

    }

    /**
     * Abstraction: Adding buttons to the sidebar that match the
     * Lecturer's permissions in the project requirements.
     */
    @Override
    protected void addNavigationButtons(JPanel sidebar) {

        // Requirement: Upload marks for exams
        sidebar.add(createNavButton("Upload Marks",
                () -> cardLayout.show(contentPanel, "MarksManagement")));

        sidebar.add(Box.createVerticalStrut(12));

        // Requirement: Add materials to courses
        sidebar.add(createNavButton("Add Course Materials",
                () -> cardLayout.show(contentPanel, "AddMaterial")));

        sidebar.add(Box.createVerticalStrut(12));

        // Requirement: See notices
        sidebar.add(createNavButton("View Notices",
                () -> cardLayout.show(contentPanel, "ViewNotice")));

        sidebar.add(Box.createVerticalStrut(12));

        // Requirement: Update profile
        sidebar.add(createNavButton("Update Profile",
                () -> cardLayout.show(contentPanel, "UpdateProfile")));

        sidebar.add(Box.createVerticalStrut(12));


        sidebar.add(createNavButton("Student Records",
                () -> cardLayout.show(contentPanel, "StudentDetails")));
    }

    /**
     * Polymorphism: Providing a specific welcome message for the Lecturer.
     */
    @Override
    protected JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Welcome, Lecturer");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x555555));

        p.add(lbl);
        return p;
    }

    // Main method to test this dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create a temporary User object for testing
            User testUser = new User();

            // 2. Set the necessary data
            testUser.setUserID("adm001");
            testUser.setRole("lecture");
            testUser.setFname("Admin");
            testUser.setLname("User");

            // 3. Pass the object to the constructor
            new LecturerDashboard(testUser).setVisible(true);
        });
    }
}
