package GUI.admin;

import GUI.common.BaseDashboard;
import javax.swing.*;
import java.awt.*;

// Inheritance: AdminDashboard inherits common features from BaseDashboard
public class AdminDashboard extends BaseDashboard {

    public AdminDashboard(String loggedInID) {
        // Calls the constructor of BaseDashboard to set the title and ID
        super("Admin Dashboard - Faculty of Technology", loggedInID);
    }

    /**
     * Abstraction: Implementing the mandatory method to register
     * specific Admin panels into the CardLayout.
     */
    @Override
    protected void setupUserPanels() {
        // Adding the panels required by the project document [cite: 11, 18]
        contentPanel.add(new UserManagementPanel(), "UserManagement");
        contentPanel.add(new AdminCourseManagementPanel(contentPanel, cardLayout), "CourseManagement");
        contentPanel.add(new NoticeManagementPanel(), "NoticeManagement");
        contentPanel.add(new TimetableManagement(), "TimetableManagement");

        // You can add more panels here as you develop them
    }

    /**
     * Abstraction: Implementing the mandatory method to add
     * Admin-specific navigation buttons to the sidebar.
     */
    @Override
    protected void addNavigationButtons(JPanel sidebar) {
        // These buttons match the Admin requirements in your document [cite: 19, 20, 21, 22]

        sidebar.add(createNavButton("User Management",
                () -> cardLayout.show(contentPanel, "UserManagement")));

        sidebar.add(Box.createVerticalStrut(12)); // Spacing

        sidebar.add(createNavButton("Course Management",
                () -> cardLayout.show(contentPanel, "CourseManagement")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Notice Management",
                () -> cardLayout.show(contentPanel, "NoticeManagement")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Timetable Management",
                () -> cardLayout.show(contentPanel, "TimetableManagement")));
    }

    /**
     * Encapsulation: We can override the home panel if we want
     * a specific welcome message for the Admin.
     */
    @Override
    protected JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Admin Control Center");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x333333));

        p.add(lbl);
        return p;
    }

    // Main method for testing this specific dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Assume "adm001" is passed from the Login Form
            new AdminDashboard("adm001").setVisible(true);
        });
    }
}