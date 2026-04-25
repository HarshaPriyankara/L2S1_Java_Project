package GUI.to;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class UpdateMedicalPanel extends JPanel {
    public UpdateMedicalPanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("UPDATE MEDICAL RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(UITheme.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Medical ID to Update:"), gbc);
        gbc.gridx = 1; JTextField idField = new JTextField(20); UITheme.styleTextField(idField); add(idField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("New Registration No:"), gbc);
        gbc.gridx = 1; JTextField regField = new JTextField(20); UITheme.styleTextField(regField); add(regField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("New Reason:"), gbc);
        gbc.gridx = 1; JTextField reasonField = new JTextField(20); UITheme.styleTextField(reasonField); add(reasonField, gbc);

        JButton btnUpdate = new JButton("Update Medical");
        UITheme.stylePrimaryButton(btnUpdate);
        UITheme.setWideButtonSize(btnUpdate);

        JButton btnBack = new JButton("Back");
        UITheme.styleNeutralButton(btnBack);
        UITheme.setStandardButtonSize(btnBack);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MedicalMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(UITheme.APP_BACKGROUND);
        pnl.add(btnUpdate); pnl.add(btnBack);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}
