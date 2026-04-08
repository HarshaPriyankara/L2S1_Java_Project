package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AddUserPanel extends JFrame {
    private static final Color DARK_BG       = new Color(0x2E2E2E);
    private static final Color NAV_BTN_COLOR = new Color(0x2E7DC0);
    private static final Color CARD_COLOR    = new Color(0x7DC4EC);
    private static final Color WHITE         = Color.WHITE;

    public  AddUserPanel() {
        // JFrame setup
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 24, 24, 24));

        String[] navLabels = {"User Management", "Course Management", "Notice Management", "Timetable Management", "Logout"};
        for (String label : navLabels) {
            sidebar.add(createSimpleButton(label));
            sidebar.add(Box.createVerticalStrut(16));
        }

        // Content
        JPanel content = new JPanel();
        content.setBackground(WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        String[] cards = {"Create New User", "Update user Details", "Delete User"};
        for (String title : cards) {
            content.add(createSimpleCard(title));
            content.add(Box.createVerticalStrut(24));
        }

        // Add panels
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createSimpleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(46, 125, 192));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createSimpleCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(new Color(125, 196, 236));
        card.setPreferredSize(new Dimension(400, 80));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        card.add(label, BorderLayout.CENTER);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddUserPanel::new);
    }
}