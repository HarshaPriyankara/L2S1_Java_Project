package GUI.lecturer;

import Controllers.StudentControllers.LecturerGpaController;
import Controllers.StudentControllers.LecturerGpaResult;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGpaPanel extends JPanel {
    private final LecturerGpaController controller = new LecturerGpaController();
    private final JTextField searchField = new JTextField(12);
    private final DefaultTableModel summaryModel;
    private final DefaultTableModel detailsModel;
    private final JLabel selectedStudentLabel = new JLabel("Subject Grades: -");
    private final JLabel gpaSummaryLabel = new JLabel("SGPA: - | CGPA: -");
    private final Runnable onBack;

    public StudentGpaPanel(Runnable onBack) {
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

        UITheme.styleTextField(searchField);

        JButton loadButton = new JButton("Load GPA");
        UITheme.stylePrimaryButton(loadButton);
        UITheme.setWideButtonSize(loadButton);

        JButton showAllButton = new JButton("Show Batch Only");
        UITheme.styleNeutralButton(showAllButton);
        UITheme.setWideButtonSize(showAllButton);

        topPanel.add(backButton);
        topPanel.add(new JLabel("Reg No:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(showAllButton);
        add(topPanel, BorderLayout.NORTH);

        summaryModel = new DefaultTableModel(new String[]{"Reg No", "SGPA", "CGPA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        detailsModel = new DefaultTableModel(
                new String[]{"Course Code", "Course Name", "Credits", "Final Marks", "Grade", "Grade Point", "Weighted Point"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable summaryTable = new JTable(summaryModel);
        summaryTable.setRowHeight(28);
        UITheme.styleTable(summaryTable);

        JTable detailsTable = new JTable(detailsModel);
        detailsTable.setRowHeight(28);
        UITheme.styleTable(detailsTable);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        centerPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel summaryPanel = new JPanel(new BorderLayout(0, 8));
        summaryPanel.setBackground(UITheme.APP_BACKGROUND);
        JLabel batchLabel = new JLabel("Whole Batch SGPA / CGPA");
        batchLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        batchLabel.setForeground(UITheme.TEXT_PRIMARY);
        summaryPanel.add(batchLabel, BorderLayout.NORTH);
        summaryPanel.add(new JScrollPane(summaryTable), BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new BorderLayout(0, 8));
        detailsPanel.setBackground(UITheme.APP_BACKGROUND);
        selectedStudentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        selectedStudentLabel.setForeground(UITheme.TEXT_PRIMARY);
        gpaSummaryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gpaSummaryLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel detailsHeader = new JPanel();
        detailsHeader.setBackground(UITheme.APP_BACKGROUND);
        detailsHeader.setLayout(new BoxLayout(detailsHeader, BoxLayout.Y_AXIS));
        detailsHeader.add(selectedStudentLabel);
        detailsHeader.add(Box.createVerticalStrut(4));
        detailsHeader.add(gpaSummaryLabel);

        detailsPanel.add(detailsHeader, BorderLayout.NORTH);
        detailsPanel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);

        centerPanel.add(summaryPanel);
        centerPanel.add(detailsPanel);
        add(centerPanel, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadGpaData());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadGpaData();
        });

        loadGpaData();
    }

    private void loadGpaData() {
        summaryModel.setRowCount(0);
        detailsModel.setRowCount(0);
        selectedStudentLabel.setText("Subject Grades: -");
        gpaSummaryLabel.setText("SGPA: - | CGPA: -");

        String studentId = searchField.getText().trim();
        LecturerGpaResult result = controller.loadGpaData(studentId);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "SGPA and CGPA", JOptionPane.INFORMATION_MESSAGE);
            if (result.getSummaryRows() == null) {
                return;
            }
        }

        for (Object[] row : result.getSummaryRows()) {
            summaryModel.addRow(row);
        }

        if (result.getSelectedStudentId() != null && !result.getSelectedStudentId().isBlank()) {
            selectedStudentLabel.setText("Subject Grades: " + result.getSelectedStudentId());
            if (result.getSelectedStudentSummary() != null) {
                gpaSummaryLabel.setText(result.getSelectedStudentSummary());
            }
        }

        for (Object[] row : result.getDetailRows()) {
            detailsModel.addRow(row);
        }
    }
}
