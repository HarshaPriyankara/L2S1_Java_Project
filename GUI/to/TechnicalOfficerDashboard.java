package GUI.to;

import GUI.common.BaseDashboard;
import GUI.common.ViewNotice;
import javax.swing.*;
import java.awt.*;

// Inheritance: This class inherits all common GUI features from BaseDashboard
public class TechnicalOfficerDashboard extends BaseDashboard {

    public TechnicalOfficerDashboard(String loggedInID) {
        // Calls the BaseDashboard constructor with the specific title and user ID
        super("Technical Officer Dashboard - Faculty of Technology", loggedInID);
    }

    /**
     * Abstraction: Implementing the mandatory method to register
     * Technical Officer specific panels as defined in the project requirements.
     */
    @Override
    protected void setupUserPanels() {
        // Adding the panels required by the project document [cite: 31, 33, 34]
        contentPanel.add(new AttendanceManagement(this), "Attendance");
        contentPanel.add(new MedicalManagement(this), "Medical");
        contentPanel.add(new Timetable(this), "Timetable");

        // Using the common ViewNotice panel used by other users
        contentPanel.add(new ViewNotice("Technical Officer", contentPanel, cardLayout), "Notices");
    }

    /**
     * Abstraction: Adding buttons to the sidebar that match the
     * Technical Officer's job role.
     */
    @Override
    protected void addNavigationButtons(JPanel sidebar) {
        // Buttons are added in order from top to bottom

        sidebar.add(createNavButton("Attendance Management",
                () -> cardLayout.show(contentPanel, "Attendance")));

        sidebar.add(Box.createVerticalStrut(12)); // Consistent spacing

        sidebar.add(createNavButton("Medical Management",
                () -> cardLayout.show(contentPanel, "Medical")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("View Timetable",
                () -> cardLayout.show(contentPanel, "Timetable")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("View Notices",
                () -> cardLayout.show(contentPanel, "Notices")));

        // The Logout button is automatically added at the bottom by BaseDashboard
    }

    /**
     * Polymorphism: Providing a specific welcome screen for the Technical Officer.
     */
    @Override
    protected JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Technical Officer Portal");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x444444));

        p.add(lbl);
        return p;
    }

    // Main method for testing this dashboard independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TechnicalOfficerDashboard("to001").setVisible(true);
        });
    }
}