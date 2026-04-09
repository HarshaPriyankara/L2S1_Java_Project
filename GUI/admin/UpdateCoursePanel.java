package GUI.admin;

import Models.Course;
import DAO.CourseDAO;
import javax.swing.*;
import java.awt.*;

class UpdateCoursePanel extends JPanel {

    private JTextField txtCode, txtName, txtType, txtCredits, txtLecturer, txtDep;
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public UpdateCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        // Layout eka BoxLayout (Y_AXIS) ekata maru kara
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title eka add kara
        addTitle("Update Course Details", BUTTON_COLOR);

        // 2. UserManagement eke wage fields tika piliwelata add kara
        txtCode     = addField("Course Code (to Update)");
        txtName     = addField("New Course Name");
        txtType     = addField("New Type");
        txtCredits  = addField("New Credits");
        txtLecturer = addField("New Lecturer ID");
        txtDep      = addField("New Department ID");

        // 3. Button Row setup kara
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack, new Color(0xAAAAAA));
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "CourseMenu"));

        // Update Button
        JButton btnUpdate = new JButton("Update Course");
        styleButton(btnUpdate, BUTTON_COLOR);

        row.add(btnBack);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnUpdate);
        add(row);

        // Update button action (Oyaage original logic eka - method wenas kale ne)
        btnUpdate.addActionListener(e -> {
            try {
                String code = txtCode.getText().trim();
                String name = txtName.getText().trim();
                String credit = txtCredits.getText().trim();

                if (code.isEmpty() || name.isEmpty() || credit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Required fields are empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int credits = Integer.parseInt(credit);
                Course updatedCourse = new Course(code, name, credits, txtType.getText().trim(), txtLecturer.getText().trim(), txtDep.getText().trim());

                CourseDAO courseDao = new CourseDAO();
                if (courseDao.updateCourse(updatedCourse)) {
                    JOptionPane.showMessageDialog(this, "Course Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Update Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // ── UserManagement eke thibba Styling Methods (Copy-Paste) ──

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

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        txtType.setText("");
        txtCredits.setText("");
        txtLecturer.setText("");
        txtDep.setText("");
    }
}