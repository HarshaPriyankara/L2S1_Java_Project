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
    private Timetable recordToDelete = null; // මැකීමට පෙර සොයාගත් record එක තබා ගැනීමට

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

        // ID Search Section
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Enter Timetable ID:"), gbc);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        txtId = new JTextField(10);
        btnSearch = new JButton("Search");
        searchPanel.add(txtId, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        gbc.gridx = 1;
        add(searchPanel, gbc);

        // Details Display (මෙහිදී HTML පාවිච්චි කරලා ලස්සනට විස්තර පෙන්වනවා)
        lblDetails = new JLabel("<html><body style='color: gray;'>Search ID to see details before deleting.</body></html>");
        lblDetails.setBorder(BorderFactory.createTitledBorder("Record Preview"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.ipady = 40; // උස පොඩ්ඩක් වැඩි කළා
        add(lblDetails, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(Color.WHITE);

        btnBack = new JButton("Back to Menu");
        btnBack.addActionListener(e -> parent.showMenu());

        btnDelete = new JButton("Delete Permanently");
        btnDelete.setBackground(new Color(211, 47, 47));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setEnabled(false); // මුලින්ම disable කරලා තියෙන්නේ (Search කරනකම්)

        // --- Logic: Search Button ---
        btnSearch.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID to search!");
                return;
            }

            TimetableDAO dao = new TimetableDAO();
            recordToDelete = dao.searchTimetableById(id); // DAO එකේ search method එක call කරනවා

            if (recordToDelete != null) {
                // Record එක හම්බුණාම විස්තර label එකේ පෙන්වනවා
                lblDetails.setText("<html><div style='padding: 10px;'>"
                        + "<b>ID:</b> " + recordToDelete.getTimeTableId() + "<br>"
                        + "<b>Course:</b> " + recordToDelete.getCourseCode() + "<br>"
                        + "<b>Day:</b> " + recordToDelete.getDay() + "<br>"
                        + "<b>Venue:</b> " + recordToDelete.getVenue() + "</div></html>");
                btnDelete.setEnabled(true); // දැන් Delete කරන්න පුළුවන්
            } else {
                lblDetails.setText("<html><body style='color: red;'>No record found with this ID!</body></html>");
                btnDelete.setEnabled(false);
            }
        });

        // --- Logic: Delete Button ---
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this record?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Static method එක call කරනවා
                if (Timetable.deleteTimeTable(txtId.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Record Deleted Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error! Record could not be deleted.");
                }
            }
        });

        btnPanel.add(btnBack);
        btnPanel.add(btnDelete);

        gbc.gridy = 3;
        gbc.ipady = 0;
        add(btnPanel, gbc);
    }

    private void clearFields() {
        txtId.setText("");
        lblDetails.setText("<html><body style='color: gray;'>Search ID to see details before deleting.</body></html>");
        btnDelete.setEnabled(false);
        recordToDelete = null;
    }
}