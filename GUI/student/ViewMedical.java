package GUI.student;

import DAO.MedicalRecordDAO;
import Models.MedicalRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewMedical extends JFrame {

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

        try {
            List<MedicalRecord> records = studentId == null || studentId.isBlank()
                    ? new MedicalRecordDAO().getAllMedicalRecords()
                    : new MedicalRecordDAO().getMedicalRecordsByStudent(studentId);

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewMedical::new);
    }
}
