package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminCourseManagementPanel extends javax.swing.JFrame {
    private JPanel sidebar, contentPanel, menuPanel, addCoursePanel;
    private JButton btnAddCourse, btnUpdateCourse, btnDeleteCourse;
    private CardLayout cardLayout = new CardLayout(); // Panel මාරු කිරීමට

    public AdminCourseManagementPanel() {
        setTitle("Admin Dashboard - Course Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Sidebar (ස්ථාවර කොටස) ---
        sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setLayout(new GridLayout(10, 1, 0, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] navButtons = {"User Management", "Course Management", "Notice Management", "Timetable Management", "Logout"};
        for (String text : navButtons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 1));

            // Hover Effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(52, 152, 219)); }
                public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(52, 73, 94)); }
            });
            sidebar.add(btn);
        }

        // Content Panel
        contentPanel = new JPanel(cardLayout);

        //  Menu Panel(Add, Update, Delete Buttons)
        menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        btnAddCourse = new JButton("Add New Course");
        styleButton(btnAddCourse);

        // Button එක Click කල විට Form එක පෙන්වීමට Action එක
        btnAddCourse.addActionListener(e -> cardLayout.show(contentPanel, "AddForm"));

        btnUpdateCourse = new JButton("Update Course");
        styleButton(btnUpdateCourse);

        btnDeleteCourse = new JButton("Delete Course");
        styleButton(btnDeleteCourse);

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(btnAddCourse);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnUpdateCourse);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnDeleteCourse);
        menuPanel.add(Box.createVerticalGlue());

        // 2. අලුතින් Course එකක් එකතු කරන Form Panel එක
        addCoursePanel = createAddCoursePanel();

        // CardLayout එකට Panel දෙක එකතු කිරීම
        contentPanel.add(menuPanel, "Menu");
        contentPanel.add(addCoursePanel, "AddForm");

        // Main Layout එකට සම්බන්ධ කිරීම
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Menu"); // Default එක Menu එක පෙන්වන්න
    }

    // Course Add කිරීමේ Form එක නිර්මාණය කරන Method එක
    private JPanel createAddCoursePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Course Code:", "Course Name:", "Type:", "Credits:", "Lecturer ID:", "Dept ID:"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            panel.add(fields[i], gbc);
        }

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton btnSave = new JButton("Save Course");
        btnSave.setBackground(new Color(46, 204, 113)); // Green
        btnSave.setForeground(Color.WHITE);

        JButton btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> cardLayout.show(contentPanel, "Menu")); // back to menu

        btnPanel.add(btnSave);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setPreferredSize(new Dimension(500, 100));
        btn.setMaximumSize(new Dimension(1000, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminCourseManagementPanel().setVisible(true);
        });
    }
}