package GUI.lecturer;

import Controllers.MarksControllers.MarksLoadResult;
import Controllers.MarksControllers.MarksManagementController;
import Controllers.MarksControllers.MarksSaveResult;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final MarksManagementController marksController = new MarksManagementController();

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
        courseComboBox.addActionListener(e -> refreshAssessmentTypes());
        refreshAssessmentTypes();

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
        try {
            for (String course : marksController.loadLecturerCourses(lecturerID)) {
                courseComboBox.addItem(course);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void refreshAssessmentTypes() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        typeComboBox.removeAllItems();
        String[] markTypes = selectedCourse == null
                ? MarksCalculator.MARK_TYPES
                : marksController.getAllowedMarkTypes(selectedCourse);
        for (String markType : markTypes) {
            typeComboBox.addItem(markType);
        }
    }

    private void loadEnrolledStudents() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String selectedType = (String) typeComboBox.getSelectedItem();
        if (selectedCourse == null || selectedType == null) {
            return;
        }

        tableModel.setRowCount(0);
        studentComboBox.removeAllItems();
        loadedEndMarks.clear();

        MarksLoadResult result = marksController.loadEnrolledStudents(selectedCourse, selectedType);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        loadedEndMarks.putAll(result.getLoadedEndMarks());
        for (String studentId : result.getStudentIds()) {
            studentComboBox.addItem(studentId);
        }
        for (Object[] row : result.getTableRows()) {
            tableModel.addRow(row);
        }
        statusLabel.setText(result.getStatusMessage());
    }

    private void saveOrUpdateMarks() {
        stopTableEditing();
        if (tableModel.getRowCount() == 0) {
            return;
        }

        MarksSaveResult result = marksController.saveAllMarks(extractTableRows(), loadedEndMarks);
        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                result.isSuccess() ? "Success" : "Error",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );

        if (result.isSuccess()) {
            loadEnrolledStudents();
        }
    }

    private void saveQuickMark() {
        String regNo = (String) studentComboBox.getSelectedItem();
        String course = (String) courseComboBox.getSelectedItem();
        String type = (String) typeComboBox.getSelectedItem();
        String markText = quickMarkField.getText().trim();

        MarksSaveResult result = marksController.saveQuickMark(regNo, course, type, markText);
        if (result.isSuccess()) {
            quickMarkField.setText("");
            statusLabel.setText(result.getMessage());
            loadEnrolledStudents();
            selectStudentRow(regNo);
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage());
        }
    }

    private void stopTableEditing() {
        if (marksTable.isEditing()) {
            marksTable.getCellEditor().stopCellEditing();
        }
    }

    private void selectStudentRow(String regNo) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (regNo.equals(String.valueOf(tableModel.getValueAt(i, 0)))) {
                marksTable.setRowSelectionInterval(i, i);
                marksTable.scrollRectToVisible(marksTable.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private List<Object[]> extractTableRows() {
        List<Object[]> rows = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rows.add(new Object[]{
                    tableModel.getValueAt(i, 0),
                    tableModel.getValueAt(i, 1),
                    tableModel.getValueAt(i, 2),
                    tableModel.getValueAt(i, 3),
                    tableModel.getValueAt(i, 4),
                    tableModel.getValueAt(i, 5),
                    tableModel.getValueAt(i, 6),
                    tableModel.getValueAt(i, 7),
                    tableModel.getValueAt(i, 8)
            });
        }
        return rows;
    }
}
