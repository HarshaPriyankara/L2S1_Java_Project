package GUI.lecturer;

import DAO.MarkDAO;
import Utils.DBConnection;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MarksManagement extends JPanel {
    private JComboBox<String> courseComboBox;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> studentComboBox;
    private JTextField quickMarkField;
    private JLabel statusLabel;
    private JTable marksTable;
    private DefaultTableModel tableModel;
    private String lecturerID;
    private final Map<String, Double> loadedEndMarks = new HashMap<>();

    public MarksManagement(String lecturerID) {
        this.lecturerID = lecturerID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        courseComboBox = new JComboBox<>();
        loadLecturerCourses();

        typeComboBox = new JComboBox<>(MarksCalculator.MARK_TYPES);

        JButton btnLoad = new JButton("Show Students");
        styleButton(btnLoad, new Color(46, 125, 192));

        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Assessment:"));
        topPanel.add(typeComboBox);
        topPanel.add(btnLoad);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Course Code", "Assessment Type", "Mark Value", "CA", "END", "Total Marks", "Grade", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 5;
            }
        };
        marksTable = new JTable(tableModel);
        marksTable.setRowHeight(28);
        marksTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        marksTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        add(new JScrollPane(marksTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);

        JPanel quickAddPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        quickAddPanel.setBackground(Color.WHITE);
        studentComboBox = new JComboBox<>();
        studentComboBox.setPreferredSize(new Dimension(120, 28));
        quickMarkField = new JTextField(8);
        JButton btnQuickSave = new JButton("Add / Update Mark");
        styleButton(btnQuickSave, new Color(46, 125, 192));
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(80, 80, 80));

        quickAddPanel.add(new JLabel("Student:"));
        quickAddPanel.add(studentComboBox);
        quickAddPanel.add(new JLabel("Mark:"));
        quickAddPanel.add(quickMarkField);
        quickAddPanel.add(btnQuickSave);
        quickAddPanel.add(statusLabel);

        JButton btnSave = new JButton("Save / Update Marks");
        styleButton(btnSave, new Color(40, 167, 69));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        savePanel.setBackground(Color.WHITE);
        savePanel.add(btnSave);

        bottomPanel.add(quickAddPanel, BorderLayout.CENTER);
        bottomPanel.add(savePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadEnrolledStudents());
        btnQuickSave.addActionListener(e -> saveQuickMark());
        btnSave.addActionListener(e -> saveOrUpdateMarks());
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void loadLecturerCourses() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT Course_code FROM course WHERE Lecturer_in_charge = ?")) {
            pst.setString(1, lecturerID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                courseComboBox.addItem(rs.getString("Course_code"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void loadEnrolledStudents() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String selectedType = (String) typeComboBox.getSelectedItem();
        if (selectedCourse == null || selectedType == null) return;

        tableModel.setRowCount(0);
        studentComboBox.removeAllItems();
        Map<String, MarksCalculator.MarkBreakdown> breakdowns = loadBreakdownMap(selectedCourse);
        loadedEndMarks.clear();

        String query = "SELECT e.Reg_no, e.Course_code, m.Marks_value " +
                "FROM enrollment e " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "AND LOWER(REPLACE(TRIM(m.Marks_type), ' ', '_')) = LOWER(?) " +
                "WHERE e.Course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, selectedType);
            pst.setString(2, selectedCourse);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String regNo = rs.getString("Reg_no");
                studentComboBox.addItem(regNo);
                Vector<Object> row = new Vector<>();
                row.add(regNo);
                row.add(selectedCourse);
                row.add(selectedType);

                Object mark = rs.getObject("Marks_value");
                row.add(mark == null ? "" : mark);

                MarksCalculator.MarkBreakdown breakdown = breakdowns.get(regNo);
                addBreakdownColumns(row, breakdown);
                if (breakdown != null) {
                    loadedEndMarks.put(createStudentCourseKey(regNo, selectedCourse), breakdown.getEndMarks());
                }
                tableModel.addRow(row);
            }
            statusLabel.setText(tableModel.getRowCount() + " students loaded");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private Map<String, MarksCalculator.MarkBreakdown> loadBreakdownMap(String selectedCourse) {
        Map<String, MarksCalculator.MarkBreakdown> breakdowns = new HashMap<>();
        try {
            MarkDAO dao = new MarkDAO();
            List<MarksCalculator.MarkBreakdown> courseBreakdowns = dao.getCourseMarkBreakdowns(selectedCourse);
            for (MarksCalculator.MarkBreakdown breakdown : courseBreakdowns) {
                breakdowns.put(breakdown.getRegNo(), breakdown);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading calculated marks: " + e.getMessage());
        }
        return breakdowns;
    }

    private void addBreakdownColumns(Vector<Object> row, MarksCalculator.MarkBreakdown breakdown) {
        if (breakdown == null) {
            row.add(0.0);
            row.add(0.0);
            row.add(0.0);
            row.add("E");
            row.add(0.0);
            return;
        }

        row.add(breakdown.getCaMarks());
        row.add(breakdown.getEndMarks());
        row.add(breakdown.getTotalMarks());
        row.add(breakdown.getGrade());
        row.add(breakdown.getGpa());
    }

    private void saveOrUpdateMarks() {
        stopTableEditing();

        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) return;

        try (Connection conn = DBConnection.getConnection()) {
            for (int i = 0; i < rowCount; i++) {
                String regNo = tableModel.getValueAt(i, 0).toString();
                String course = tableModel.getValueAt(i, 1).toString();
                String type = tableModel.getValueAt(i, 2).toString();
                String markStr = tableModel.getValueAt(i, 3).toString().trim();
                String endStr = tableModel.getValueAt(i, 5).toString().trim();

                if (markStr.isEmpty() && endStr.isEmpty()) continue;

                if (!markStr.isEmpty()) {
                    double markValue = Double.parseDouble(markStr);
                    if (markValue < 0 || markValue > 100) {
                        JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100 for " + regNo);
                        return;
                    }
                    saveSingleAssessmentMark(conn, regNo, course, type, markValue);
                }

                if (!endStr.isEmpty()) {
                    double endMark = Double.parseDouble(endStr);
                    if (endMark < 0 || endMark > 70) {
                        JOptionPane.showMessageDialog(this, "END marks must be between 0 and 70 for " + regNo);
                        return;
                    }
                    saveEndMarkIfChanged(conn, regNo, course, endMark);
                }
            }

            JOptionPane.showMessageDialog(this, "All marks saved successfully!");
            loadEnrolledStudents();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric marks only.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + e.getMessage());
        }
    }

    private void saveQuickMark() {
        String regNo = (String) studentComboBox.getSelectedItem();
        String course = (String) courseComboBox.getSelectedItem();
        String type = (String) typeComboBox.getSelectedItem();
        String markText = quickMarkField.getText().trim();

        if (regNo == null || course == null || type == null) {
            JOptionPane.showMessageDialog(this, "Please load students first.");
            return;
        }

        if (markText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a mark value.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            double markValue = Double.parseDouble(markText);
            if (markValue < 0 || markValue > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.");
                return;
            }

            saveSingleAssessmentMark(conn, regNo, course, type, markValue);
            quickMarkField.setText("");
            statusLabel.setText("Saved " + type + " for " + regNo);
            loadEnrolledStudents();
            selectStudentRow(regNo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric mark.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving mark: " + e.getMessage());
        }
    }

    private void stopTableEditing() {
        if (marksTable.isEditing()) {
            marksTable.getCellEditor().stopCellEditing();
        }
    }

    private void selectStudentRow(String regNo) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (regNo.equals(tableModel.getValueAt(i, 0).toString())) {
                marksTable.setRowSelectionInterval(i, i);
                marksTable.scrollRectToVisible(marksTable.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void saveSingleAssessmentMark(Connection conn, String regNo, String course,
                                          String type, double markValue) throws SQLException {
        String checkQuery = "SELECT Mark_id FROM MARK WHERE Reg_no=? AND Course_code=? " +
                "AND LOWER(REPLACE(TRIM(Marks_type), ' ', '_')) = LOWER(?)";
        try (PreparedStatement checkPst = conn.prepareStatement(checkQuery)) {
            checkPst.setString(1, regNo);
            checkPst.setString(2, course);
            checkPst.setString(3, type);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE MARK SET Marks_value=?, Marks_type=? WHERE Mark_id=?";
                try (PreparedStatement upPst = conn.prepareStatement(updateQuery)) {
                    upPst.setDouble(1, markValue);
                    upPst.setString(2, type);
                    upPst.setInt(3, rs.getInt("Mark_id"));
                    upPst.executeUpdate();
                }
            } else {
                String insertQuery = "INSERT INTO MARK (Reg_no, Course_code, Marks_type, Marks_value) VALUES (?, ?, ?, ?)";
                try (PreparedStatement inPst = conn.prepareStatement(insertQuery)) {
                    inPst.setString(1, regNo);
                    inPst.setString(2, course);
                    inPst.setString(3, type);
                    inPst.setDouble(4, markValue);
                    inPst.executeUpdate();
                }
            }
        }
    }

    private void saveEndMarkIfChanged(Connection conn, String regNo, String course, double endMark) throws SQLException {
        Double loadedEndMark = loadedEndMarks.get(createStudentCourseKey(regNo, course));
        if (loadedEndMark != null && Math.abs(loadedEndMark - endMark) < 0.01) {
            return;
        }

        double rawEndMark = Math.round((endMark * 100.0 / 70.0) * 100.0) / 100.0;
        if (rawEndMark > 100) rawEndMark = 100;

        boolean hasTheory = hasAssessmentMark(conn, regNo, course, "End_theory");
        boolean hasPractical = hasAssessmentMark(conn, regNo, course, "End_practical");

        if (hasTheory && hasPractical) {
            saveSingleAssessmentMark(conn, regNo, course, "End_theory", rawEndMark);
            saveSingleAssessmentMark(conn, regNo, course, "End_practical", rawEndMark);
        } else if (hasPractical) {
            saveSingleAssessmentMark(conn, regNo, course, "End_practical", rawEndMark);
        } else {
            saveSingleAssessmentMark(conn, regNo, course, "End_theory", rawEndMark);
        }
    }

    private boolean hasAssessmentMark(Connection conn, String regNo, String course, String type) throws SQLException {
        String query = "SELECT Mark_id FROM MARK WHERE Reg_no=? AND Course_code=? " +
                "AND LOWER(REPLACE(TRIM(Marks_type), ' ', '_')) = LOWER(?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, type);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        }
    }

    private String createStudentCourseKey(String regNo, String course) {
        return regNo + "|" + course;
    }
}
