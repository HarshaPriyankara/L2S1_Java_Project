package GUI.student;

import Controllers.TimetableControllers.TimetableController;
import GUI.common.UITheme;
import Models.Timetable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimetablePanel extends JPanel {

    private JComboBox<String> cmbYear, cmbSemester;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSearch;
    private final String studentId;
    private final TimetableController timetableController = new TimetableController();

    public TimetablePanel(String studentId) {
        this.studentId = studentId;
        setBackground(UITheme.APP_BACKGROUND);
        setLayout(new BorderLayout(15, 15));
        setBorder(UITheme.createContentBorder());

        // --- Top Panel for Filtering ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        // Academic Year සහ Semester සඳහා Dropdowns
        cmbYear = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});
        UITheme.styleComboBox(cmbYear);
        UITheme.styleComboBox(cmbSemester);

        btnSearch = new JButton("View Timetable");
        UITheme.stylePrimaryButton(btnSearch);

        topPanel.add(new JLabel("Academic Level:"));
        topPanel.add(cmbYear);
        topPanel.add(new JLabel("Semester:"));
        topPanel.add(cmbSemester);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Day", "Start Time", "End Time", "Course Code", "Course Name", "Venue", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        UITheme.styleTable(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Day
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Course Name (දිග වැඩි නිසා)

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadData());
    }

    private void loadData() {

        String level = cmbYear.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();


        TimetableController.TimetableStudentResult result = timetableController.loadStudentTimetable(studentId, level, sem);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Timetable> list = result.getTimetables();

        tableModel.setRowCount(0);

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No lectures found for " + level + " " + sem, "No Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Timetable tt : list) {
                Object[] row = {
                        tt.getDay(),
                        tt.getStartTime(),
                        tt.getEndTime(),
                        tt.getCourseCode(),
                        tt.getCourseName(),
                        tt.getVenue(),
                        tt.getSessionType()
                };
                tableModel.addRow(row);
            }
        }
    }
}
