package GUI.admin;

import Models.Course;
import DAO.CourseDAO;
import javax.swing.*;
import java.awt.*;

class AddNewCoursePanel extends JPanel {

    private JTextField txtCode, txtName, txtType, txtCredits, txtLecturer, txtDep;
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public AddNewCoursePanel(JPanel parentPanel, CardLayout cardLayout) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Create New Course", BUTTON_COLOR);

        txtCode     = addField("*Course Code");
        txtName     = addField("*Course Name");
        txtType     = addField("Type");
        txtCredits  = addField("*Credits");
        txtLecturer = addField("Lecturer ID");
        txtDep      = addField("Department ID");

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack, new Color(0xAAAAAA));
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "CourseMenu"));

        // Save Button
        JButton btnSave = new JButton("Save Course");
        styleButton(btnSave, BUTTON_COLOR);

        row.add(btnBack);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnSave);
        add(row);

        // Save Button Action
        btnSave.addActionListener(e -> {
            saveCourse();
        });
    }

    private void addTitle(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(24));
    }

    private JTextField addField(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(lbl);
        add(Box.createVerticalStrut(5));
        add(field);
        add(Box.createVerticalStrut(12));
        return field;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));
    }

    private void saveCourse() {
        try {
            String code = txtCode.getText().trim();
            String name = txtName.getText().trim();
            String creditStr = txtCredits.getText().trim();
            String type = txtType.getText().trim();
            String lecturer = txtLecturer.getText().trim();
            String department = txtDep.getText().trim();

            if (code.isEmpty() || name.isEmpty() || creditStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int credits = Integer.parseInt(creditStr);
            Course newCourse = new Course(code, name, credits, type, lecturer, department);
            CourseDAO courseDao = new CourseDAO();

            if (courseDao.addCourse(newCourse)) {
                JOptionPane.showMessageDialog(this, "Course Added Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course. Code might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
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