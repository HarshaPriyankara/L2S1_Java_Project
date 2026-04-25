package GUI.lecturer;

import Controllers.StudentControllers.LecturerAttendanceController;
import Controllers.StudentControllers.LecturerAttendanceResult;
import Controllers.StudentControllers.LecturerAttendanceSummaryRow;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentAttendancePanel extends JPanel {
    private final LecturerAttendanceController controller = new LecturerAttendanceController();
    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JComboBox<String> viewComboBox = new JComboBox<>(new String[]{"Overall", "Theory", "Practical"});
    private final JTextField searchField = new JTextField(12);
    private final DefaultTableModel summaryModel;
    private final DefaultTableModel detailsModel;
    private final JLabel selectedStudentLabel = new JLabel("Student Details: -");
    private final Runnable onBack;
    private final String lecturerId;

    public StudentAttendancePanel(String lecturerId, Runnable onBack) {
        this.lecturerId = lecturerId;
        this.onBack = onBack;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        JButton backButton = new JButton("Back");
        UITheme.styleNeutralButton(backButton);
        UITheme.setStandardButtonSize(backButton);
        backButton.addActionListener(e -> this.onBack.run());

        UITheme.styleComboBox(courseComboBox);
        UITheme.styleComboBox(viewComboBox);
        UITheme.styleTextField(searchField);

        JButton filterButton = new JButton("Load Attendance");
        UITheme.stylePrimaryButton(filterButton);
        UITheme.setWideButtonSize(filterButton);

        JButton showAllButton = new JButton("Show All");
        UITheme.styleNeutralButton(showAllButton);
        UITheme.setStandardButtonSize(showAllButton);

        topPanel.add(backButton);
        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("View:"));
        topPanel.add(viewComboBox);
        topPanel.add(new JLabel("Reg No:"));
        topPanel.add(searchField);
        topPanel.add(filterButton);
        topPanel.add(showAllButton);
        add(topPanel, BorderLayout.NORTH);

        summaryModel = new DefaultTableModel(
                new String[]{"Reg No", "Attended Hours", "Total Hours", "Attended Sessions", "Total Sessions", "Attendance %"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable summaryTable = new JTable(summaryModel);
        summaryTable.setRowHeight(28);
        UITheme.styleTable(summaryTable);

        detailsModel = new DefaultTableModel(new String[]{"Date", "Session Type", "Hours", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable detailsTable = new JTable(detailsModel);
        detailsTable.setRowHeight(26);
        UITheme.styleTable(detailsTable);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        centerPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel summaryPanel = new JPanel(new BorderLayout(0, 8));
        summaryPanel.setBackground(UITheme.APP_BACKGROUND);
        JLabel batchLabel = new JLabel("Batch Attendance Overview");
        batchLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        batchLabel.setForeground(UITheme.TEXT_PRIMARY);
        summaryPanel.add(batchLabel, BorderLayout.NORTH);
        summaryPanel.add(new JScrollPane(summaryTable), BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new BorderLayout(0, 8));
        detailsPanel.setBackground(UITheme.APP_BACKGROUND);
        selectedStudentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        selectedStudentLabel.setForeground(UITheme.TEXT_PRIMARY);
        detailsPanel.add(selectedStudentLabel, BorderLayout.NORTH);
        detailsPanel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);

        centerPanel.add(summaryPanel);
        centerPanel.add(detailsPanel);
        add(centerPanel, BorderLayout.CENTER);

        filterButton.addActionListener(e -> loadAttendance());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadAttendance();
        });

        loadLecturerCourses();
    }

    private void loadLecturerCourses() {
        try {
            courseComboBox.removeAllItems();
            for (String course : controller.loadLecturerCourses(lecturerId)) {
                courseComboBox.addItem(course);
            }
            if (courseComboBox.getItemCount() > 0) {
                loadAttendance();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load lecturer courses: " + ex.getMessage());
        }
    }

    private void loadAttendance() {
        summaryModel.setRowCount(0);
        detailsModel.setRowCount(0);
        selectedStudentLabel.setText("Student Details: -");

        String courseCode = (String) courseComboBox.getSelectedItem();
        String sessionFilter = (String) viewComboBox.getSelectedItem();
        String studentId = searchField.getText().trim();

        LecturerAttendanceResult result = controller.loadAttendance(courseCode, sessionFilter, studentId);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        for (LecturerAttendanceSummaryRow row : result.getSummaryRows()) {
            summaryModel.addRow(new Object[]{
                    row.getRegNo(),
                    row.getAttendedHours(),
                    row.getTotalHours(),
                    row.getAttendedSessions(),
                    row.getTotalSessions(),
                    String.format("%.2f%%", row.getPercentage())
            });
        }

        String detailStudent = studentId;
        if ((detailStudent == null || detailStudent.isBlank()) && !result.getSummaryRows().isEmpty()) {
            detailStudent = result.getSummaryRows().get(0).getRegNo();
        }

        if (detailStudent != null && !detailStudent.isBlank()) {
            selectedStudentLabel.setText("Student Details: " + detailStudent);
        }

        for (Object[] row : result.getDetailRows()) {
            detailsModel.addRow(row);
        }
    }
}
