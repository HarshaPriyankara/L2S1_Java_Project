package GUI.admin;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;

// INHERITANCE: Inheriting from the standard JPanel to act as a container
public class NoticeManagementPanel extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    // ENCAPSULATION: Encapsulated constants and internal state
    public NoticeManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        showMainButtons();
    }

    public void showMainButtons() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        JPanel menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        menu.add(createNoticeCard("Create Notice", this::loadCreateNoticePanel));
        menu.add(Box.createVerticalStrut(20));
        menu.add(createNoticeCard("View & Modify Notice", this::loadViewNoticePanel));

        add(menu, BorderLayout.CENTER);

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

    private JPanel createNoticeCard(String text, Runnable action) {
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

    public void showUpdateNotice(int id, String title) {
        this.removeAll();
        this.setLayout(new BorderLayout());
        
        // Using Polymorphic GUI transition by reusing UI via UpdateNotice which extends CreateNotice
        this.add(new UpdateNotice(this, id, title));

        this.revalidate();
        this.repaint();
    }
}
