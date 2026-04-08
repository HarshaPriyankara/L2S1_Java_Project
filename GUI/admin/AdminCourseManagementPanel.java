package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminCourseManagementPanel extends JPanel {

    // Sub-card names
    private static final String MENU   = "CourseMenu";
    private static final String ADD    = "AddForm";
    private static final String UPDATE = "UpdateForm";
    private static final String DELETE = "DeleteForm";

    // Inner card layout (just for this panel's sub-views)
    private final CardLayout innerLayout = new CardLayout();
    private final JPanel     innerPanel  = new JPanel(innerLayout);

    public AdminCourseManagementPanel(JPanel rootContent, CardLayout rootCard) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

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
        menu.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        menu.add(Box.createVerticalGlue());
        menu.add(courseButton("Add New Course",  () -> innerLayout.show(innerPanel, ADD)));
        menu.add(Box.createRigidArea(new Dimension(0, 20)));
        menu.add(courseButton("Update Course",   () -> innerLayout.show(innerPanel, UPDATE)));
        menu.add(Box.createRigidArea(new Dimension(0, 20)));
        menu.add(courseButton("Delete Course",   () -> innerLayout.show(innerPanel, DELETE)));
        menu.add(Box.createVerticalGlue());

        return menu;
    }

    private JButton courseButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 100));
        btn.setMaximumSize(new Dimension(1000, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }
}