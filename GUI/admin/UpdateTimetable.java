package GUI.admin;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class UpdateTimetable extends JPanel {

    private JTextField txtId, txtStartTime, txtEndTime, txtVenue;
    private JComboBox<String> cmbLevel, cmbDay, cmbCourse;
    private JButton btnSearch, btnUpdate, btnClear, btnBack;
    private TimetableManagement parent;

    // Search කරලා හම්බෙන data ටික තියාගන්න object එකක්
    private Timetable currentRecord = null;

    public UpdateTimetable(TimetableManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Update Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        // --- Search Section ---
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        addLabel("Enter ID to Search:", gbc, 1);
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        txtId = new JTextField(10);
        btnSearch = new JButton("Search");
        searchPanel.add(txtId, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        gbc.gridx = 1; gbc.gridy = 1;
        add(searchPanel, gbc);

        // --- Form Fields ---
        addFormField("New Level:", cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"}), gbc, 2);
        addFormField("Day:", cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}), gbc, 3);
        addFormField("Start Time (HH:mm):", txtStartTime = new JTextField(), gbc, 4);
        addFormField("End Time (HH:mm):", txtEndTime = new JTextField(), gbc, 5);
        addFormField("Venue:", txtVenue = new JTextField(), gbc, 6);
        addFormField("Course Code:", cmbCourse = new JComboBox<>(new String[]{"ICT2101", "ICT2102", "ICT2201"}), gbc, 7);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu());

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearFields());

        btnUpdate = new JButton("Update Entry");
        btnUpdate.setBackground(new Color(46, 125, 192));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setEnabled(false); // Search කරනකම් update button එක disable කරලා තියෙන්නේ

        // --- SEARCH LOGIC ---
        btnSearch.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to search!");
                return;
            }

            TimetableDAO dao = new TimetableDAO();
            currentRecord = dao.searchTimetableById(id);

            if (currentRecord != null) {
                // Fields වලට data පුරවනවා
                txtStartTime.setText(currentRecord.getStartTime().toString());
                txtEndTime.setText(currentRecord.getEndTime().toString());
                txtVenue.setText(currentRecord.getVenue());
                cmbDay.setSelectedItem(currentRecord.getDay());
                cmbCourse.setSelectedItem(currentRecord.getCourseCode());

                // Level එක හොයාගන්න logic එක (Course Code එකේ 4 වෙනි අකුර)
                String levelChar = currentRecord.getCourseCode().substring(3, 4);
                cmbLevel.setSelectedIndex(Integer.parseInt(levelChar) - 1);

                btnUpdate.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Record found! You can now edit.");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with ID: " + id);
                btnUpdate.setEnabled(false);
            }
        });

        // --- UPDATE LOGIC ---
        btnUpdate.addActionListener(e -> {
            try {
                // පවතින object එකට අලුත් data සෙට් කරනවා
                currentRecord.setStartTime(LocalTime.parse(txtStartTime.getText().trim()));
                currentRecord.setEndTime(LocalTime.parse(txtEndTime.getText().trim()));
                currentRecord.setVenue(txtVenue.getText().trim());
                currentRecord.setDay(cmbDay.getSelectedItem().toString());
                currentRecord.setCourseCode(cmbCourse.getSelectedItem().toString());

                if (currentRecord.updateTimeTable()) {
                    JOptionPane.showMessageDialog(this, "Timetable Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Update Failed. Please check Database.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnPanel.add(btnBack);
        btnPanel.add(btnClear);
        btnPanel.add(btnUpdate);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
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

    private void clearFields() {
        txtId.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        txtVenue.setText("");
        cmbLevel.setSelectedIndex(0);
        cmbDay.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);
        btnUpdate.setEnabled(false);
        currentRecord = null;
    }
}