/*
package GUI.admin;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import java.awt.*;

public class DeleteTimetable extends JPanel {

    private JTextField txtId;
    private JLabel lblDetails;
    private JButton btnSearch, btnDelete, btnClear, btnBack;
    private TimetableManagement parent;
    private Timetable recordToDelete = null;

    public DeleteTimetable(TimetableManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Delete Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(lblTitle, gbc);

        // --- Search Section ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Enter Timetable ID:"), gbc);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        txtId = new JTextField(12);
        btnSearch = new JButton("Search");
        searchPanel.add(txtId, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        gbc.gridx = 1;
        add(searchPanel, gbc);

        // --- Preview Details Label ---
        lblDetails = new JLabel("<html><body style='color: gray; text-align: center;'>"
                + "Enter ID and Search to preview record details.</body></html>");
        lblDetails.setPreferredSize(new Dimension(420, 180));
        lblDetails.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Record Preview"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(lblDetails, gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back");
        btnBack.addActionListener(e -> parent.showMenu());

        btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearFields());

        btnDelete = new JButton("Delete Permanently");
        btnDelete.setBackground(new Color(211, 47, 47));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setEnabled(false);

        // --- Search Logic ---
        btnSearch.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to search!");
                return;
            }

            TimetableDAO dao = new TimetableDAO();
            // මෙතනදී DAO එකේ අපි අලුතින් හදපු searchTimetableById එක කෝල් වෙනවා
            recordToDelete = dao.searchTimetableById(id);

            if (recordToDelete != null) {
                // Course Code එකෙන් Level සහ Semester Extract කරගැනීම (Smart Logic)
                String code = recordToDelete.getCourseCode();
                String level = "N/A", semester = "N/A";
                if(code != null && code.length() >= 5) {
                    level = "Level " + code.charAt(3);
                    semester = "Semester " + code.charAt(4);
                }

                String details = "<html><div style='font-family: sans-serif; font-size: 11pt; padding: 5px;'>"
                        + "<b style='color: #2E7DC0;'>ID:</b> " + recordToDelete.getTimeTableId() + "<br>"
                        + "<b>Course Code:</b> " + recordToDelete.getCourseCode() + "<br>"
                        + "<b>Batch:</b> <span style='color: green;'>" + level + " | " + semester + "</span><br>"
                        + "<b>Schedule:</b> " + recordToDelete.getDay() + " (" + recordToDelete.getStartTime() + " - " + recordToDelete.getEndTime() + ")<br>"
                        + "<b>Venue:</b> " + recordToDelete.getVenue() + "</div></html>";

                lblDetails.setText(details);
                btnDelete.setEnabled(true);
            } else {
                lblDetails.setText("<html><body style='color: #D32F2F; text-align: center;'><b>No record found with ID: " + id + "</b></body></html>");
                btnDelete.setEnabled(false);
            }
        });

        // --- Delete Logic ---
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "This action will permanently remove the record. Are you sure?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Timetable Model එකේ deleteTimeTable මෙතඩ් එක කෝල් කිරීම
                if (Timetable.deleteTimeTable(txtId.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting record! Please try again.");
                }
            }
        });

        btnPanel.add(btnBack);
        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);

        gbc.gridy = 3;
        add(btnPanel, gbc);
    }

    private void clearFields() {
        txtId.setText("");
        lblDetails.setText("<html><body style='color: gray; text-align: center;'>Enter ID and Search to preview record details.</body></html>");
        btnDelete.setEnabled(false);
        recordToDelete = null;
    }
}

 */