package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class NoticeManagementPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public NoticeManagementPanel() {

        setBackground(Color.WHITE);
        showMainButtons();
    }

    public void showMainButtons() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnCreate = createNoticeButton("Create Notice");

        btnCreate.addActionListener(e -> {
            loadCreateNoticePanel();
        });

        add(btnCreate, gbc);
        add(createNoticeButton("Update Notice"), gbc);
        add(createNoticeButton("Delete Notice"), gbc);

        JButton btnView = createNoticeButton("View Notice");
        btnView.addActionListener(e -> loadViewNoticePanel());
        add(btnView, gbc);

        this.revalidate();
        this.repaint();
    }

    private void loadCreateNoticePanel() {
        this.removeAll(); // remove buttons
        this.setLayout(new BorderLayout());
        this.add(new CreateNotice(this)); // add White Space  to new panel
        this.revalidate();
        this.repaint();
    }


    private void loadViewNoticePanel() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(new ViewNotice(this)); // 'this' pass කරන්න අමතක කරන්න එපා
        this.revalidate();
        this.repaint();
    }


    private JButton createNoticeButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 80));
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));



        return btn;
    }
}