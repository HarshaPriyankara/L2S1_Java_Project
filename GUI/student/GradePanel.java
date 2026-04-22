package GUI.student;

import DAO.MarkDAO;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GradePanel extends JPanel {
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JLabel sgpaLabel;
    private String studentID;

    public GradePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Grades / GPA");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Course Code", "Course Name", "Grade", "GPA", "SGPA"};
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

        sgpaLabel = new JLabel("SGPA: 0.00", SwingConstants.RIGHT);
        sgpaLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        sgpaLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        add(sgpaLabel, BorderLayout.SOUTH);

        loadGradeData();
    }

    private void loadGradeData() {
        tableModel.setRowCount(0);

        try {
            MarkDAO dao = new MarkDAO();
            List<MarksCalculator.MarkBreakdown> breakdowns = dao.getStudentMarkBreakdowns(studentID);

            if (breakdowns.isEmpty()) {
                sgpaLabel.setText("No grade data available");
                return;
            }

            double sgpa = MarksCalculator.calculateSGPA(breakdowns);

            for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
                tableModel.addRow(new Object[]{
                        breakdown.getCourseCode(),
                        breakdown.getCourseName(),
                        breakdown.getGrade(),
                        breakdown.getGpa(),
                        sgpa
                });
            }

            sgpaLabel.setText(String.format("SGPA: %.2f", sgpa));
        } catch (SQLException e) {
            sgpaLabel.setText("Error loading grades");
            JOptionPane.showMessageDialog(this, "Error loading grades: " + e.getMessage());
        }
    }
}
