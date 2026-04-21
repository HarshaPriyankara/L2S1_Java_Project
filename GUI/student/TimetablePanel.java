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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(Color.WHITE);

        cmbYear = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        cmbSemester = new JComboBox<>(new String[]{"1", "2"});

        btnSearch = new JButton("View Timetable");
        btnSearch.setBackground(new Color(46, 125, 192));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSearch.setFocusPainted(false);

        topPanel.add(new JLabel("Academic Year:"));
        topPanel.add(cmbYear);
        topPanel.add(new JLabel("Semester:"));
        topPanel.add(cmbSemester);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // --- වෙනස් කළ යුතු කොටස 1: "Course Name" column එක එකතු කිරීම ---
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

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadData());
    }

    private void loadData() {
        String level = cmbYear.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();
        String deptId = "D0001";

        TimetableDAO dao = new TimetableDAO();
        List<Timetable> list = dao.getStudentTimetable(deptId, level, sem);

        tableModel.setRowCount(0);

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records found for Level " + level + " Semester " + sem);
        } else {
            // --- වෙනස් කළ යුතු කොටස 2: Row එකට Course Name එක ඇතුළත් කිරීම ---
            for (Timetable tt : list) {
                Object[] row = {
                        tt.getDay(),
                        tt.getStartTime(),
                        tt.getEndTime(),
                        tt.getCourseCode(),
                        tt.getCourseName(), // DAO එකෙන් දැන් මේකත් එනවා
                        tt.getVenue(),
                        tt.getSessionType()
                };
                tableModel.addRow(row);
            }
        }
    }
}