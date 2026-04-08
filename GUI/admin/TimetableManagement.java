package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class TimetableManagement extends JPanel {

    // Components (Fields) - මේවා class එකේ උඩින්ම තියෙන්න ඕනේ
    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public TimetableManagement() {
        // ප්‍රධාන පැනල් එකේ සැකසුම
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 0));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- 1. මැද පැනල් එක (Center) - Input Form එක සඳහා ---
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title එක
        JLabel lblTitle = new JLabel("Timetable Management System");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0x2E2E2E));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 40, 10);
        formPanel.add(lblTitle, gbc);

        // Fields එකතු කිරීම
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);

        addFormField("Timetable ID:", txtId = new JTextField(20), formPanel, gbc, 1);

        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        addFormField("Level:", cmbLevel = new JComboBox<>(levels), formPanel, gbc, 2);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        addFormField("Day:", cmbDay = new JComboBox<>(days), formPanel, gbc, 3);

        addFormField("Start Time (HH:mm):", txtStartTime = new JTextField(), formPanel, gbc, 4);
        addFormField("End Time (HH:mm):", txtEndTime = new JTextField(), formPanel, gbc, 5);
        addFormField("Venue:", txtVenue = new JTextField(), formPanel, gbc, 6);

        String[] courses = {"-- Select Course --", "ICT2101", "ICT2102", "ICT2103"};
        addFormField("Course Code:", cmbCourse = new JComboBox<>(courses), formPanel, gbc, 7);

        formWrapper.add(formPanel);
        add(formWrapper, BorderLayout.CENTER);

        // --- 2. දකුණු පස පැනල් එක (East) - Buttons සඳහා ---
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.setPreferredSize(new Dimension(220, 0)); // බටන් පැනල් එකේ පළල

        // බටන් එක යට එක එන්න Vertical Box එකක්
        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(80)); // උඩින් හිස් ඉඩක්

        // බටන් නිර්මාණය (Dashboard theme එකට ගැලපෙන පාට)
        btnAdd = createStyledButton("ADD ENTRY", new Color(46, 125, 192));
        btnUpdate = createStyledButton("UPDATE ENTRY", new Color(46, 125, 192));
        btnDelete = createStyledButton("DELETE ENTRY", new Color(211, 47, 47));
        btnClear = createStyledButton("CLEAR FIELDS", new Color(108, 117, 125));

        // Box එකට බටන් එකතු කිරීම
        vBox.add(btnAdd);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnUpdate);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnDelete);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnClear);

        buttonContainer.add(vBox);

        // අනිවාර්යයෙන්ම මෙය තිබිය යුතුයි (දකුණු පැත්තට බටන් පැනල් එක add කිරීම)
        add(buttonContainer, BorderLayout.EAST);

        // --- 3. සරල Action Listeners ---
        btnClear.addActionListener(e -> clearAllFields());

        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Logic Goes Here"));
    }

    // Helper Method to add Labels and Fields
    private void addFormField(String label, JComponent comp, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    // Button Styling Method
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    private void clearAllFields() {
        txtId.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        txtVenue.setText("");
        cmbLevel.setSelectedIndex(0);
        cmbDay.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);
    }
}