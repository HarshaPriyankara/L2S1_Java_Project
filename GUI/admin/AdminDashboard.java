package GUI.admin;

import GUI.common.BaseDashboard;
import GUI.common.UITheme;
import Models.User;
import javax.swing.*;
import java.awt.*;

// Inheritance: AdminDashboard inherits common features from BaseDashboard
public class AdminDashboard extends BaseDashboard {

    public AdminDashboard(User user) {
        // Pass the whole user object to the parent
        super("Admin Dashboard", user);
    }
    /**
     * Abstraction: Implementing the mandatory method to register
     * specific Admin panels into the CardLayout.
     */
    @Override
    protected void setupUserPanels() {
        // Adding the panels required by the project document
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
        p.setBackground(UITheme.APP_BACKGROUND);

        JLabel lbl = new JLabel("Admin Control Center");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(UITheme.TEXT_PRIMARY);

        p.add(lbl);
        return p;
    }

    // Main method for testing this specific dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create a temporary User object for testing
            User testUser = new User();

            // 2. Set the necessary data
            testUser.setUserID("adm001");
            testUser.setRole("Admin");
            testUser.setFname("Admin");
            testUser.setLname("User");

            // 3. Pass the object to the constructor
            new AdminDashboard(testUser).setVisible(true);
        });
    }
}
