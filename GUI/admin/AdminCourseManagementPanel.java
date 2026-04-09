package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AdminCourseManagementPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    // Sub-card names
    private static final String MENU   = "CourseMenu";
    private static final String ADD    = "AddForm";
    private static final String UPDATE = "UpdateForm";
    private static final String DELETE = "DeleteForm";

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
        // NoticeManagementPanel eke wage GridBagLayout use kara
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0); // Buttons athara gap eka
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buttons tika add kirima
        menu.add(courseButton("Add New Course",  () -> innerLayout.show(innerPanel, ADD)), gbc);
        menu.add(courseButton("Update Course",   () -> innerLayout.show(innerPanel, UPDATE)), gbc);
        menu.add(courseButton("Delete Course",   () -> innerLayout.show(innerPanel, DELETE)), gbc);

        return menu;
    }

    private JButton courseButton(String text, Runnable action) {
        // NoticeManagementPanel eke createNoticeButton ekatama samana kara
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 80)); // Size eka 80 ta adu kara
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> action.run());
        return btn;
    }
}