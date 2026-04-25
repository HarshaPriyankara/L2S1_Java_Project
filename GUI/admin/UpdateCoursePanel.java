package GUI.admin;

import Controllers.CourseControllers.CourseController;
import Controllers.CourseControllers.CourseFormData;
import Controllers.CourseControllers.CourseOperationResult;
import GUI.common.UITheme;
import javax.swing.*;
import java.awt.*;

class UpdateCoursePanel extends JPanel {

    private JTextField txtName, txtCredits;
    private JComboBox<String> cmbCode, cmbType, cmbLecturer, cmbDep;
    private static final Color BUTTON_COLOR = UITheme.PRIMARY;
    private final CourseController courseController = new CourseController();

    public UpdateCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        // Layout eka BoxLayout (Y_AXIS) ekata maru kara
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.SURFACE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title eka add kara
        addTitle("Update Course Details", BUTTON_COLOR);

        // 2. UserManagement eke wage fields tika piliwelata add kara
        cmbCode     = addDropdown("Course Code (to Update)");
        txtName     = addField("New Course Name");
        cmbType     = addTypeDropdown("New Type");
        txtCredits  = addField("New Credits");
        cmbLecturer = addDropdown("New Lecturer ID");
        cmbDep      = addDropdown("New Department ID");
        refreshDropdowns();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshDropdowns();
            }
        });

        // 3. Button Row setup kara
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(UITheme.SURFACE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack, UITheme.SURFACE_MUTED);
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "CourseMenu"));

        // Update Button
        JButton btnUpdate = new JButton("Update Course");
        styleButton(btnUpdate, BUTTON_COLOR);

        row.add(btnBack);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnUpdate);
        add(row);

        // Update button action (Oyaage original logic eka - method wenas kale ne)
        btnUpdate.addActionListener(e -> updateCourse());
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
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        UITheme.styleTextField(field);

        add(lbl);
        add(Box.createVerticalStrut(5));
        add(field);
        add(Box.createVerticalStrut(12));
        return field;
    }

    private JComboBox<String> addTypeDropdown(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Theory", "Practical"});
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        UITheme.styleComboBox(comboBox);

        add(lbl);
        add(Box.createVerticalStrut(5));
        add(comboBox);
        add(Box.createVerticalStrut(12));
        return comboBox;
    }

    private JComboBox<String> addDropdown(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        UITheme.styleComboBox(comboBox);

        add(lbl);
        add(Box.createVerticalStrut(5));
        add(comboBox);
        add(Box.createVerticalStrut(12));
        return comboBox;
    }

    private void refreshDropdowns() {
        loadComboBox(cmbCode, courseController.getCourseCodes());
        loadComboBox(cmbLecturer, courseController.getLecturerIds());
        loadComboBox(cmbDep, courseController.getDepartmentIds());
    }

    private void loadComboBox(JComboBox<String> comboBox, java.util.List<String> values) {
        String selectedValue = selectedValue(comboBox);
        comboBox.removeAllItems();
        for (String value : values) {
            comboBox.addItem(value);
        }
        comboBox.setSelectedItem(selectedValue);
        if (comboBox.getSelectedItem() == null && comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);
        }
    }

    private String selectedValue(JComboBox<String> comboBox) {
        Object selected = comboBox.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    private void styleButton(JButton btn, Color bg) {
        if (UITheme.SURFACE_MUTED.equals(bg)) {
            UITheme.styleNeutralButton(btn);
        } else {
            UITheme.stylePrimaryButton(btn);
        }
        UITheme.setStandardButtonSize(btn);
    }

    private void clearFields() {
        if (cmbCode.getItemCount() > 0) cmbCode.setSelectedIndex(0);
        txtName.setText("");
        cmbType.setSelectedIndex(0);
        txtCredits.setText("");
        if (cmbLecturer.getItemCount() > 0) cmbLecturer.setSelectedIndex(0);
        if (cmbDep.getItemCount() > 0) cmbDep.setSelectedIndex(0);
    }

    private void updateCourse() {
        CourseFormData formData = new CourseFormData(
                selectedValue(cmbCode),
                txtName.getText(),
                txtCredits.getText(),
                cmbType.getSelectedItem().toString(),
                selectedValue(cmbLecturer),
                selectedValue(cmbDep)
        );

        CourseOperationResult result = courseController.updateCourse(formData);
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage());
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
