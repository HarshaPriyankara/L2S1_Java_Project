package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard  extends javax.swing.JFrame {
    private static final Color darkBg      = new Color(0x2E2E2E);
    private static final Color buttonColor = new Color(46, 125, 192);


    private JPanel sidebar, contentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);// Align middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(darkBg);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 24, 24, 24));

        JButton btnUser      = createNavButton("User Management");
        JButton btnCourse    = createNavButton("Course Management");
        JButton btnNotice    = createNavButton("Notice Management");
        JButton btnTimetable = createNavButton("Timetable Management");
        JButton btnLogout    = createNavButton("Logout");

        sidebar.add(btnUser);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnCourse);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnNotice);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnTimetable);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnLogout);



        // 2. Content Panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new CardLayout());


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

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(buttonColor);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}
