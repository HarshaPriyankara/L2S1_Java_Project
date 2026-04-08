package GUI.to;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicalPanel extends JPanel {
    public AddMedicalPanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Heading Title
        JLabel lblTitle = new JLabel("ADD NEW MEDICAL RECORD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(66, 133, 244));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        //Form Fields
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("Session Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; add(new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20), gbc);

        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("Session Type:"), gbc);
        gbc.gridx = 1; add(new JComboBox<>(new String[]{"NormalDay", "Exam"}), gbc);

        gbc.gridy = 4; gbc.gridx = 0; add(new JLabel("Exam Course (Optional):"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        gbc.gridy = 5; gbc.gridx = 0; add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        //Buttons
        JButton btnSave = new JButton("Save Medical");
        btnSave.setBackground(new Color(66, 133, 244));
        btnSave.setForeground(Color.WHITE);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MedicalMenu"));

        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.add(btnSave); pnl.add(btnBack);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        add(pnl, gbc);
    }
}