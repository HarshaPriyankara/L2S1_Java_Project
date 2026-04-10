package GUI.lecturer;

import javax.swing.*;
import java.awt.*;

public class AddCourseMaterialPanel extends JPanel {

    private JTextField txtMaterialID, txtTitle, txtCourseCode, txtDescription;
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public AddCourseMaterialPanel() {
        // Layout and Padding
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title
        addTitle("Upload Course Materials", BUTTON_COLOR);

        // 2. Input Fields
        txtMaterialID = addField("*Material ID");
        txtTitle      = addField("*Material Title (e.g., Lecture 01)");
        txtCourseCode = addField("*Course Code");
        txtDescription= addField("Description / File Link");

        // 3. Button Row
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Save Button (Back button eka sampurnayen ain kara)
        JButton btnSave = new JButton("Upload Material");
        styleButton(btnSave, BUTTON_COLOR);

        row.add(btnSave);
        add(row);

        // Save Action logic
        btnSave.addActionListener(e -> {
            saveMaterial();
        });
    }

    private void saveMaterial() {
        String mid = txtMaterialID.getText().trim();
        String title = txtTitle.getText().trim();
        String cCode = txtCourseCode.getText().trim();

        if (mid.isEmpty() || title.isEmpty() || cCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill required fields (*)", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Database logic eka methanata danna
        JOptionPane.showMessageDialog(this, "Material details added successfully!");
        clearFields();
    }

    // ── Helper Methods ──

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
        btn.setPreferredSize(new Dimension(150, 38));
    }

    private void clearFields() {
        txtMaterialID.setText("");
        txtTitle.setText("");
        txtCourseCode.setText("");
        txtDescription.setText("");
    }
}