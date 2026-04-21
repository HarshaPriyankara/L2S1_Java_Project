package GUI.admin;

import GUI.common.LoginForm;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private static final Color DARK_BG      = new Color(0x2E2E2E);
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel   = new JPanel(cardLayout);

    public AdminDashboard(String loggedInID) {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(contentPanel,   BorderLayout.CENTER);

        // Register all top-level panels once
        contentPanel.add(new UserManagementPanel(),   "UserManagement");
        contentPanel.add(new AdminCourseManagementPanel(contentPanel, cardLayout), "CourseManagement");
        contentPanel.add(new NoticeManagementPanel(),                "NoticeManagement");    // placeholder
        contentPanel.add(new TimetableManagement(), "TimetableManagement");
        contentPanel.add(buildHomePanel(),            "Home");

        cardLayout.show(contentPanel, "Home");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 16, 24, 16));

        sidebar.add(navButton("User Management",      () -> cardLayout.show(contentPanel, "UserManagement")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Course Management",    () -> cardLayout.show(contentPanel, "CourseManagement")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Notice Management",    () -> cardLayout.show(contentPanel, "NoticeManagement")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Timetable Management", () -> cardLayout.show(contentPanel, "TimetableManagement")));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navButton("Logout", () -> {
            new LoginForm().setVisible(true); // open login form
            dispose(); // close current window
        }));

        return sidebar;
    }

    private JButton navButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BUTTON_COLOR);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Welcome, Admin");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x555555));
        p.add(lbl);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard("adm01").setVisible(true));
    }
}