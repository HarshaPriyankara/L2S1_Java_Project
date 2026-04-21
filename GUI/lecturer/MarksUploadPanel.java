/*package GUI.lecturer;

import javax.swing.*;
import java.awt.*;
import DAO.LecturerDAO;
import DAO.MarkDAO;

public class MarksUploadPanel extends JPanel {
    private MarksManagement parent;
    private JTextField txtRegNo, txtMarks;
    private JComboBox<String> cmbCourse, cmbType;

    public MarksUploadPanel(MarksManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Title
        JLabel lblTitle = new JLabel("Upload New Marks");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(46, 125, 192));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        //Form Fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        addFormField("Registration No:", txtRegNo = new JTextField(20), gbc, 1);

        // Course Codes
        String[] courses = {"ICT2101", "ICT2102", "ICT2103", "ICT2104"};
        addFormField("Course Code:", cmbCourse = new JComboBox<>(courses), gbc, 2);

        // Marks Types
        String[] displayTypes = {
                "Quiz 1", "Quiz 2", "Quiz 3",
                "Assignment 1", "Assignment 2",
                "Mini Project",
                "Mid Practical", "Mid Theory",
                "End Practical", "End Theory"
        };
        cmbType = new JComboBox<>(displayTypes);
        addFormField("Marks Type:", cmbType, gbc, 3);

        addFormField("Marks Value:", txtMarks = new JTextField(20), gbc, 4);

        //Buttons
        JButton btnBack = new JButton("Back");
        JButton btnSave = new JButton("Save Marks");
        btnSave.setBackground(new Color(46, 125, 192));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Back Button Action
        btnBack.addActionListener(e -> parent.showMenu());

        // Save Button Action
        btnSave.addActionListener(e -> saveMarks());

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bp.setBackground(Color.WHITE);
        bp.add(btnBack);
        bp.add(btnSave);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(bp, gbc);
    }

    private void saveMarks() {
        String regNo = txtRegNo.getText().trim();
        String course = cmbCourse.getSelectedItem().toString();

        // convert
        String selectedType = cmbType.getSelectedItem().toString();
        String typeForDB = selectedType.replace(" ", "_");

        String marksStr = txtMarks.getText().trim();

        //isempty
        if (regNo.isEmpty() || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double marks = Double.parseDouble(marksStr);

            //mark check
            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //send database
            MarkDAO dao = new MarkDAO();
            boolean isSuccess = dao.addMarks(regNo, course, typeForDB, marks);

            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Marks Uploaded Successfully!");
                clearFields();
            } else {
                //check regno
                JOptionPane.showMessageDialog(this, "Failed to upload marks.\nCheck if Registration Number exists!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for marks!", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addFormField(String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    private void clearFields() {
        txtRegNo.setText("");
        txtMarks.setText("");
        cmbCourse.setSelectedIndex(0);
        cmbType.setSelectedIndex(0);
    }
}*/