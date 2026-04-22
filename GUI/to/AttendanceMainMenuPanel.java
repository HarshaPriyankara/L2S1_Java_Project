package GUI.to;

import javax.swing.*;
import java.awt.*;

public class AttendanceMainMenuPanel extends JPanel {
    public AttendanceMainMenuPanel(AttendanceManagement parent) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        String[] labels = {"Mark New Attendance", "Update/Delete Records"};
        String[] commands = {"Add", "UpdateDelete"};
        Color btnColor = new Color(46, 125, 192);

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            btn.setPreferredSize(new Dimension(400, 80));
            btn.setBackground(btnColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));

            final String cmd = commands[i];
            btn.addActionListener(e -> parent.showPanel(cmd));

            gbc.gridy = i;
            add(btn, gbc);
        }
    }
}