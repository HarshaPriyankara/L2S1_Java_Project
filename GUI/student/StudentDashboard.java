package GUI.student;

import GUI.common.BaseDashboard;
import GUI.common.ViewNotice;
import javax.swing.*;
import java.awt.*;
import Models.User;

// Inheritance: StudentDashboard inherits from BaseDashboard
public class StudentDashboard extends BaseDashboard {

    public StudentDashboard(User user) {
        // Pass the whole user object to the parent
        super("Lecture Dashboard", user);
    }

    /**
     * Abstraction: Registering all the panels specific to the student
     * as required by the project document.
     */
    @Override
    protected void setupUserPanels() {
        // These classes (UpdateProfilePanel, AttendancePanel, etc.) should exist in your project
        contentPanel.add(new UpdateProfilePanel(), "Update Profile");
        contentPanel.add(new AttendancePanel(), "Attendance Details");
        contentPanel.add(new MedicalPanel(), "Medical Details");
        contentPanel.add(new CoursePanel(loggedInID), "Course Details");
        contentPanel.add(new GradePanel(), "Grades/GPA");
        contentPanel.add(new TimetablePanel(), "Timetable Details");

        // ViewNotice is a shared component used by multiple user roles
        contentPanel.add(new ViewNotice("Undergraduate", contentPanel, cardLayout), "Notice");
    }

    /**
     * Abstraction: Adding buttons to the sidebar that match the
     * Student's permissions[cite: 38, 39, 40, 41, 42, 43, 44].
     */
    @Override
    protected void addNavigationButtons(JPanel sidebar) {

        sidebar.add(createNavButton("Update Profile",
                () -> cardLayout.show(contentPanel, "Update Profile")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Attendance Details",
                () -> cardLayout.show(contentPanel, "Attendance Details")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Medical Details",
                () -> cardLayout.show(contentPanel, "Medical Details")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Course Details",
                () -> cardLayout.show(contentPanel, "Course Details")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Grades/GPA",
                () -> cardLayout.show(contentPanel, "Grades/GPA")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Timetable Details",
                () -> cardLayout.show(contentPanel, "Timetable Details")));

        sidebar.add(Box.createVerticalStrut(12));

        sidebar.add(createNavButton("Notice",
                () -> cardLayout.show(contentPanel, "Notice")));
    }

    /**
     * Polymorphism: Overriding the welcome message specifically for students.
     */
    @Override
    protected JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Welcome to Student Portal");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x555555));

        p.add(lbl);
        return p;
    }

    // Main method to test the Student Dashboard alone
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
            new StudentDashboard(testUser).setVisible(true);
        });
    }
}