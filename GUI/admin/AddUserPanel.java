package GUI.admin;

import javax.swing.*;
import java.awt.*;

public class AddUserPanel extends JPanel {
    public AddUserPanel() {
        setBackground(Color.WHITE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("First Name:"));
        add(new JTextField());

        add(new JLabel("Role:"));
        String[] roles = {"Admin", "Lecturer", "Student"};
        add(new JComboBox<>(roles));

        JButton btnSave = new JButton("Save User");
        add(btnSave);
    }
}