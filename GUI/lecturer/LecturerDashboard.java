package GUI.lecturer;

import GUI.common.BaseDashboard;
import GUI.common.ViewNotice;
import javax.swing.*;
import java.awt.*;

// Inheritance: LecturerDashboard inherits common GUI logic from BaseDashboard
public class LecturerDashboard extends BaseDashboard {

    public LecturerDashboard(String loggedInID) {
        // Calls the BaseDashboard constructor to set the title and user ID
        super("Lecturer Dashboard - Faculty of Technology", loggedInID);
    }

    /**
     * Abstraction: Implementing the method to register panels specific
     * to the Lecturer as required by the project document.
     */
    @Override
    protected void setupUserPanels() {
        // These are the panels for tasks a lecturer must perform
        contentPanel.add(new MarksManagement(), "MarksManagement");
        contentPanel.add(new AddCourseMaterialPanel(), "AddMaterial");
        contentPanel.add(new ProfileManagementPanel(loggedInID), "UpdateProfile");

        // Common panel for viewing notices
        contentPanel.add(new ViewNotice("Lecturer", contentPanel, cardLayout), "ViewNotice");

        // Add other panels like StudentDetails or Eligibility as you create them
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
            new LecturerDashboard("LEC001").setVisible(true);
        });
    }
}