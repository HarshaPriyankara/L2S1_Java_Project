package GUI.lecturer;

import javax.swing.*;
import java.awt.*;
import DAO.LecturerDAO;
import DAO.MarkDAO;

public class MarksUpdatePanel extends JPanel {
    private MarksManagement parent;
    private JTextField txtMarkId, txtRegNo, txtMarks;
    private JComboBox<String> cmbCourse, cmbType;
    private JButton btnUpdate, btnBack, btnClear;

    public MarksUpdatePanel(MarksManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JLabel lblTitle = new JLabel("Update Student Marks");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(46, 125, 192));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitle, gbc);

        //Input Fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        addFormField("Enter Mark ID to Update:", txtMarkId = new JTextField(20), gbc, 1);
        addFormField("Student Reg No:", txtRegNo = new JTextField(20), gbc, 2);

        // Courses
        String[] courses = {"ICT2101", "ICT2102", "ICT2103", "ICT2104"};
        addFormField("Course Code:", cmbCourse = new JComboBox<>(courses), gbc, 3);

        // Marks Types
        String[] displayTypes = {
                "Quiz 1", "Quiz 2", "Quiz 3",
                "Assignment 1", "Assignment 2",
                "Mini Project",
                "Mid Practical", "Mid Theory",
                "End Practical", "End Theory"
        };
        addFormField("Marks Type:", cmbType = new JComboBox<>(displayTypes), gbc, 4);

        addFormField("New Marks Value:", txtMarks = new JTextField(20), gbc, 5);

        //Buttons
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bp.setBackground(Color.WHITE);

        btnBack = new JButton("Back");
        btnClear = new JButton("Clear");
        btnUpdate = new JButton("Update Marks");
        btnUpdate.setBackground(new Color(46, 125, 192));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));

        bp.add(btnBack); bp.add(btnClear); bp.add(btnUpdate);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(bp, gbc);

        // --- Listeners ---
        btnBack.addActionListener(e -> parent.showMenu());
        btnClear.addActionListener(e -> clearFields());

        btnUpdate.addActionListener(e -> updateMarksLogic());
    }

    private void updateMarksLogic() {
        try {
            // Fields check
            if (txtMarkId.getText().isEmpty() || txtRegNo.getText().isEmpty() || txtMarks.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(txtMarkId.getText().trim());
            String regNo = txtRegNo.getText().trim();
            String course = cmbCourse.getSelectedItem().toString();

            //convert
            String selectedType = cmbType.getSelectedItem().toString();
            String typeForDB = selectedType.replace(" ", "_");

            double marks = Double.parseDouble(txtMarks.getText().trim());

            //check mark limit
            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100!");
                return;
            }


            MarkDAO dao = new MarkDAO();
            boolean isSuccess = dao.updateMarks(id, regNo, course, typeForDB, marks);

            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Marks Updated Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed! Check if Mark ID is correct.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and Marks!", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addFormField(String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }

    private void clearFields() {
        txtMarkId.setText("");
        txtRegNo.setText("");
        txtMarks.setText("");
        cmbCourse.setSelectedIndex(0);
        cmbType.setSelectedIndex(0);
    }
}