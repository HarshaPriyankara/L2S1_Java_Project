package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class DeleteTimetable extends JPanel {

    private JTextField txtId;
    private JLabel lblDetails; // මකන්න කලින් දත්ත පෙන්වන්න
    private JButton btnSearch, btnDelete, btnClear;

    public DeleteTimetable() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Delete Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // --- Search Section ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Enter Timetable ID:"), gbc);

        txtId = new JTextField(15);
        gbc.gridx = 1;
        add(txtId, gbc);

        btnSearch = new JButton("Find Details");
        gbc.gridx = 1; gbc.gridy = 2;
        add(btnSearch, gbc);

        // --- පෙන්වන විස්තර (මකන්න කලින් පරීක්ෂා කිරීමට) ---
        lblDetails = new JLabel("<html><body style='text-align: center; color: gray;'>Enter an ID and click search to see details before deleting.</body></html>");
        lblDetails.setPreferredSize(new Dimension(300, 80));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(lblDetails, gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Color.WHITE);

        btnDelete = new JButton("Delete Permanently");
        btnDelete.setBackground(new Color(211, 47, 47)); // රතු පාට
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setEnabled(false); // මුලින්ම disable කරලා තියන්න

        btnClear = new JButton("Clear");

        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);

        gbc.gridy = 4;
        add(btnPanel, gbc);

        // --- Action Listeners ---

        // සෙවීමේදී
        btnSearch.addActionListener(e -> {
            String id = txtId.getText();
            if(!id.isEmpty()) {
                // මෙතනදී Database එකෙන් data අරන් lblDetails එකට දාන්න ඕනේ
                lblDetails.setText("<html><font color='black'>ID: " + id + "<br>Course: ICT2101<br>Day: Monday<br>Venue: Lab 01</font></html>");
                btnDelete.setEnabled(true); // දත්ත හමු වුණොත් විතරක් delete button එක enable කරන්න
            } else {
                JOptionPane.showMessageDialog(this, "Please enter an ID!");
            }
        });

        // මැකීමේදී
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this schedule?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Database Delete Logic
                JOptionPane.showMessageDialog(this, "Deleted successfully (Preview)!");
                clearFields();
            }
        });

        btnClear.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        txtId.setText("");
        lblDetails.setText("<html><body style='text-align: center; color: gray;'>Enter an ID and click search to see details before deleting.</body></html>");
        btnDelete.setEnabled(false);
    }
}