package GUI.lecturer;

import javax.swing.*;
import java.awt.*;

public class MarksManagement extends JPanel {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    public MarksManagement() {
        setLayout(new BorderLayout());

        // panels are register
        mainContainer.add(createMenuPanel(), "Menu");
        mainContainer.add(new MarksUploadPanel(this), "AddMarks");
        mainContainer.add(new MarksUpdatePanel(this), "UpdateMarks");
        mainContainer.add(new MarksDeletePanel(this), "DeleteMarks");

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(15, 0, 15, 0); gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAdd = createMenuButton("Upload New Marks");
        JButton btnUpdate = createMenuButton("Update Existing Marks");
        JButton btnDelete = createMenuButton("Delete Student Marks");

        btnAdd.addActionListener(e -> cardLayout.show(mainContainer, "AddMarks"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainContainer, "UpdateMarks"));
        btnDelete.addActionListener(e -> cardLayout.show(mainContainer, "DeleteMarks"));

        menuPanel.add(btnAdd, gbc);
        menuPanel.add(btnUpdate, gbc);
        menuPanel.add(btnDelete, gbc);

        return menuPanel;
    }

    public void showMenu() {
        cardLayout.show(mainContainer, "Menu");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 70));
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setBackground(new Color(46, 125, 192));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}