package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class NoticeManagementPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public NoticeManagementPanel() {

        setBackground(Color.WHITE);


        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        add(createNoticeButton("Create Notice"), gbc);
        add(createNoticeButton("Update Notice"), gbc);
        add(createNoticeButton("Delete Notice"), gbc);
        add(createNoticeButton("View Notice"), gbc);
    }

    private JButton createNoticeButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 80));
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            System.out.println(text + " Clicked");
        });

        return btn;
    }
}