package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class UpdateTimetable extends JPanel {

    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnSearch, btnUpdate, btnClear;

    public UpdateTimetable() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Update Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // --- Search Section (ID එකෙන් දත්ත සොයන්න) ---
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

        // --- Editable Fields ---
        addLabel("New Level:", gbc, 2);
        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        addComp(cmbLevel, gbc, 2);

        addLabel("New Day:", gbc, 3);
        cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        addComp(cmbDay, gbc, 3);

        addLabel("New Start Time:", gbc, 4);
        txtStartTime = new JTextField();
        addComp(txtStartTime, gbc, 4);

        addLabel("New End Time:", gbc, 5);
        txtEndTime = new JTextField();
        addComp(txtEndTime, gbc, 5);

        addLabel("New Venue:", gbc, 6);
        txtVenue = new JTextField();
        addComp(txtVenue, gbc, 6);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        btnUpdate = new JButton("Update Entry");
        btnUpdate.setBackground(new Color(46, 125, 192));
        btnUpdate.setForeground(Color.WHITE);

        btnClear = new JButton("Clear");

        btnPanel.add(btnClear);
        btnPanel.add(btnUpdate);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // --- Simple Action for Testing ---
        btnSearch.addActionListener(e -> {
            // මෙතනදී Database එකෙන් දත්ත අරන් Fields පුරවන්න ඕනේ
            JOptionPane.showMessageDialog(this, "Searching for ID: " + txtId.getText());
        });

        btnUpdate.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data updated successfully (Preview)!");
        });
    }

    private void addLabel(String text, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(text), gbc);
    }

    private void addComp(JComponent c, GridBagConstraints gbc, int y) {
        gbc.gridx = 1; gbc.gridy = y;
        add(c, gbc);
    }
}