package GUI.admin;

import Controllers.CourseControllers.CourseController;
import Controllers.CourseControllers.CourseOperationResult;

import javax.swing.*;
import java.awt.*;

class DeleteCoursePanel extends JPanel {
    private JTextField txtCode;
    private static final Color DELETE_COLOR = new Color(0xCC0000); // Red color for delete
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private final CourseController courseController = new CourseController();

    public DeleteCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        // Layout and Styling setup
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title
        addTitle("Delete Course", DELETE_COLOR);

        // 2. Description label
        JLabel desc = new JLabel("Enter the Course Code of the course you want to delete.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(Color.GRAY);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(desc);
        add(Box.createVerticalStrut(20));

        // 3. Input Field (addField helper method eka use kara)
        txtCode = addField("Course Code");

        // 4. Button Row setup
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Back Button
        JButton btnBack = new JButton("Back");
        styleButton(btnBack, new Color(0xAAAAAA));
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "CourseMenu"));

        // Delete Button
        JButton btnDelete = new JButton("Delete Course");
        styleButton(btnDelete, DELETE_COLOR);

        row.add(btnBack);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnDelete);
        add(row);

        // --- Delete Button Action (Oyaage original logic eka) ---
        btnDelete.addActionListener(e -> {
            String code = txtCode.getText().trim();

            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Course Code!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete course " + code + "?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                CourseOperationResult result = courseController.deleteCourse(code);
                if (result.isSuccess()) {
                    JOptionPane.showMessageDialog(this, result.getMessage());
                    txtCode.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // --- UserManagement Styling Methods ---

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
}
