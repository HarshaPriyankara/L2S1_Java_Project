package GUI.to;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Notices extends JPanel {
    public Notices(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //topic
        JLabel lblTitle = new JLabel("NOTICES", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(66, 133, 244));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        //table

        String[] columns = {"Notice ID", "Date", "Title", "Content"};


        Object[][] data = {
                {"N001", "2026-04-08", "Lab Maintenance", "Lab 01 will be closed today."},
                {"N002", "2026-04-09", "Special Lecture", "BST Lecture shifted to Lab 02."}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
    }
}