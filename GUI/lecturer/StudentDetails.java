package GUI.lecturer;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDetails extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    public StudentDetails(String lecturerId) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        contentPanel.add(buildMenuPanel(), "Menu");
        contentPanel.add(new StudentAttendancePanel(lecturerId, () -> cardLayout.show(contentPanel, "Menu")), "Attendance");
        contentPanel.add(new StudentMarksPanel(lecturerId, () -> cardLayout.show(contentPanel, "Menu")), "Marks"); // pass lecturer id and menu panel to studentMarksPanel
        contentPanel.add(new StudentGpaPanel(() -> cardLayout.show(contentPanel, "Menu")), "Gpa");

        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "Menu");
    }

    /// @author dilusha
    private JPanel buildMenuPanel() {
        //main GUI
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(Color.WHITE);

        //for main BG
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        //title contents
        JLabel titleLabel = new JLabel("Student Records");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(12));

        JLabel subtitleLabel = new JLabel("Select the record area you want to manage.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitleLabel);
        content.add(Box.createVerticalStrut(28));

        //3 buttons
        content.add(createMenuCard("Student Attendance", () -> cardLayout.show(contentPanel, "Attendance")));
        content.add(Box.createVerticalStrut(20));
        content.add(createMenuCard("Student Marks", () -> cardLayout.show(contentPanel, "Marks")));
        content.add(Box.createVerticalStrut(20));
        content.add(createMenuCard("SGPA and CGPA", () -> cardLayout.show(contentPanel, "Gpa")));

        menuPanel.add(content, BorderLayout.CENTER);
        return menuPanel;
    }

    /// @author dilusha
    private JPanel createMenuCard(String title, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return card;
    }

}
