package GUI.to;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicalPanel extends JPanel {
    public AddMedicalPanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Heading Title
        JLabel lblTitle = new JLabel("ADD NEW MEDICAL RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(UITheme.PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        //Form Fields
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1; JTextField regField = new JTextField(20); UITheme.styleTextField(regField); add(regField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("Upload Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20); UITheme.styleTextField(dateField); add(dateField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("Session Type:"), gbc);
        gbc.gridx = 1; JComboBox<String> typeBox = new JComboBox<>(new String[]{"NormalDay", "Exam"}); UITheme.styleComboBox(typeBox); add(typeBox, gbc);

        gbc.gridy = 4; gbc.gridx = 0; add(new JLabel("Exam Course (Optional):"), gbc);
        gbc.gridx = 1; JTextField examField = new JTextField(20); UITheme.styleTextField(examField); add(examField, gbc);

        gbc.gridy = 5; gbc.gridx = 0; add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1; JTextField reasonField = new JTextField(20); UITheme.styleTextField(reasonField); add(reasonField, gbc);

        //Buttons
        JButton btnSave = new JButton("Save Medical");
        UITheme.stylePrimaryButton(btnSave);
        UITheme.setWideButtonSize(btnSave);

        JButton btnBack = new JButton("Back");
        UITheme.styleNeutralButton(btnBack);
        UITheme.setStandardButtonSize(btnBack);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MedicalMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(UITheme.APP_BACKGROUND);
        pnl.add(btnSave); pnl.add(btnBack);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}
