package GUI.student;

import Controllers.MedicalControllers.MedicalViewController;
import Controllers.MedicalControllers.StudentMedicalController;
import Models.MedicalRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewMedical extends JFrame {
    private final MedicalViewController medicalViewController = new MedicalViewController();

    public ViewMedical() {
        this("");
    }

    public ViewMedical(String studentId) {
        setTitle("View Medicals");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Medical Id", "Registration No", "Medical Date", "Session Type", "Exam Course", "Reason", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(Color.GREEN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        add(new JScrollPane(table));

        StudentMedicalController.MedicalRecordsResult result = medicalViewController.loadMedicalRecords(studentId);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Medical Records", JOptionPane.INFORMATION_MESSAGE);
        } else {
            List<MedicalRecord> records = result.getRecords();
            for (MedicalRecord record : records) {
                model.addRow(new Object[]{
                        record.getMedicalId(),
                        record.getRegNo(),
                        record.getSessionDate(),
                        record.getSessionType(),
                        record.getExamCourse(),
                        record.getReason(),
                        record.isApproved() ? "Approved" : "Pending"
                });
            }
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewMedical::new);
    }
}
