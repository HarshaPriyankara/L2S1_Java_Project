package GUI.to;

import javax.swing.*;
import java.awt.*;

public class UpdateMedicalPanel extends JPanel {
    public UpdateMedicalPanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("UPDATE MEDICAL RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Medical ID to Update:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("New Registration No:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("New Reason:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        JButton btnUpdate = new JButton("Update Medical");
        btnUpdate.setBackground(new Color(66, 133, 244));
        btnUpdate.setForeground(Color.WHITE);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MedicalMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.add(btnUpdate); pnl.add(btnBack);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}