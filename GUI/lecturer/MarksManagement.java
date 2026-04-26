package GUI.lecturer;

import Controllers.MarksControllers.MarksLoadResult;
import Controllers.MarksControllers.MarksManagementController;
import Controllers.MarksControllers.MarksSaveResult;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MarksManagement extends JPanel {
    private JComboBox<String> courseComboBox;
    private JComboBox<String> typeComboBox;
    private JLabel statusLabel;
    private JTable marksTable;
    private DefaultTableModel tableModel;
    private String lecturerID;
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

        String[] columns = {"Student ID", "Course Code", "Assessment Type", "Mark Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        marksTable = new JTable(tableModel);
        marksTable.setRowHeight(28);
        marksTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        marksTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        add(new JScrollPane(marksTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(80, 80, 80));

        JButton btnSave = new JButton("Upload / Update Marks");
        styleButton(btnSave, new Color(40, 167, 69));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        savePanel.setBackground(Color.WHITE);
        savePanel.add(btnSave);

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(savePanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadEnrolledStudents());
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

        MarksLoadResult result = marksController.loadEnrolledStudents(selectedCourse, selectedType);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
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

        MarksSaveResult result = marksController.saveAllMarks(extractTableRows());
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

    private void stopTableEditing() {
        if (marksTable.isEditing()) {
            marksTable.getCellEditor().stopCellEditing();
        }
    }

    private List<Object[]> extractTableRows() {
        List<Object[]> rows = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rows.add(new Object[]{
                    tableModel.getValueAt(i, 0),
                    tableModel.getValueAt(i, 1),
                    tableModel.getValueAt(i, 2),
                    tableModel.getValueAt(i, 3)
            });
        }
        return rows;
    }
}
