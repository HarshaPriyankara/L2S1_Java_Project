package GUI.admin;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;

// INHERITANCE: Inheriting from the standard JPanel to act as a container
public class NoticeManagementPanel extends JPanel {

    // ENCAPSULATION: Encapsulated constants and internal state
    public NoticeManagementPanel() {
        setBackground(UITheme.APP_BACKGROUND);
        showMainButtons();
    }

    public void showMainButtons() {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        this.setBackground(UITheme.APP_BACKGROUND);

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

        JButton btnView = createNoticeButton("View & Modify Notice");
        btnView.addActionListener(e -> loadViewNoticePanel());
        add(btnView, gbc);

        this.revalidate();
        this.repaint();
    }

    // ABSTRACTION: Loading complex inner panels independently of the main buttons 
    private void loadCreateNoticePanel() {
        this.removeAll(); 
        this.setLayout(new BorderLayout());
        this.add(new CreateNotice(this)); 
        this.revalidate();
        this.repaint();
    }

    private void loadViewNoticePanel() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(new ViewNotice(this)); 
        this.revalidate();
        this.repaint();
    }

    private JButton createNoticeButton(String text) {
        JButton btn = new JButton(text);
        UITheme.styleLargeMenuButton(btn);

        return btn;
    }

    public void showUpdateNotice(int id, String title) {
        this.removeAll();
        this.setLayout(new BorderLayout());
        
        // Using Polymorphic GUI transition by reusing UI via UpdateNotice which extends CreateNotice
        this.add(new UpdateNotice(this, id, title));

        this.revalidate();
        this.repaint();
    }
}
