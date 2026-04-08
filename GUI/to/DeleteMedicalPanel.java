package GUI.to;

import javax.swing.*;
import java.awt.*;

public class DeleteMedicalPanel extends JPanel {
    public DeleteMedicalPanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("DELETE MEDICAL RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("Medical ID to Delete:"), gbc);
        gbc.gridx = 1; add(new JTextField(10), gbc);

        JButton btnDelete = new JButton("Delete Now");
        btnDelete.setBackground(new Color(66, 133, 244));
        btnDelete.setForeground(Color.WHITE);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MedicalMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.add(btnDelete); pnl.add(btnBack);
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}