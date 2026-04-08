package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class NoticeDashboard extends JFrame {

    public NoticeDashboard() {
        setTitle("Admin Dashboard - Notice Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        // Sidebar Navigation Buttons
        String[] navItems = {"User Management", "Course Management", "Notice Management", "Timetable Management", "Logout"};
        for (String item : navItems) {
            JButton navBtn = createSidebarButton(item);
            sidebar.add(navBtn);
        }
        add(sidebar, BorderLayout.WEST);

        // --- MAIN CONTENT AREA ---
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout()); // Using GridBag for centered stacking
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0); // Spacing between buttons
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Action Buttons (Notice specific)
        contentPanel.add(createActionButton("Create Notice"), gbc);
        contentPanel.add(createActionButton("Update Notice"), gbc);
        contentPanel.add(createActionButton("Delete Notice"), gbc);
        contentPanel.add(createActionButton("View Notice"), gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Helper method to style Sidebar buttons
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    // Helper method to style the Large Action buttons (Blue ones)
    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 80));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22)); // Large font
        return btn;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new NoticeDashboard().setVisible(true);
        });
    }
}