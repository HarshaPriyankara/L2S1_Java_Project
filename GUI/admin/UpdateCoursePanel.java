package GUI.admin;

import javax.swing.*;
import java.awt.*;

class UpdateCoursePanel extends JPanel {
    public UpdateCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // fields
        add(new JLabel("Course Code (to Update):"));
        JTextField txtCode = new JTextField();
        add(txtCode);

        add(new JLabel("New Course Name:"));
        JTextField txtName = new JTextField();
        add(txtName);

        add(new JLabel("New Type:"));
        JTextField txtType = new JTextField();
        add(txtType);

        add(new JLabel("New Credits:"));
        JTextField txtCredits = new JTextField();
        add(txtCredits);

        add(new JLabel("New Lecturer ID:"));
        JTextField txtLecturer = new JTextField();
        add(txtLecturer);

        add(new JLabel("New Department ID:"));
        JTextField txtDep = new JTextField();
        add(txtDep);

        JButton btnBack = new JButton("Back");
        JButton btnUpdate = new JButton("Update Course");
        btnUpdate.setBackground(new Color(46, 204, 113)); // button color
        btnUpdate.setForeground(Color.WHITE);

        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        add(btnBack);
        add(btnUpdate);
    }
}