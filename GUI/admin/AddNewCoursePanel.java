package GUI.admin;

import javax.swing.*;
import java.awt.*;

class AddNewCoursePanel extends JPanel {
    public AddNewCoursePanel(JPanel parentPanel, CardLayout cardLayout) {
        setLayout(new GridLayout(7, 2, 10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Form Fields
        add(new JLabel("Course Code:"));
        JTextField txtCode = new JTextField();
        add(txtCode);

        add(new JLabel("Course Name:"));
        JTextField txtName = new JTextField();
        add(txtName);

        add(new JLabel("Type:"));
        JTextField txtType = new JTextField();
        add(txtType);

        add(new JLabel("Credits:"));
        JTextField txtCredits = new JTextField();
        add(txtCredits);

        add(new JLabel("Lecturer ID:"));
        JTextField txtLecturer = new JTextField();
        add(txtLecturer);

        add(new JLabel("Department ID:"));
        JTextField txtDep = new JTextField();
        add(txtDep);

        JButton btnBack = new JButton("Back");
        JButton btnSave = new JButton("Save Course");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);

        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        add(btnBack);
        add(btnSave);

    }
}