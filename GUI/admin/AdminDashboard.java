package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard  extends javax.swing.JFrame {
    private JPanel sidebar, contentPanel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(650, 700);
        setLocationRelativeTo(null);// Align middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Sidebar එක (වම් පැත්තේ ඇති මෙනු එක)
        sidebar = new JPanel();
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(200, 700));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5)); // බොත්තම් පේළියට තියන්න

        // 2. Content Panel එක (මැද ඇති ප්‍රධාන කොටස)
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new CardLayout()); // පැනල් මාරු කරන්න CardLayout හොඳයි

        // Sidebar එකට Buttons එකතු කිරීම
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

        add(sidebar, BorderLayout.WEST);
        /*add(contentPanel, BorderLayout.CENTER);*/
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("Images/admin.jpg"); // your image path
        Image img = icon.getImage().getScaledInstance(450, 700, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));

        add(imageLabel, BorderLayout.CENTER);
    }

   public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}
