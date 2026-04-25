package GUI.to;

import Controllers.MedicalControllers.MedicalManagementController;
import Controllers.MedicalControllers.MedicalManagementFormData;
import Controllers.MedicalControllers.StudentMedicalController;
import Models.MedicalRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MedicalManagement extends JPanel {
    private final MedicalManagementController medicalController = new MedicalManagementController();
    private final DefaultTableModel tableModel;
    private final JTable medicalTable;
    private final JTextField txtMedicalId;
    private final JTextField txtRegNo;
    private final JTextField txtSessionDate;
    private final JComboBox<String> cmbSessionType;
    private final JTextField txtExamCourse;
    private final JTextArea txtReason;
    private final JCheckBox chkApproved;

    public MedicalManagement(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Medical Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Medical ID", "Reg No", "Session Date", "Type", "Exam Course", "Reason", "Approved"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        medicalTable = new JTable(tableModel);
        medicalTable.setRowHeight(28);
        medicalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicalTable.getTableHeader().setBackground(new Color(46, 125, 192));
        medicalTable.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(medicalTable), BorderLayout.CENTER);

        JPanel editorPanel = new JPanel(new GridBagLayout());
        editorPanel.setBackground(Color.WHITE);
        editorPanel.setBorder(BorderFactory.createTitledBorder("Selected Medical"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        editorPanel.add(new JLabel("Medical ID:"), gbc);
        gbc.gridx = 1;
        txtMedicalId = new JTextField(12);
        txtMedicalId.setEditable(false);
        editorPanel.add(txtMedicalId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editorPanel.add(new JLabel("Registration No:"), gbc);
        gbc.gridx = 1;
        txtRegNo = new JTextField(12);
        editorPanel.add(txtRegNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editorPanel.add(new JLabel("Session Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        txtSessionDate = new JTextField(LocalDate.now().toString(), 12);
        editorPanel.add(txtSessionDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editorPanel.add(new JLabel("Session Type:"), gbc);
        gbc.gridx = 1;
        cmbSessionType = new JComboBox<>(new String[]{"NormalDay", "Exam"});
        editorPanel.add(cmbSessionType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editorPanel.add(new JLabel("Exam Course:"), gbc);
        gbc.gridx = 1;
        txtExamCourse = new JTextField(12);
        editorPanel.add(txtExamCourse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        editorPanel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        txtReason = new JTextArea(4, 18);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        editorPanel.add(new JScrollPane(txtReason), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        editorPanel.add(new JLabel("Approved:"), gbc);
        gbc.gridx = 1;
        chkApproved = new JCheckBox("Approve this medical");
        chkApproved.setBackground(Color.WHITE);
        editorPanel.add(chkApproved, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClear = new JButton("Clear");
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnApprove = new JButton("Approve");
        JButton btnDelete = new JButton("Delete");

        for (JButton button : new JButton[]{btnAdd, btnUpdate, btnApprove, btnDelete}) {
            button.setBackground(new Color(46, 125, 192));
            button.setForeground(Color.WHITE);
        }

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnApprove);
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
        btnRefresh.addActionListener(e -> loadRecords());
        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addMedical());
        btnUpdate.addActionListener(e -> updateMedical());
        btnApprove.addActionListener(e -> approveMedical());
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
                    record.isApproved() ? "Yes" : "No"
            });
        }
    }

    private void populateFromSelectedRow() {
        int row = medicalTable.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtMedicalId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtRegNo.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtSessionDate.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        cmbSessionType.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 3)));
        txtExamCourse.setText(valueOrEmpty(tableModel.getValueAt(row, 4)));
        txtReason.setText(valueOrEmpty(tableModel.getValueAt(row, 5)));
        chkApproved.setSelected("Yes".equalsIgnoreCase(String.valueOf(tableModel.getValueAt(row, 6))));
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
        StudentMedicalController.MedicalActionResult result = medicalController.addMedical(buildFormData());
        if (result.isSuccess()) {
            loadRecords();
            clearForm();
        }
        showMedicalActionResult(result);
    }

    private void updateMedical() {
        StudentMedicalController.MedicalActionResult result = medicalController.updateMedical(buildFormData());
        if (result.isSuccess()) {
            loadRecords();
        }
        showMedicalActionResult(result);
    }

    private void approveMedical() {
        StudentMedicalController.MedicalActionResult result = medicalController.approveMedical(buildApprovedFormData());
        if (result.isSuccess()) {
            loadRecords();
        }
        showMedicalActionResult(result);
    }

    private void deleteMedical() {
        if (txtMedicalId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Please select a medical record to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected medical record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        StudentMedicalController.MedicalActionResult result = medicalController.deleteMedical(txtMedicalId.getText());
        if (result.isSuccess()) {
            loadRecords();
            clearForm();
        }
        showMedicalActionResult(result);
    }

    private void clearForm() {
        txtMedicalId.setText("");
        txtRegNo.setText("");
        txtSessionDate.setText(LocalDate.now().toString());
        cmbSessionType.setSelectedItem("NormalDay");
        txtExamCourse.setText("");
        txtReason.setText("");
        chkApproved.setSelected(false);
        medicalTable.clearSelection();
        updateExamCourseState();
    }

    private String valueOrEmpty(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private MedicalManagementFormData buildFormData() {
        return new MedicalManagementFormData(
                txtMedicalId.getText(),
                txtRegNo.getText(),
                txtSessionDate.getText(),
                String.valueOf(cmbSessionType.getSelectedItem()),
                txtExamCourse.getText(),
                txtReason.getText(),
                chkApproved.isSelected()
        );
    }

    private MedicalManagementFormData buildApprovedFormData() {
        return new MedicalManagementFormData(
                txtMedicalId.getText(),
                txtRegNo.getText(),
                txtSessionDate.getText(),
                String.valueOf(cmbSessionType.getSelectedItem()),
                txtExamCourse.getText(),
                txtReason.getText(),
                true
        );
    }

    private void showMedicalActionResult(StudentMedicalController.MedicalActionResult result) {
        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                result.isSuccess() ? "Success" : "Error",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }
}
