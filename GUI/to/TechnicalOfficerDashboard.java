package GUI.to;

import javax.swing.*;
import java.awt.*;

public class TechnicalOfficerDashboard extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public TechnicalOfficerDashboard(String loggedInID) {
        setTitle("Technical Officer Dashboard - Faculty of Technology");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar (Dark Theme)
        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        JButton btnAttendance = createSidebarButton("Attendance Management");
        JButton btnMedical = createSidebarButton("Medical Management");
        JButton btnNotices = createSidebarButton("View Notices");
        JButton btnTimetable = createSidebarButton("View Timetable");
        JButton btnLogout = createSidebarButton("Logout");
        btnLogout.setBackground(new Color(66, 133, 244));

        btnAttendance.addActionListener(e -> switchPage("Attendance"));
        btnMedical.addActionListener(e -> switchPage("Medical"));
        btnNotices.addActionListener(e -> switchPage("Notices"));
        btnTimetable.addActionListener(e -> switchPage("Timetable"));
        btnLogout.addActionListener(e -> System.exit(0));

        sidebar.add(btnAttendance, gbc);
        sidebar.add(btnMedical, gbc);
        sidebar.add(btnNotices, gbc);
        sidebar.add(btnTimetable, gbc);
        sidebar.add(btnLogout, gbc);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new AttendanceManagement(this), "Attendance");
        contentPanel.add(new MedicalManagement(this), "Medical");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);

        contentPanel.add(new Notices(this), "Notices");
        contentPanel.add(new Timetable(this), "Timetable");
    }

    public void switchPage(String pageName) {
        cardLayout.show(contentPanel, pageName);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(280, 50));
        btn.setBackground(new Color(66, 133, 244));
        btn.setForeground(Color.WHITE); // අකුරු සුදු පාටයි
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TechnicalOfficerDashboard("to001").setVisible(true));
    }
}