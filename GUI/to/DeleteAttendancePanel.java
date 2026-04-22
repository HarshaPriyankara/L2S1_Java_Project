/*package GUI.to;

import javax.swing.*;
import java.awt.*;

public class DeleteAttendancePanel extends JPanel {
    public DeleteAttendancePanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Heading Title
        JLabel lblTitle = new JLabel("DELETE ATTENDANCE RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        //Input Fields
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Student Index:"), gbc);
        gbc.gridx = 1; add(new JTextField(15), gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1; add(new JTextField(15), gbc);

        //Button
        JButton btnDelete = new JButton("Delete Now");
        btnDelete.setBackground(new Color(66, 133, 244));
        btnDelete.setForeground(Color.WHITE);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "AttendanceMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.add(btnDelete); pnl.add(btnBack);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}*/