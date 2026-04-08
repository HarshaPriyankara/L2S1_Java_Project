package GUI.to;

import javax.swing.*;
import java.awt.*;

public class AttendanceManagement extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer;

    public AttendanceManagement(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        mainContainer = new JPanel(cardLayout);

        // 1. panels are add CardLayout
        mainContainer.add(createMenuPanel(), "AttendanceMenu");
        mainContainer.add(new AddAttendancePanel(cardLayout, mainContainer), "AddForm");
        mainContainer.add(new UpdateAttendancePanel(cardLayout, mainContainer), "UpdateForm");
        mainContainer.add(new DeleteAttendancePanel(cardLayout, mainContainer), "DeleteForm");

        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "AttendanceMenu");
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);

        //button
        JButton btnAdd = createBigButton("Add New Attendance");
        JButton btnUpdate = createBigButton("Update Attendance Record");
        JButton btnDelete = createBigButton("Delete Attendance Record"); // මේ බටන් එක අලුතින් එක් කළා

        //get page
        btnAdd.addActionListener(e -> cardLayout.show(mainContainer, "AddForm"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainContainer, "UpdateForm"));
        btnDelete.addActionListener(e -> cardLayout.show(mainContainer, "DeleteForm"));

        panel.add(btnAdd, gbc);
        panel.add(btnUpdate, gbc);
        panel.add(btnDelete, gbc); //set button for panel
        return panel;
    }

    private JButton createBigButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(600, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}