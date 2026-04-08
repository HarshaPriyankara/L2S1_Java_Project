package GUI.to;

import javax.swing.*;
import java.awt.*;

public class UpdateAttendancePanel extends JPanel {
    public UpdateAttendancePanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("UPDATE ATTENDANCE RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("Student Index:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("New Status:"), gbc);
        gbc.gridx = 1; add(new JComboBox<>(new String[]{"Present", "Absent", "Medical"}), gbc);

        JPanel btnPnl = new JPanel();
        btnPnl.setBackground(Color.WHITE);
        JButton btnUpdate = new JButton("Update Now");
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "AttendanceMenu"));

        btnPnl.add(btnUpdate); btnPnl.add(btnBack);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        add(btnPnl, gbc);
    }
}