package GUI.to;

import DAO.TimetableDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Timetable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> levelCombo, semesterCombo, deptCombo;

    public Timetable(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Header Container ---
        JPanel headerContainer = new JPanel(new GridLayout(2, 1, 0, 10));
        headerContainer.setBackground(Color.WHITE);
        headerContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // 1. Title Row
        JLabel lblTitle = new JLabel("FACULTY ACADEMIC TIMETABLE", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(41, 128, 185));
        headerContainer.add(lblTitle);

        // 2. Filter Row
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectPanel.setBackground(Color.WHITE);

        levelCombo = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        semesterCombo = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});


        deptCombo = new JComboBox<>(new String[]{ "ICT", "ET", "BST", "Multidisciplinary"});

        JButton btnView = new JButton("View Timetable");
        btnView.setBackground(new Color(41, 128, 185));
        btnView.setForeground(Color.WHITE);
        btnView.setPreferredSize(new Dimension(130, 30));
        btnView.setFocusPainted(false);

        selectPanel.add(new JLabel("Level: "));
        selectPanel.add(levelCombo);
        selectPanel.add(new JLabel("  Sem: "));
        selectPanel.add(semesterCombo);
        selectPanel.add(new JLabel("  Dept: "));
        selectPanel.add(deptCombo);
        selectPanel.add(new JLabel("  "));
        selectPanel.add(btnView);

        headerContainer.add(selectPanel);
        add(headerContainer, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Level", "Sem", "Department", "Day", "Time Slot", "Course", "Venue", "Type"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        btnView.addActionListener(e -> updateTableData());

        // if page load , show data
        updateTableData();
    }

    private void updateTableData() {
        model.setRowCount(0);

        String selectedLevel = levelCombo.getSelectedItem().toString();
        String selectedSem = semesterCombo.getSelectedItem().toString();
        String selectedDeptName = deptCombo.getSelectedItem().toString();


        String deptId = "ALL";
        if (selectedDeptName.equals("ICT")) {
            deptId = "D1";
        } else if (selectedDeptName.equals("ET")) {
            deptId = "D2";
        } else if (selectedDeptName.equals("BST")) {
            deptId = "D3";
        } else if (selectedDeptName.equals("Multidisciplinary")) {
            deptId = "D4";
        }

        // get filter data using DAO
        TimetableDAO dao = new TimetableDAO();
        List<Models.Timetable> list = dao.getTOTimetableFiltered(selectedLevel, selectedSem, deptId);

        if (list != null && !list.isEmpty()) {
            for (Models.Timetable tt : list) {
                model.addRow(new Object[]{
                        selectedLevel,
                        selectedSem,
                        tt.getDepartmentId(), // මෙතන දැන් එන්නේ 'Information & Communication Technology' වගේ Full Name එක
                        tt.getDay(),
                        tt.getStartTime() + " - " + tt.getEndTime(),
                        tt.getCourseCode() + " : " + tt.getCourseName(),
                        tt.getVenue(),
                        tt.getSessionType()
                });
            }
        } else {

        }
    }
}