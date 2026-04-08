package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard  extends javax.swing.JFrame {
    private JPanel sidebar, contentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);// Align middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Sidebar
        sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(200, 700));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));

        // 2. Content Panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new CardLayout());

        // add sidebar buttons
        JButton btnUser = new JButton("User Management");
        btnUser.setBackground(new Color(52, 152, 219)); // set color blue
        btnUser.setForeground(Color.WHITE);
        JButton btnCourse = new JButton("Course Management");
        btnCourse.setBackground(new Color(52, 152, 219)); // set color blue
        btnCourse.setForeground(Color.WHITE);
        JButton btnNotice = new JButton("Notice Management");
        btnNotice.setBackground(new Color(52, 152, 219)); // set color blue
        btnNotice.setForeground(Color.WHITE);
        JButton btnTimetable = new JButton("Timetable Management");
        btnTimetable.setBackground(new Color(52, 152, 219)); // set color blue
        btnTimetable.setForeground(Color.WHITE);
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(52, 152, 219)); // set color blue
        btnLogout.setForeground(Color.WHITE);

        sidebar.add(btnUser);
        sidebar.add(btnCourse);
        sidebar.add(btnNotice);
        sidebar.add(btnTimetable);
        sidebar.add(btnLogout);

        // when click the Course management button
        btnCourse.addActionListener(e -> {
            // create new AdminCourseManagement
            AdminCourseManagementPanel courseMgmt = new AdminCourseManagementPanel();
            // visible in screen
            courseMgmt.setVisible(true);
            // close before window
            this.dispose();
        });

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}
