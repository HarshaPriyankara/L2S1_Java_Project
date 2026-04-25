package GUI.lecturer;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDetails extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    public StudentDetails(String lecturerId) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JLabel titleLabel = new JLabel("Student Records");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        JLabel subtitleLabel = new JLabel("Select the record area you want to manage.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(28));

        contentPanel.add(createMenuCard("Student Attendance"));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createMenuCard("Student Marks"));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createMenuCard("Student Eligibility"));

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createMenuCard(String title) {
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
                JOptionPane.showMessageDialog(
                        StudentDetails.this,
                        title + " view will be implemented in the next step.",
                        "Student Records",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        return card;
    }
}
