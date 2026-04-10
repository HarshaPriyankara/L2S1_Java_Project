package GUI.lecturer;

import javax.swing.*;
import java.awt.*;
import DAO.LecturerDAO;
import DAO.MarkDAO;

public class MarksDeletePanel extends JPanel {
    private MarksManagement parent;
    private JTextField txtMarkId;

    public MarksDeletePanel(MarksManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Title
        JLabel lblTitle = new JLabel("Delete Student Marks");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(211, 47, 47));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        //Input Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0; add(new JLabel("Enter Mark ID:"), gbc);

        txtMarkId = new JTextField(15);
        gbc.gridx = 1; add(txtMarkId, gbc);

        //Buttons
        JButton btnBack = new JButton("Back");
        JButton btnDelete = new JButton("Delete Marks");
        btnDelete.setBackground(new Color(211, 47, 47));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Back Action
        btnBack.addActionListener(e -> parent.showMenu());

        // Delete Action
        btnDelete.addActionListener(e -> {
            deleteMarksAction();
        });

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bp.setBackground(Color.WHITE);
        bp.add(btnBack);
        bp.add(btnDelete);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(bp, gbc);
    }

    private void deleteMarksAction() {
        String idStr = txtMarkId.getText().trim();

        // Validation
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Mark ID to delete!", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmation Dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete this mark record?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);

                // LecturerDAO call කිරීම
                MarkDAO dao = new MarkDAO();
                boolean isSuccess = dao.deleteMarks(id);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, "Mark Record Deleted Successfully!");
                    txtMarkId.setText(""); // Field එක හිස් කිරීම
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed! Mark ID not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}