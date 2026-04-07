package GUI.admin;

import javax.swing.*;
import java.awt.*;

class DeleteCoursePanel extends JPanel {
    public DeleteCoursePanel(JPanel parentPanel, CardLayout cardLayout) {

        setLayout(new GridLayout(7, 2, 10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        for (int i = 0; i < 4; i++) {
            add(new JLabel(""));
        }

        add(new JLabel("Enter Course Code:"));
        JTextField txtCode = new JTextField();
        add(txtCode);


        JButton btnBack = new JButton("Back");
        JButton btnDelete = new JButton("Delete Course");
        btnDelete.setBackground(new Color(231, 76, 60)); // set button color
        btnDelete.setForeground(Color.WHITE);

        add(btnBack);
        add(btnDelete);
        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        for (int i = 0; i < 4; i++) {
            add(new JLabel(""));
        }

        // delete button action
        btnDelete.addActionListener(e -> {
            String code = txtCode.getText().trim();

            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Course Code!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmation Dialog
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                DAO.CourseDAO dao = new DAO.CourseDAO();
                boolean isSuccess = dao.deleteCourse(code);

                if (isSuccess) {
                    JOptionPane.showMessageDialog(this, "Course Deleted Successfully!");
                    txtCode.setText(""); // clear field
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed! Course Code not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}