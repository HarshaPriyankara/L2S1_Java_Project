package GUI.to;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAttendancePanel extends JPanel {
    public AddAttendancePanel(CardLayout cardLayout, JPanel mainContainer) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("ADD NEW ATTENDANCE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(52, 152, 219));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        //Student Index
        gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("Student Index (Reg_no):"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        //Course Code
        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        //Date
        gbc.gridy = 3; gbc.gridx = 0; add(new JLabel("Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; add(new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20), gbc);

        //Type
        gbc.gridy = 4; gbc.gridx = 0; add(new JLabel("Session Type:"), gbc);
        gbc.gridx = 1; add(new JComboBox<>(new String[]{"Theory", "Practical"}), gbc);

        //Hours
        gbc.gridy = 5; gbc.gridx = 0; add(new JLabel("Hours:"), gbc);
        gbc.gridx = 1; add(new JTextField(20), gbc);

        //Status
        gbc.gridy = 6; gbc.gridx = 0; add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; add(new JComboBox<>(new String[]{"Present", "Absent", "Medical"}), gbc);

        //Buttons
        JPanel btnPnl = new JPanel();
        btnPnl.setBackground(Color.WHITE);
        JButton btnSave = new JButton("Save Record");
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "AttendanceMenu"));

        btnPnl.add(btnSave); btnPnl.add(btnBack);
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        add(btnPnl, gbc);
    }
}