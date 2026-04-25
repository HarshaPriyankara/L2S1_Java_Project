package GUI.to;

import Controllers.TimetableControllers.TimetableController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Timetable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> deptCombo;
    private final TimetableController timetableController = new TimetableController();

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

        deptCombo = new JComboBox<>(new String[]{ "ICT", "ET", "BST", "Multidisciplinary"});

        JButton btnView = new JButton("View Timetable");
        btnView.setBackground(new Color(41, 128, 185));
        btnView.setForeground(Color.WHITE);
        btnView.setPreferredSize(new Dimension(130, 30));
        btnView.setFocusPainted(false);

        selectPanel.add(new JLabel("Select Department: "));
        selectPanel.add(deptCombo);
        selectPanel.add(new JLabel("  "));
        selectPanel.add(btnView);

        headerContainer.add(selectPanel);
        add(headerContainer, BorderLayout.NORTH);

        // --- Table Setup ---


        String[] columns = {"Department", "Day", "Time Slot", "Course", "Venue", "Type"};
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

        // Button Action
        btnView.addActionListener(e -> updateTableData());

        //if page load, show data
        updateTableData();
    }

    private void updateTableData() {
        model.setRowCount(0);

        String selectedDeptName = deptCombo.getSelectedItem().toString();

        // Department ID choose
        String deptId = "dep001"; // defauli
        if (selectedDeptName.equals("ICT")) {
            deptId = "dep01"; //
        } else if (selectedDeptName.equals("ET")) {
            deptId = "dep02";
        } else if (selectedDeptName.equals("BST")) {
            deptId = "dep03";
        } else if (selectedDeptName.equals("Multidisciplinary")) {
            deptId = "dep04";
        }

        // Controller using and get data
        List<Models.Timetable> list = timetableController.loadTimetable(deptId);

        if (list != null && !list.isEmpty()) {
            for (Models.Timetable tt : list) {
                model.addRow(new Object[]{
                        selectedDeptName,
                        tt.getDay(),
                        tt.getStartTime() + " - " + tt.getEndTime(),
                        tt.getCourseCode() + " : " + tt.getCourseName(), // කෝස් නම මෙතනට වැටෙනවා
                        tt.getVenue(),
                        tt.getSessionType()
                });
            }
        } else {
            // if data not, send error message
        }
    }
}