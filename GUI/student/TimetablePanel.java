package GUI.student;

import Controllers.TimetableControllers.TimetableController;
import GUI.common.UITheme;
import Models.Timetable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimetablePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private final String studentId;
    private final TimetableController timetableController = new TimetableController();

    public TimetablePanel(String studentId) {
        this.studentId = studentId;
        setBackground(UITheme.APP_BACKGROUND);
        setLayout(new BorderLayout(15, 15));
        setBorder(UITheme.createContentBorder());

        // --- Top Panel (Title Only) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        JLabel lblTitle = new JLabel("MY ACADEMIC TIMETABLE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(41, 128, 185));
        topPanel.add(lblTitle);

        add(topPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Day", "Start Time", "End Time", "Course Code", "Course Name", "Venue", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        UITheme.styleTable(table);

        // Column width
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Day
        table.getColumnModel().getColumn(4).setPreferredWidth(250); // Course Name

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        //data load
        loadData();
    }

    private void loadData() {
        // Controller using and get student
        TimetableController.TimetableStudentResult result = timetableController.loadStudentTimetable(studentId);

        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Timetable> list = result.getTimetables();
        tableModel.setRowCount(0); // old data remove

        if (list == null || list.isEmpty()) {

            tableModel.addRow(new Object[]{"-", "-", "-", "No records found", "-", "-", "-"});
        } else {
            for (Timetable tt : list) {
                tableModel.addRow(new Object[]{
                        tt.getDay(),
                        tt.getStartTime(),
                        tt.getEndTime(),
                        tt.getCourseCode(),
                        tt.getCourseName(),
                        tt.getVenue(),
                        tt.getSessionType()
                });
            }
        }
    }
}