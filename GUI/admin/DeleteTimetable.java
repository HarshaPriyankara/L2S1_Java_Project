package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class DeleteTimetable extends JPanel {

    private JTextField txtId;
    private JLabel lblDetails;
    private JButton btnSearch, btnDelete, btnClear, btnBack;
    private TimetableManagement parent;

    public DeleteTimetable(TimetableManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Delete Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // ID Search
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Enter Timetable ID:"), gbc);
        txtId = new JTextField(15);
        gbc.gridx = 1;
        add(txtId, gbc);

        // Details Display
        lblDetails = new JLabel("<html><body style='color: gray;'>Search ID to see details before deleting.</body></html>");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(lblDetails, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu());

        btnDelete = new JButton("Delete Permanently");
        btnDelete.setBackground(new Color(211, 47, 47));
        btnDelete.setForeground(Color.WHITE);

        btnPanel.add(btnBack);
        btnPanel.add(btnDelete);

        gbc.gridy = 3;
        add(btnPanel, gbc);
    }
}