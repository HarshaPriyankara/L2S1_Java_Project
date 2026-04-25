package GUI.to;

import Controllers.MedicalControllers.MedicalManagementController;
import Controllers.MedicalControllers.MedicalManagementFormData;
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
import java.util.List;

public class MedicalManagement extends JPanel {
    private final MedicalManagementController medicalController = new MedicalManagementController();
    private final DefaultTableModel tableModel;
    private final JTable medicalTable;
    private final JTextField txtRegNo;
    private final JTextField txtSessionDate;
    private final JComboBox<String> cmbSessionType;
    private final JTextField txtExamCourse;
    private final JTextField txtMedicalFile;
    private final JTextArea txtReason;
    private final JCheckBox chkApproved;
    private final FileStorageSupport fileStorageSupport = new FileStorageSupport() {
        @Override
        protected File uploadDirectory() {
            return new File("uploads/medical_records");
        }
    };
    private File selectedMedicalFile;
    private String selectedMedicalId = "";

    public MedicalManagement(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JLabel title = UITheme.createSectionTitle("Medical Management");
        add(title, BorderLayout.NORTH);

        String[] columns = {"Medical ID", "Reg No", "Session Date", "Type", "Exam Course", "Reason", "Medical File", "Approved", "Open"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        medicalTable = new JTable(tableModel);
        medicalTable.setRowHeight(28);
        medicalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UITheme.styleTable(medicalTable);
        medicalTable.getColumnModel().getColumn(8).setCellRenderer(new OpenButtonRenderer());
        medicalTable.getColumnModel().getColumn(8).setCellEditor(new OpenButtonEditor(medicalTable));

        JScrollPane tableScrollPane = new JScrollPane(medicalTable);
        tableScrollPane.setBorder(UITheme.createSectionBorder("Medical Records"));
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel editorPanel = new JPanel(new GridBagLayout());
        editorPanel.setBackground(UITheme.SURFACE);
        editorPanel.setBorder(UITheme.createSectionBorder("Selected Medical"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        editorPanel.add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1;
        txtRegNo = new JTextField(12);
        UITheme.styleTextField(txtRegNo);
        editorPanel.add(txtRegNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editorPanel.add(new JLabel("Session Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        txtSessionDate = new JTextField(LocalDate.now().toString(), 12);
        UITheme.styleTextField(txtSessionDate);
        editorPanel.add(txtSessionDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editorPanel.add(new JLabel("Session Type:"), gbc);
        gbc.gridx = 1;
        cmbSessionType = new JComboBox<>(new String[]{"NormalDay", "Exam"});
        UITheme.styleComboBox(cmbSessionType);
        editorPanel.add(cmbSessionType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editorPanel.add(new JLabel("Exam Course:"), gbc);
        gbc.gridx = 1;
        txtExamCourse = new JTextField(12);
        UITheme.styleTextField(txtExamCourse);
        editorPanel.add(txtExamCourse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editorPanel.add(new JLabel("Medical File:"), gbc);
        gbc.gridx = 1;
        JPanel filePanel = new JPanel(new BorderLayout(8, 0));
        filePanel.setBackground(UITheme.SURFACE);
        txtMedicalFile = new JTextField(12);
        txtMedicalFile.setEditable(false);
        txtMedicalFile.setBackground(UITheme.SURFACE_MUTED);
        JButton btnBrowse = new JButton("Browse");
        UITheme.styleNeutralButton(btnBrowse);
        filePanel.add(txtMedicalFile, BorderLayout.CENTER);
        filePanel.add(btnBrowse, BorderLayout.EAST);
        editorPanel.add(filePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        editorPanel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        txtReason = new JTextArea(4, 18);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        UITheme.styleTextArea(txtReason);
        editorPanel.add(new JScrollPane(txtReason), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        editorPanel.add(new JLabel("Approved:"), gbc);
        gbc.gridx = 1;
        chkApproved = new JCheckBox("Approve this medical");
        chkApproved.setBackground(UITheme.SURFACE);
        editorPanel.add(chkApproved, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UITheme.SURFACE);
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClear = new JButton("Clear");
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        UITheme.styleNeutralButton(btnRefresh);
        UITheme.styleNeutralButton(btnClear);
        UITheme.stylePrimaryButton(btnAdd);
        UITheme.stylePrimaryButton(btnUpdate);
        UITheme.styleDangerButton(btnDelete);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        editorPanel.add(buttonPanel, gbc);

        add(editorPanel, BorderLayout.SOUTH);

        medicalTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFromSelectedRow();
            }
        });
        cmbSessionType.addActionListener(e -> updateExamCourseState());
        btnBrowse.addActionListener(e -> browseMedicalFile());
        btnRefresh.addActionListener(e -> loadRecords());
        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addMedical());
        btnUpdate.addActionListener(e -> updateMedical());
        btnDelete.addActionListener(e -> deleteMedical());

        updateExamCourseState();
        loadRecords();
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        StudentMedicalController.MedicalRecordsResult result = medicalController.loadAllMedicalRecords();
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        List<MedicalRecord> records = result.getRecords();
        for (MedicalRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getMedicalId(),
                    record.getRegNo(),
                    record.getSessionDate(),
                    record.getSessionType(),
                    record.getExamCourse(),
                    record.getReason(),
                    record.getMedicalFilePath() == null ? "" : record.getMedicalFilePath(),
                    record.isApproved() ? "Yes" : "No",
                    record.getMedicalFilePath() == null || record.getMedicalFilePath().isBlank() ? "" : "Open"
            });
        }
    }

    private void populateFromSelectedRow() {
        int selectedRow = medicalTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int row = medicalTable.convertRowIndexToModel(selectedRow);

        selectedMedicalId = String.valueOf(tableModel.getValueAt(row, 0));
        txtRegNo.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtSessionDate.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        cmbSessionType.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 3)));
        txtExamCourse.setText(valueOrEmpty(tableModel.getValueAt(row, 4)));
        txtReason.setText(valueOrEmpty(tableModel.getValueAt(row, 5)));
        txtMedicalFile.setText(valueOrEmpty(tableModel.getValueAt(row, 6)));
        selectedMedicalFile = null;
        chkApproved.setSelected("Yes".equalsIgnoreCase(String.valueOf(tableModel.getValueAt(row, 7))));
        updateExamCourseState();
        if ("Exam".equals(cmbSessionType.getSelectedItem())) {
            txtExamCourse.setText(valueOrEmpty(tableModel.getValueAt(row, 4)));
        }
    }

    private void updateExamCourseState() {
        boolean isExam = "Exam".equals(cmbSessionType.getSelectedItem());
        txtExamCourse.setEnabled(isExam);
        if (!isExam) {
            txtExamCourse.setText("");
        }
    }

    private void addMedical() {
        String uploadedFilePath = saveSelectedMedicalFile();
        if (selectedMedicalFile != null && uploadedFilePath == null) {
            JOptionPane.showMessageDialog(this, "Medical file upload failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MedicalManagementFormData formData = buildFormData(uploadedFilePath);
        StudentMedicalController.MedicalActionResult result = medicalController.addMedical(formData);
        if (result.isSuccess()) {
            loadRecords();
            clearForm();
        }
        showMedicalActionResult(result);
    }

    private void updateMedical() {
        StudentMedicalController.MedicalActionResult result = medicalController.updateMedical(buildFormData(txtMedicalFile.getText()));
        if (result.isSuccess()) {
            loadRecords();
        }
        showMedicalActionResult(result);
    }

    private void deleteMedical() {
        if (selectedMedicalId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please select a medical record to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected medical record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        StudentMedicalController.MedicalActionResult result = medicalController.deleteMedical(selectedMedicalId);
        if (result.isSuccess()) {
            loadRecords();
            clearForm();
        }
        showMedicalActionResult(result);
    }

    private void clearForm() {
        selectedMedicalId = "";
        txtRegNo.setText("");
        txtSessionDate.setText(LocalDate.now().toString());
        cmbSessionType.setSelectedItem("NormalDay");
        txtExamCourse.setText("");
        txtMedicalFile.setText("");
        selectedMedicalFile = null;
        txtReason.setText("");
        chkApproved.setSelected(false);
        medicalTable.clearSelection();
        updateExamCourseState();
    }

    private String valueOrEmpty(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private MedicalManagementFormData buildFormData(String medicalFilePath) {
        return new MedicalManagementFormData(
                selectedMedicalId,
                txtRegNo.getText(),
                txtSessionDate.getText(),
                String.valueOf(cmbSessionType.getSelectedItem()),
                txtExamCourse.getText(),
                txtReason.getText(),
                medicalFilePath,
                chkApproved.isSelected()
        );
    }

    private void browseMedicalFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedMedicalFile = fileChooser.getSelectedFile();
            txtMedicalFile.setText(selectedMedicalFile.getAbsolutePath().replace("\\", "/"));
        }
    }

    private String saveSelectedMedicalFile() {
        if (selectedMedicalFile == null) {
            return null;
        }

        LocalDate sessionDate;
        try {
            sessionDate = LocalDate.parse(txtSessionDate.getText().trim());
        } catch (Exception ex) {
            return null;
        }

        return fileStorageSupport.saveFile(selectedMedicalFile, txtRegNo.getText().trim(), sessionDate);
    }

    private void showMedicalActionResult(StudentMedicalController.MedicalActionResult result) {
        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                result.isSuccess() ? "Success" : "Error",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
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
                String filePath = String.valueOf(table.getModel().getValueAt(modelRow, 6));
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
