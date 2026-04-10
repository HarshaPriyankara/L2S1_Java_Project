package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class UpdateTimetable extends JPanel {

    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnSearch, btnUpdate, btnClear, btnBack;
    private TimetableManagement parent;

    public UpdateTimetable(TimetableManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Update Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        // Search Section
        gbc.gridwidth = 1;
        addLabel("Enter ID to Search:", gbc, 1);
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        txtId = new JTextField(10);
        btnSearch = new JButton("Search");
        searchPanel.add(txtId, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 1;
        add(searchPanel, gbc);

        // Fields
        addFormField("New Level:", cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"}), gbc, 2);
        addFormField("New Start Time:", txtStartTime = new JTextField(), gbc, 3);
        addFormField("New End Time:", txtEndTime = new JTextField(), gbc, 4);
        addFormField("New Venue:", txtVenue = new JTextField(), gbc, 5);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu());

        btnClear = new JButton("Clear");
        btnUpdate = new JButton("Update Entry");
        btnUpdate.setBackground(new Color(46, 125, 192));
        btnUpdate.setForeground(Color.WHITE);

        btnPanel.add(btnBack);
        btnPanel.add(btnClear);
        btnPanel.add(btnUpdate);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(btnPanel, gbc);
    }

    private void addLabel(String text, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private void addFormField(String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }
}