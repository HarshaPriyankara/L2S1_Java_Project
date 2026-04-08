package GUI.to;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class Timetable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> levelCombo;

    public Timetable(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel lblTitle = new JLabel("ACADEMIC TIMETABLE", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));

        // Level
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectPanel.setBackground(Color.WHITE);
        selectPanel.add(new JLabel("Select Level: "));

        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        levelCombo = new JComboBox<>(levels);
        levelCombo.setPreferredSize(new Dimension(150, 30));

        // Level change
        levelCombo.addActionListener(e -> updateTableData());

        selectPanel.add(levelCombo);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(selectPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Day", "Time Slot", "Course Code", "Course Name", "Location"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);


        updateTableData();
    }

    private void updateTableData() {
        String selectedLevel = (String) levelCombo.getSelectedItem();
        model.setRowCount(0);


        if (selectedLevel.equals("Level 1")) {
            model.addRow(new Object[]{"Monday", "08:30 - 10:30", "ICT1113", "Programming", "Lab 01"});
            model.addRow(new Object[]{"Wednesday", "10:30 - 12:30", "MAT1122", "Mathematics", "LT 01"});
        } else if (selectedLevel.equals("Level 2")) {
            model.addRow(new Object[]{"Monday", "10:30 - 12:30", "ICT2123", "Database Systems", "Main Lab"});
            model.addRow(new Object[]{"Tuesday", "08:30 - 11:30", "ICT2143", "Networks", "Lab 02"});
        } else if (selectedLevel.equals("Level 3")) {
            model.addRow(new Object[]{"Thursday", "13:30 - 15:30", "ICT3113", "Software Eng", "Lab 03"});
        } else if (selectedLevel.equals("Level 4")) {
            model.addRow(new Object[]{"Friday", "09:00 - 12:00", "ICT4153", "Research Project", "Meeting Room"});
        }
    }
}