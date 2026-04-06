package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminCourseManagement extends javax.swing.JFrame {
    private JPanel sidebar, contentPanel, formPanel;
    private JButton btnAddCourse, btnUpdateCourse, btnDeleteCourse;

    public AdminCourseManagement() {
        setTitle("Admin Dashboard - Course Management");
        setSize(1000, 600);
        setLocationRelativeTo(null); // center the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // sidebar
        sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));

        // sidebar buttons
        String[] navButtons = {"User Management", "Course Management", "Notice Management", "Timetable Management", "Logout"};
        for (String text : navButtons) {
            JButton btn = new JButton(text);
            btn.setBackground(new Color(52, 152, 100)); // button color
            btn.setBackground(new Color(52, 152, 219));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            sidebar.add(btn);
        }

        // content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout());

        // Form Panel
        formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        formPanel.add(Box.createVerticalGlue());

        // Add Course Button
        btnAddCourse = new JButton("Add New Course");
        styleButton(btnAddCourse);
        formPanel.add(btnAddCourse);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // gap between two button

        btnUpdateCourse = new JButton("Update Course");
        styleButton(btnUpdateCourse);
        formPanel.add(btnUpdateCourse);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // gap between two button

        btnDeleteCourse = new JButton("Delete Course");
        styleButton(btnDeleteCourse);
        formPanel.add(btnDeleteCourse);

        formPanel.add(Box.createVerticalGlue());
        contentPanel.add(formPanel, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setPreferredSize(new Dimension(500, 100));
        btn.setMaximumSize(new Dimension(1000, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // hover mouse hand icon
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminCourseManagement().setVisible(true);
        });
    }
}