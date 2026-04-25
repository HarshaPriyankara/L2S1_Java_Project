package GUI.student;

import DAO.MarkDAO;
import Utils.GpaCalculator;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GradePanel extends JPanel {
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JLabel gpaLabel;
    private String studentID;

    public GradePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Grades / GPA");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Course Code", "Course Name", "Total Marks", "Grade", "Grade Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gradeTable = new JTable(tableModel);
        gradeTable.setRowHeight(30);
        gradeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        gpaLabel = new JLabel("SGPA: Not available | CGPA: Not available", SwingConstants.RIGHT);
        gpaLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gpaLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        add(gpaLabel, BorderLayout.SOUTH);

        loadGradeData();
    }

    private void loadGradeData() {
        tableModel.setRowCount(0);

        try {
            MarkDAO dao = new MarkDAO();
            List<MarksCalculator.MarkBreakdown> breakdowns = dao.getStudentMarkBreakdowns(studentID);

            if (breakdowns.isEmpty()) {
                gpaLabel.setText("No grade data available");
                return;
            }

            GpaCalculator.GpaResult gpaResult = GpaCalculator.calculate(breakdowns);

            for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
                tableModel.addRow(new Object[]{
                        breakdown.getCourseCode(),
                        breakdown.getCourseName(),
                        breakdown.hasMarks() ? breakdown.getTotalMarks() : "Pending",
                        breakdown.getGrade(),
                        breakdown.hasMarks() ? breakdown.getGradePoint() : "Pending"
                });
            }

            if (gpaResult.isSgpaAvailable() && gpaResult.isCgpaAvailable()) {
                gpaLabel.setText(String.format("SGPA: %.2f | CGPA: %.2f",
                        gpaResult.getSgpa(), gpaResult.getCgpa()));
            } else {
                gpaLabel.setText("SGPA: Not available | CGPA: Not available");
            }
        } catch (SQLException e) {
            gpaLabel.setText("Error loading grades");
            JOptionPane.showMessageDialog(this, "Error loading grades: " + e.getMessage());
        }
    }
}
