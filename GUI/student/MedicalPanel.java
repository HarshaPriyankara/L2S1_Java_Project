package GUI.student;

import DAO.MedicalRecordDAO;
import Models.MedicalRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MedicalPanel extends JPanel {
    private final String studentId;
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final DefaultTableModel tableModel;
    private final JTextArea txtReason;
    private final JComboBox<String> cmbSessionType;
    private final JTextField txtExamCourse;
    private final JSpinner dateSpinner;

    public MedicalPanel(String studentId) {
        this.studentId = studentId;

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Submit New Medical"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Registration No:"), gbc);

        gbc.gridx = 1;
        JTextField txtStudentId = new JTextField(studentId, 18);
        txtStudentId.setEditable(false);
        formPanel.add(txtStudentId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Medical Date:"), gbc);

        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        formPanel.add(dateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Session Type:"), gbc);

        gbc.gridx = 1;
        cmbSessionType = new JComboBox<>(new String[]{"NormalDay", "Exam"});
        formPanel.add(cmbSessionType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Exam Course:"), gbc);

        gbc.gridx = 1;
        txtExamCourse = new JTextField(18);
        formPanel.add(txtExamCourse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Reason:"), gbc);

        gbc.gridx = 1;
        txtReason = new JTextArea(4, 18);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtReason), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton btnSubmit = new JButton("Submit Medical");
        btnSubmit.setBackground(new Color(46, 125, 192));
        btnSubmit.setForeground(Color.WHITE);

        JButton btnRefresh = new JButton("Refresh My Records");
        actionPanel.add(btnRefresh);
        actionPanel.add(btnSubmit);
        formPanel.add(actionPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Medical ID", "Date", "Type", "Exam Course", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setBackground(new Color(46, 125, 192));
        table.getTableHeader().setForeground(Color.WHITE);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cmbSessionType.addActionListener(e -> updateExamCourseState());
        btnSubmit.addActionListener(e -> submitMedical());
        btnRefresh.addActionListener(e -> loadMedicalRecords());

        updateExamCourseState();
        loadMedicalRecords();
    }

    private void updateExamCourseState() {
        boolean isExam = "Exam".equals(cmbSessionType.getSelectedItem());
        txtExamCourse.setEnabled(isExam);
        if (!isExam) {
            txtExamCourse.setText("");
        }
    }

    private void submitMedical() {
        String reason = txtReason.getText().trim();
        String sessionType = (String) cmbSessionType.getSelectedItem();
        String examCourse = txtExamCourse.getText().trim();
        LocalDate sessionDate = ((Date) dateSpinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (reason.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter the medical reason.");
            return;
        }

        if ("Exam".equals(sessionType) && examCourse.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter the exam course code for exam medicals.");
            return;
        }

        try {
            medicalRecordDAO.addMedical(studentId, sessionDate, sessionType, examCourse, reason);
            txtReason.setText("");
            if (!"Exam".equals(sessionType)) {
                txtExamCourse.setText("");
            }
            loadMedicalRecords();
            JOptionPane.showMessageDialog(this, "Medical submitted successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to submit medical: " + ex.getMessage());
        }
    }

    private void loadMedicalRecords() {
        tableModel.setRowCount(0);

        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByStudent(studentId);
            for (MedicalRecord record : records) {
                tableModel.addRow(new Object[]{
                        record.getMedicalId(),
                        record.getSessionDate(),
                        record.getSessionType(),
                        record.getExamCourse(),
                        record.getReason(),
                        record.isApproved() ? "Approved" : "Pending"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load medical records: " + ex.getMessage());
        }
    }
}
