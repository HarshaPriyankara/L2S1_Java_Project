package GUI.lecturer;

import GUI.common.LoginForm;
import GUI.common.ViewNotice;
import javax.swing.*;
import java.awt.*;

public class LecturerDashboard extends JFrame {

    private static final Color DARK_BG      = new Color(0x2E2E2E);
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel   = new JPanel(cardLayout);

    public LecturerDashboard(String loggedInUserId) {
        setTitle("Lecturer Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(contentPanel,   BorderLayout.CENTER);

        contentPanel.add(buildHomePanel(),                          "Home");
        contentPanel.add(new ProfileManagementPanel(loggedInUserId), "Update Profile");
        contentPanel.add(new ViewNotice("Lecturer",contentPanel,cardLayout), "View Notice Card");
        cardLayout.show(contentPanel, "Home");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 16, 24, 16));

        sidebar.add(navButton("Upload Marks",         () -> cardLayout.show(contentPanel, "Home")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Add Course Materials", () -> cardLayout.show(contentPanel, "Home")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("View Student Details", () -> cardLayout.show(contentPanel, "Home")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Undergraduate Details", () -> cardLayout.show(contentPanel, "Undergraduate Details")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("View Notice", () -> cardLayout.show(contentPanel, "View Notice Card")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Update Profile",       () -> cardLayout.show(contentPanel, "Update Profile")));

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(navButton("Logout", () -> {
            new LoginForm().setVisible(true);
            dispose();
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
        JLabel lbl = new JLabel("Welcome, Lecturer");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x555555));
        p.add(lbl);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LecturerDashboard("L001").setVisible(true));
    }
}