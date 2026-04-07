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

        // back to menu
        btnBack.addActionListener(e -> cardLayout.show(parentPanel, "Menu"));

        add(btnBack);
        add(btnDelete);

        for (int i = 0; i < 4; i++) {
            add(new JLabel(""));
        }
    }
}