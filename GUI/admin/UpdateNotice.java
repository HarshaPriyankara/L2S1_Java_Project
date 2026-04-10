package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class UpdateNotice extends JPanel {
    private NoticeManagementPanel parent;
    private int noticeId;
    private String noticeTitle;

    // This constructor matches your 'new UpdateNotice(this, id, title)' call
    public UpdateNotice(NoticeManagementPanel parent, int id, String title) {
        this.parent = parent;
        this.noticeId = id;
        this.noticeTitle = title;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // UI Setup
        JLabel lbl = new JLabel("Updating Notice: " + title + " (ID: " + id + ")");
        add(lbl, BorderLayout.NORTH);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> parent.showMainButtons());
        add(btnBack, BorderLayout.SOUTH);

        // Add your text fields and save buttons here...
    }
}