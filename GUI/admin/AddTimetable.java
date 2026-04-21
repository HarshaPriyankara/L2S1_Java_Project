package GUI.admin;

import Models.Timetable;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddTimetable extends JPanel {

    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnAdd, btnClear, btnBack;
    private TimetableManagement parent;

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
        addFormField("Start Time (HH:mm):", txtStartTime = new JTextField(), gbc, 4);
        addFormField("End Time (HH:mm):", txtEndTime = new JTextField(), gbc, 5);
        addFormField("Venue:", txtVenue = new JTextField(), gbc, 6);
        addFormField("Course Code:", cmbCourse = new JComboBox<>(new String[]{"-- Select --", "ICT2101", "ICT2102", "ICT2201", "ENG1222", "ICT2113", "ICT2132", "ICT2122", "ICT2142" ,"ICT2152" ,"TCS2112", "TCS2122"}), gbc, 7);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu());

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearFields());

        btnAdd = new JButton("Add Entry");
        btnAdd.setBackground(new Color(46, 125, 192));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnAdd.addActionListener(e -> {
            try {
                // 1. Model Object එකක් හදාගමු
                Timetable tt = new Timetable();

                // 2. GUI එකේ Fields වලින් දත්ත ගැනීම
                tt.setTimeTableId(txtId.getText().trim());
                tt.setCourseCode(cmbCourse.getSelectedItem().toString());
                tt.setDay(cmbDay.getSelectedItem().toString());
                tt.setVenue(txtVenue.getText().trim());
                tt.setSessionType("Theory");
                tt.setDepartmentId("D0001");

                // --- සුපිරි Time Formatter එක (මෙතනයි වැදගත්ම කොටස) ---
                // H:mm පාවිච්චි කළාම 8:30 සහ 08:30 දෙකම වැඩ කරනවා
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

                try {
                    LocalTime startTime = LocalTime.parse(txtStartTime.getText().trim(), timeFormatter);
                    LocalTime endTime = LocalTime.parse(txtEndTime.getText().trim(), timeFormatter);

                    tt.setStartTime(startTime);
                    tt.setEndTime(endTime);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Time! Please use 08:30 format.");
                    return; // වෙලාව වැරදි නම් ඉතිරි ටික කරන්නේ නැහැ
                }

                // 3. Database එකට Save කිරීම
                if (tt.createTimeTable()) {
                    JOptionPane.showMessageDialog(this, "Timetable Entry Added Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add. ID already exists or Connection Error.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
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