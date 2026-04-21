package GUI.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import DAO.CourseDAO;

public class CoursePanel extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private String studentID;

    public CoursePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Label
        JLabel lblTitle = new JLabel("My Enrolled Courses");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table Columns
        String[] columns = {"Course Code", "Course Name", "Credits"};
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);

        // Table Style
        courseTable.setRowHeight(30);
        courseTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);
        loadStudentData();
    }

    private void loadStudentData() {
        try {
            CourseDAO dao = new CourseDAO();
            List<String[]> data = dao.getStudentCourses(studentID);

            tableModel.setRowCount(0); // remove before content
            for (String[] row : data) {
                tableModel.addRow(row);
            }

            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No courses found for ID: " + studentID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }
}