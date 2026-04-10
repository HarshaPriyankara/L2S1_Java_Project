package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AddTimetable extends JPanel {

    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnAdd, btnClear, btnBack;
    private TimetableManagement parent; // Menu එකට යාම පාලනය කිරීමට

    public AddTimetable(TimetableManagement parent) {
        this.parent = parent;

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Add New Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        // --- Form Fields ---
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        addFormField("Timetable ID:", txtId = new JTextField(20), gbc, 1);
        addFormField("Level:", cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"}), gbc, 2);
        addFormField("Day:", cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}), gbc, 3);
        addFormField("Start Time:", txtStartTime = new JTextField(), gbc, 4);
        addFormField("End Time:", txtEndTime = new JTextField(), gbc, 5);
        addFormField("Venue:", txtVenue = new JTextField(), gbc, 6);
        addFormField("Course Code:", cmbCourse = new JComboBox<>(new String[]{"-- Select --", "ICT2101", "ICT2102"}), gbc, 7);

        // --- Buttons පැනල් එක ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        // Back බටන් එක (Menu එකට යාමට)
        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu()); // Menu එක පෙන්වීමට මාරු වේ

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearFields());

        btnAdd = new JButton("Add Entry");
        btnAdd.setBackground(new Color(46, 125, 192));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data Saved Successfully!");
        });

        btnPanel.add(btnBack);
        btnPanel.add(btnClear);
        btnPanel.add(btnAdd);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(btnPanel, gbc);
    }

    private void addFormField(String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    private void clearFields() {
        txtId.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        txtVenue.setText("");
        cmbLevel.setSelectedIndex(0);
        cmbDay.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);
    }
}