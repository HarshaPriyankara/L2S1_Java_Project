package GUI.admin;

import Controllers.CourseControllers.CourseController;
import Controllers.CourseControllers.CourseFormData;
import Controllers.CourseControllers.CourseOperationResult;
import GUI.common.UITheme;
import javax.swing.*;
import java.awt.*;

class AddNewCoursePanel extends JPanel {

    private JTextField txtCode, txtName, txtCredits;
    private JComboBox<String> cmbType, cmbLecturer, cmbDep;
    private static final Color BUTTON_COLOR = UITheme.PRIMARY;
    private final CourseController courseController = new CourseController();

    public AddNewCoursePanel(JPanel parentPanel, CardLayout cardLayout) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.SURFACE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Create New Course", BUTTON_COLOR);

        txtCode     = addField("*Course Code");
        txtName     = addField("*Course Name");
        cmbType     = addTypeDropdown("Type");
        txtCredits  = addField("*Credits");
        cmbLecturer = addDropdown("Lecturer ID");
        cmbDep      = addDropdown("Department ID");
        refreshDropdowns();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshDropdowns();
            }
        });

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(UITheme.SURFACE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack, UITheme.SURFACE_MUTED);
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "CourseMenu"));

        // Save Button
        JButton btnSave = new JButton("Save Course");
        styleButton(btnSave, BUTTON_COLOR);

        row.add(btnBack);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnSave);
        add(row);

        // Save Button Action
        btnSave.addActionListener(e -> saveCourse());
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
        loadComboBox(cmbLecturer, courseController.getLecturerIds());
        loadComboBox(cmbDep, courseController.getDepartmentIds());
    }

    private void loadComboBox(JComboBox<String> comboBox, java.util.List<String> values) {
        comboBox.removeAllItems();
        for (String value : values) {
            comboBox.addItem(value);
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
        btn.setPreferredSize(new Dimension(130, 38));
    }

    private void saveCourse() {
        CourseFormData formData = new CourseFormData(
                txtCode.getText(),
                txtName.getText(),
                txtCredits.getText(),
                cmbType.getSelectedItem().toString(),
                selectedValue(cmbLecturer),
                selectedValue(cmbDep)
        );

        CourseOperationResult result = courseController.addCourse(formData);
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage());
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        cmbType.setSelectedIndex(0);
        txtCredits.setText("");
        if (cmbLecturer.getItemCount() > 0) cmbLecturer.setSelectedIndex(0);
        if (cmbDep.getItemCount() > 0) cmbDep.setSelectedIndex(0);
    }
}
