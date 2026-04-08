package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AddTimetable extends JPanel {

    // Components
    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbType, cmbDept, cmbCourse;
    private JButton btnAdd, btnClear;

    public AddTimetable() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout()); // මැදට එන්න GridBagLayout පාවිච්චි කරමු
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Add New Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // --- Fields ---
        gbc.gridwidth = 1;

        // ID
        addLabel("Timetable ID:", gbc, 1);
        txtId = new JTextField(15);
        addTextField(txtId, gbc, 1);

        // Level
        addLabel("Level:", gbc, 2);
        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        addComboBox(cmbLevel, gbc, 2);

        // Day
        addLabel("Day:", gbc, 3);
        cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        addComboBox(cmbDay, gbc, 3);

        // Start Time
        addLabel("Start Time (e.g. 08:30):", gbc, 4);
        txtStartTime = new JTextField();
        addTextField(txtStartTime, gbc, 4);

        // End Time
        addLabel("End Time (e.g. 10:30):", gbc, 5);
        txtEndTime = new JTextField();
        addTextField(txtEndTime, gbc, 5);

        // Venue
        addLabel("Venue:", gbc, 6);
        txtVenue = new JTextField();
        addTextField(txtVenue, gbc, 6);

        // Course (Placeholder data)
        addLabel("Course Code:", gbc, 7);
        cmbCourse = new JComboBox<>(new String[]{"-- Select Course --", "ICT2101", "ICT2102", "ICT2103"});
        addComboBox(cmbCourse, gbc, 7);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Entry");
        btnAdd.setBackground(new Color(46, 125, 192)); // Dashboard එකේ button color එක
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnClear = new JButton("Clear");

        btnPanel.add(btnClear);
        btnPanel.add(btnAdd);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // Simple Test Action
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data is ready! (Database connection pending)");
        });

        btnClear.addActionListener(e -> clearFields());
    }

    // Helper methods to keep code clean
    private void addLabel(String text, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private void addTextField(JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 1; gbc.gridy = y;
        add(field, gbc);
    }

    private void addComboBox(JComboBox box, GridBagConstraints gbc, int y) {
        gbc.gridx = 1; gbc.gridy = y;
        add(box, gbc);
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