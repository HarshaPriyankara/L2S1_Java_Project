package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class TimetableManagement extends JPanel {

    private CardLayout internalCardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(internalCardLayout);

    public TimetableManagement() {
        setLayout(new BorderLayout());

        //Menu
        mainContainer.add(buildMenuPanel(), "Menu");

        //Form Panel
        mainContainer.add(new AddTimetable(this), "Add");
        mainContainer.add(new UpdateTimetable(this), "Update");
        mainContainer.add(new DeleteTimetable(this), "Delete");

        add(mainContainer, BorderLayout.CENTER);
    }

    // Main menu 3 buttons
    private JPanel buildMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(15, 0, 15, 0); gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAdd = createMenuButton("Add New Timetable Entry");
        JButton btnUpdate = createMenuButton("Update Timetable Details");
        JButton btnDelete = createMenuButton("Delete Timetable Entry");

        btnAdd.addActionListener(e -> internalCardLayout.show(mainContainer, "Add"));
        btnUpdate.addActionListener(e -> internalCardLayout.show(mainContainer, "Update"));
        btnDelete.addActionListener(e -> internalCardLayout.show(mainContainer, "Delete"));

        menuPanel.add(btnAdd, gbc);
        menuPanel.add(btnUpdate, gbc);
        menuPanel.add(btnDelete, gbc);

        return menuPanel;
    }

    // again menu
    public void showMenu() {
        internalCardLayout.show(mainContainer, "Menu");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 70));
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setBackground(new Color(84, 193, 246));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}