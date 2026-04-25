package GUI.to;

import GUI.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class AttendanceMainMenuPanel extends JPanel {
    public AttendanceMainMenuPanel(AttendanceManagement parent) {
        setLayout(new GridBagLayout());
        setBackground(UITheme.APP_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        String[] labels = {"Mark New Attendance", "Update/Delete Records"};
        String[] commands = {"Add", "UpdateDelete"};
        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            UITheme.styleLargeMenuButton(btn);
            btn.setPreferredSize(new Dimension(400, 80));

            final String cmd = commands[i];
            btn.addActionListener(e -> parent.showPanel(cmd));

            gbc.gridy = i;
            add(btn, gbc);
        }
    }
}
