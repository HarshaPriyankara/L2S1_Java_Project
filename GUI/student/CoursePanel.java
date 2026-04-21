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
    private JLabel lblStatus; // show error messages label

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

        // Status Label - first hide
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 16));
        lblStatus.setForeground(Color.RED);

        // Table Setup
        String[] columns = {"Course Code", "Course Name", "Type", "Credits", "Lecturer"};
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        //Table style
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

            tableModel.setRowCount(0);

            if (data == null || data.isEmpty()) {
                // if not data show error label
                courseTable.setVisible(false);
                lblStatus.setText("You haven't registered for any courses yet.");
                add(lblStatus, BorderLayout.SOUTH); // show message
            } else {
                courseTable.setVisible(true);
                lblStatus.setText(""); // remove message
                for (String[] row : data) {
                    tableModel.addRow(row);
                }
            }

            this.revalidate();
            this.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            lblStatus.setText("Error loading data: " + e.getMessage());
            add(lblStatus, BorderLayout.SOUTH);
        }
    }
}