package GUI.student;

import Controllers.MedicalControllers.StudentMedicalController;
import GUI.common.UITheme;
import Models.MedicalRecord;
import Utils.FileStorageSupport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicalPanel extends JPanel {
    private final String studentId;
    private final StudentMedicalController medicalController = new StudentMedicalController();
    private final FileStorageSupport fileStorageSupport = new FileStorageSupport() {
        @Override
        protected File uploadDirectory() {
            return new File("uploads/medical_records");
        }
    };

    private DefaultTableModel tableModel;
    private JTextArea txtReason;
    private JComboBox<String> cmbSessionType;
    private JTextField txtExamCourse;
    private JTextField txtMedicalFile;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private DefaultTableModel absentTableModel;
    private final List<Integer> loadedAbsentAttendanceIds = new ArrayList<>();
    private File selectedMedicalFile;

    public MedicalPanel(String studentId) {
        this.studentId = studentId;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        add(UITheme.createSectionTitle("Submit New Medical"), BorderLayout.NORTH);
        add(buildContentPanel(), BorderLayout.CENTER);

        updateExamCourseState();
        loadMedicalRecords();
    }

    private JPanel buildContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(UITheme.APP_BACKGROUND);
        contentPanel.add(buildFormPanel(), BorderLayout.NORTH);
        contentPanel.add(buildTablePanel(), BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.SURFACE);
        formPanel.setBorder(UITheme.createSectionBorder("Medical Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabel(formPanel, "Registration No:", gbc, 0);
        JTextField txtStudentId = new JTextField(studentId, 18);
        txtStudentId.setEditable(false);
        txtStudentId.setBackground(UITheme.SURFACE_MUTED);
        addField(formPanel, txtStudentId, gbc, 0);

        addLabel(formPanel, "Start Date:", gbc, 1);
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        addField(formPanel, startDateSpinner, gbc, 1);

        addLabel(formPanel, "End Date:", gbc, 2);
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        addField(formPanel, endDateSpinner, gbc, 2);

        JButton btnLoadAbsent = new JButton("Load Absent Attendance");
        UITheme.styleNeutralButton(btnLoadAbsent);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnLoadAbsent, gbc);

        addLabel(formPanel, "Session Type:", gbc, 4);
        cmbSessionType = new JComboBox<>(new String[]{"NormalDay", "Exam"});
        UITheme.styleComboBox(cmbSessionType);
        addField(formPanel, cmbSessionType, gbc, 4);

        addLabel(formPanel, "Exam Course:", gbc, 5);
        txtExamCourse = new JTextField(18);
        UITheme.styleTextField(txtExamCourse);
        addField(formPanel, txtExamCourse, gbc, 5);

        addLabel(formPanel, "Medical File:", gbc, 6);
        JPanel filePanel = new JPanel(new BorderLayout(8, 0));
        filePanel.setBackground(UITheme.SURFACE);
        txtMedicalFile = new JTextField(18);
        txtMedicalFile.setEditable(false);
        txtMedicalFile.setBackground(UITheme.SURFACE_MUTED);
        JButton btnBrowse = new JButton("Browse");
        UITheme.styleNeutralButton(btnBrowse);
        filePanel.add(txtMedicalFile, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);
        addField(formPanel, filePanel, gbc, 6);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Reason:"), gbc);

        gbc.gridx = 1;
        txtReason = new JTextArea(4, 18);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        UITheme.styleTextArea(txtReason);
        formPanel.add(new JScrollPane(txtReason), gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UITheme.SURFACE);

        JButton btnSubmit = new JButton("Submit Medical");
        UITheme.stylePrimaryButton(btnSubmit);

        JButton btnViewRecords = new JButton("View My Medical Records");
        UITheme.styleNeutralButton(btnViewRecords);
        actionPanel.add(btnViewRecords);
        actionPanel.add(btnSubmit);
        formPanel.add(actionPanel, gbc);

        cmbSessionType.addActionListener(e -> updateExamCourseState());
        btnLoadAbsent.addActionListener(e -> loadAbsentAttendance());
        btnBrowse.addActionListener(e -> browseMedicalFile());
        btnSubmit.addActionListener(e -> submitMedical());
        btnViewRecords.addActionListener(e -> showMedicalRecordsWindow());

        return formPanel;
    }

    private JScrollPane buildTablePanel() {
        absentTableModel = new DefaultTableModel(new String[]{"Course Code", "Date", "Type", "Hours", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable absentTable = new JTable(absentTableModel);
        absentTable.setRowHeight(28);
        UITheme.styleTable(absentTable);
        JScrollPane absentScrollPane = new JScrollPane(absentTable);
        absentScrollPane.setBorder(UITheme.createSectionBorder("Absent Attendance In Selected Range"));

        String[] columns = {"Date Range", "Type", "Exam Course", "Reason", "Medical File", "Status", "Open"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        return absentScrollPane;
    }

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(text), gbc);
    }

    private void addField(JPanel panel, Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }

    private void updateExamCourseState() {
        boolean isExam = "Exam".equals(cmbSessionType.getSelectedItem());
        txtExamCourse.setEnabled(isExam);
        if (!isExam) {
            txtExamCourse.setText("");
        }
    }

    private void browseMedicalFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedMedicalFile = fileChooser.getSelectedFile();
            txtMedicalFile.setText(selectedMedicalFile.getAbsolutePath().replace("\\", "/"));
        }
    }

    private void loadAbsentAttendance() {
        LocalDate startDate = dateFromSpinner(startDateSpinner);
        LocalDate endDate = dateFromSpinner(endDateSpinner);

        absentTableModel.setRowCount(0);
        loadedAbsentAttendanceIds.clear();

        StudentMedicalController.AbsentAttendanceResult result =
                medicalController.loadAbsentAttendance(studentId, startDate, endDate);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Object[] row : result.getRows()) {
            loadedAbsentAttendanceIds.add((Integer) row[0]);
            absentTableModel.addRow(new Object[]{row[1], row[2], row[3], row[4], row[5]});
        }

        if (loadedAbsentAttendanceIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No absent attendance records found for this date range.");
        }
    }

    private void submitMedical() {
        String reason = txtReason.getText().trim();
        String sessionType = (String) cmbSessionType.getSelectedItem();
        String examCourse = txtExamCourse.getText().trim();
        LocalDate startDate = dateFromSpinner(startDateSpinner);
        LocalDate endDate = dateFromSpinner(endDateSpinner);

        if (loadedAbsentAttendanceIds.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "No absent attendance records are loaded for this range. Submit medical anyway?",
                    "Confirm Submit",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        String uploadedFilePath = null;
        if (selectedMedicalFile != null) {
            uploadedFilePath = fileStorageSupport.saveFile(selectedMedicalFile, studentId, startDate);
            if (uploadedFilePath == null) {
                JOptionPane.showMessageDialog(this, "Medical file upload failed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        StudentMedicalController.MedicalActionResult result =
                medicalController.submitMedical(studentId, startDate, startDate, endDate, sessionType, examCourse,
                        reason, uploadedFilePath, new ArrayList<>(loadedAbsentAttendanceIds));

        if (result.isSuccess()) {
            clearForm(sessionType);
            loadMedicalRecords();
        }

        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                result.isSuccess() ? "Success" : "Error",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }

    private void clearForm(String sessionType) {
        txtReason.setText("");
        txtMedicalFile.setText("");
        selectedMedicalFile = null;
        absentTableModel.setRowCount(0);
        loadedAbsentAttendanceIds.clear();
        if (!"Exam".equals(sessionType)) {
            txtExamCourse.setText("");
        }
    }

    private void loadMedicalRecords() {
        tableModel.setRowCount(0);

        StudentMedicalController.MedicalRecordsResult result = medicalController.loadMedicalRecords(studentId);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        List<MedicalRecord> records = result.getRecords();
        for (MedicalRecord record : records) {
            String filePath = record.getMedicalFilePath() == null ? "" : record.getMedicalFilePath();
            String dateRange = record.getStartDate() + " to " + record.getEndDate();
            tableModel.addRow(new Object[]{
                    dateRange,
                    record.getSessionType(),
                    record.getExamCourse(),
                    record.getReason(),
                    filePath,
                    record.isApproved() ? "Approved" : "Pending",
                    filePath.isBlank() ? "" : "Open"
            });
        }
    }

    private void showMedicalRecordsWindow() {
        loadMedicalRecords();

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "My Medical Records",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.getContentPane().setBackground(UITheme.APP_BACKGROUND);
        ((JComponent) dialog.getContentPane()).setBorder(UITheme.createContentBorder());

        dialog.add(UITheme.createSectionTitle("My Medical Records"), BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        UITheme.styleTable(table);
        table.getColumnModel().getColumn(6).setCellRenderer(new OpenButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new OpenButtonEditor(table));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(UITheme.createSectionBorder("Medical Records"));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        UITheme.styleNeutralButton(closeButton);
        UITheme.setStandardButtonSize(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(UITheme.APP_BACKGROUND);
        footer.add(closeButton);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setSize(860, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openMedicalFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            JOptionPane.showMessageDialog(this, "No medical file uploaded for this record.");
            return;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "File not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open medical file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate dateFromSpinner(JSpinner spinner) {
        return ((Date) spinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private class OpenButtonRenderer extends JButton implements TableCellRenderer {
        OpenButtonRenderer() {
            UITheme.stylePrimaryButton(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null || value.toString().isBlank() ? "No File" : value.toString());
            setEnabled(value != null && !value.toString().isBlank());
            return this;
        }
    }

    private class OpenButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button = new JButton("Open");
        private final JTable table;
        private int row;

        OpenButtonEditor(JTable table) {
            this.table = table;
            UITheme.stylePrimaryButton(button);
            button.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = table.convertRowIndexToModel(row);
                String filePath = String.valueOf(table.getModel().getValueAt(modelRow, 4));
                openMedicalFile(filePath);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setText(value == null || value.toString().isBlank() ? "No File" : value.toString());
            button.setEnabled(value != null && !value.toString().isBlank());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

}
