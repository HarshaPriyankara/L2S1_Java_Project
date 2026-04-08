package GUI.admin;

import Models.Course;

import javax.swing.*;
import java.awt.*;

class UpdateCoursePanel extends JPanel {

    private JTextField txtCode, txtName, txtType, txtCredits, txtLecturer, txtDep;

    public UpdateCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // fields
        add(new JLabel("Course Code (to Update):"));
        txtCode = new JTextField();
        add(txtCode);

        add(new JLabel("New Course Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("New Type:"));
        txtType = new JTextField();
        add(txtType);

        add(new JLabel("New Credits:"));
        txtCredits = new JTextField();
        add(txtCredits);

        add(new JLabel("New Lecturer ID:"));
        txtLecturer = new JTextField();
        add(txtLecturer);

        add(new JLabel("New Department ID:"));
        txtDep = new JTextField();
        add(txtDep);

        JButton btnBack = new JButton("Back");
        JButton btnUpdate = new JButton("Update Course");
        btnUpdate.setBackground(new Color(46, 204, 113)); // button color
        btnUpdate.setForeground(Color.WHITE);

        add(btnBack);
        add(btnUpdate);

        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        // 2. Update button action
        btnUpdate.addActionListener(e -> {
            try {
                String code = txtCode.getText().trim();
                String name = txtName.getText().trim();
                String credit = txtCredits.getText().trim();
                String type = txtType.getText().trim();
                String lecturer = txtLecturer.getText().trim();
                String department = txtDep.getText().trim();

                // Validation
                if (code.isEmpty() || name.isEmpty() || credit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Course Code, Name and Credits are required!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int credits = Integer.parseInt(credit);

                // create course object
                Course updatedCourse = new Course(code, name, credits, type, lecturer, department);

                // update via DAO class
                DAO.CourseDAO courseDao = new DAO.CourseDAO();
                boolean isSuccess = courseDao.updateCourse(updatedCourse);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, "Course Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Update Failed! Check if Course Code exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        txtType.setText("");
        txtCredits.setText("");
        txtLecturer.setText("");
        txtDep.setText("");
    }

}
