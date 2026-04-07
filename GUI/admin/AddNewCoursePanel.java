package GUI.admin;

import Models.Course;

import javax.swing.*;
import java.awt.*;

class AddNewCoursePanel extends JPanel {
    private JTextField txtCode, txtName, txtType, txtCredits, txtLecturer, txtDep;
    public AddNewCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // fields
        add(new JLabel("*Course Code:"));
        txtCode = new JTextField();
        add(txtCode);

        add(new JLabel("*Course Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Type:"));
        txtType = new JTextField();
        add(txtType);

        add(new JLabel("*Credits:"));
        txtCredits = new JTextField();
        add(txtCredits);

        add(new JLabel("Lecturer ID:"));
        txtLecturer = new JTextField();
        add(txtLecturer);

        add(new JLabel("Department ID:"));
        txtDep = new JTextField();
        add(txtDep);

        JButton btnBack = new JButton("Back");
        JButton btnSave = new JButton("Save Course");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);

        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        add(btnBack);
        add(btnSave);
        //save button action
        btnSave.addActionListener(e -> {
            try {
                //get data
                String code = txtCode.getText().trim();
                String name = txtName.getText().trim();
                String credit = txtCredits.getText().trim();
                String type = txtType.getText().trim();
                String lecturer = txtLecturer.getText().trim();
                String department = txtDep.getText().trim();

                String dept = txtDep.getText().trim();

                // validation
                if (code.isEmpty() || name.isEmpty() || credit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // check credit is number
                int credits = Integer.parseInt(credit);
                // create object , get data in fields
                Course newCourse = new Course(code, name, credits, type, lecturer, department);
                // create DAO object
                DAO.CourseDAO courseDao = new DAO.CourseDAO();

                boolean isSuccess = courseDao.addCourse(newCourse);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, "Course Added Successfully!");
                    // clear field if correct
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add course. Code might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
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

