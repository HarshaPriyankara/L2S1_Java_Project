package GUI.admin;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class AdminCourseManagementPanel extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    // Sub-card names
    private static final String MENU   = "CourseMenu";
    private static final String ADD    = "AddForm";
    private static final String UPDATE = "UpdateForm";
    private static final String DELETE = "DeleteForm";

    private final CardLayout innerLayout = new CardLayout();
    private final JPanel     innerPanel  = new JPanel(innerLayout);

    public AdminCourseManagementPanel(JPanel rootContent, CardLayout rootCard) {
        setLayout(new BorderLayout());
        setBackground(UITheme.APP_BACKGROUND);
        innerPanel.setBackground(Color.WHITE);

        // Build sub-panels
        innerPanel.add(buildMenuPanel(),  MENU);
        innerPanel.add(new AddNewCoursePanel(innerPanel, innerLayout),    ADD);
        innerPanel.add(new UpdateCoursePanel(innerPanel, innerLayout),    UPDATE);
        innerPanel.add(new DeleteCoursePanel(innerPanel, innerLayout),    DELETE);

        add(innerPanel, BorderLayout.CENTER);
        innerLayout.show(innerPanel, MENU);
    }

    private JPanel buildMenuPanel() {
        JPanel menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        menu.add(courseCard("Add New Course", () -> innerLayout.show(innerPanel, ADD)));
        menu.add(Box.createVerticalStrut(20));
        menu.add(courseCard("Update Course", () -> innerLayout.show(innerPanel, UPDATE)));
        menu.add(Box.createVerticalStrut(20));
        menu.add(courseCard("Delete Course", () -> innerLayout.show(innerPanel, DELETE)));
        return menu;
    }

    private JPanel courseCard(String text, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
        return card;
    }
}
