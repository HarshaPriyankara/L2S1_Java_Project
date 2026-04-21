package GUI.student;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TimetablePanel extends JPanel {

    private JComboBox<String> cmbYear, cmbSemester;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSearch;

    public TimetablePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Panel for Filtering ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(Color.WHITE);

        // Academic Year සහ Semester සඳහා Dropdowns
        cmbYear = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});

        btnSearch = new JButton("View Timetable");
        btnSearch.setBackground(new Color(46, 125, 192));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSearch.setFocusPainted(false);

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
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Day
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Course Name (දිග වැඩි නිසා)

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadData());
    }

    private void loadData() {

        String level = cmbYear.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();


        String deptId = "D1";

        TimetableDAO dao = new TimetableDAO();

        // DAO එකෙන් ඩේටාබේස් එකේ නැති Level/Sem දත්ත අරගෙන එනවා (Course Code එක පාවිච්චි කරලා)
        List<Timetable> list = dao.getStudentTimetable(deptId, level, sem);

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