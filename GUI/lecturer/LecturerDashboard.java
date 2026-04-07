package GUI.lecturer;

import javax.swing.*;
import java.awt.*;

public class LecturerDashboard extends javax.swing.JFrame {
    private JPanel sidebar, contentPanel;

    public LecturerDashboard() {
        setTitle("Lecturer Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);// Align middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Sidebar එක (වම් පැත්තේ ඇති මෙනු එක)
        sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5)); // බොත්තම් පේළියට තියන්න

        // 2. Content Panel එක (මැද ඇති ප්‍රධාන කොටස)
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new CardLayout()); // පැනල් මාරු කරන්න CardLayout හොඳයි

        // Sidebar එකට Buttons එකතු කිරීම
        JButton btnAddMarks = new JButton("Upload Marks");
        JButton btnAddMaterial = new JButton("Add Courses Materials");
        JButton btnViewStudent = new JButton("View Students Details");
        JButton btnLogout = new JButton("Logout");

        sidebar.add(btnAddMarks);
        sidebar.add(btnAddMaterial);
        sidebar.add(btnViewStudent);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
