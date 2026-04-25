package GUI.to;

import Controllers.AttendanceControllers.AttendanceController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UpdateDeleteAttendancePanel extends JPanel {
    private final AttendanceController controller = new AttendanceController();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtStudentId = new JTextField(10);
    private JComboBox<String> courseBox = new JComboBox<>();

    public UpdateDeleteAttendancePanel(AttendanceManagement parent) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> parent.showPanel("Menu"));

        for (String c : controller.getCourseList()) {
            courseBox.addItem(c);
        }
        JButton btnFilter = new JButton("Filter Student");
        JButton btnAll = new JButton("Show All");

        filterBar.add(btnBack);
        filterBar.add(new JLabel("Course:"));
        filterBar.add(courseBox);
        filterBar.add(new JLabel("Student ID:"));
        filterBar.add(txtStudentId);
        filterBar.add(btnFilter);
        filterBar.add(btnAll);

        model = new DefaultTableModel(new String[]{"ID", "Student", "Course", "Date", "Status", "Hours"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };
        table = new JTable(model);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Medical"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnUpdate = new JButton("Update Selected Record");
        JButton btnDelete = new JButton("Delete Selected Record");

        btnUpdate.setBackground(new Color(40, 167, 69));
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);

        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> loadFilteredData(true));
        btnAll.addActionListener(e -> loadFilteredData(false));

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String status = model.getValueAt(row, 4).toString();
            String hours = model.getValueAt(row, 5).toString();

            AttendanceController.AttendanceActionResult result = controller.updateAttendanceRecord(id, status, hours);
            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage(),
                    result.isSuccess() ? "Success" : "Error",
                    result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            AttendanceController.AttendanceActionResult result = controller.deleteAttendanceRecord((int) model.getValueAt(row, 0));
            if (result.isSuccess()) {
                model.removeRow(row);
            }
            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage(),
                    result.isSuccess() ? "Success" : "Error",
                    result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
        });

        add(filterBar, BorderLayout.NORTH);
    }

    private void loadFilteredData(boolean isFilter) {
        String student = isFilter ? txtStudentId.getText().trim() : "";
        AttendanceController.AttendanceRecordsResult result =
                controller.loadAttendanceRecords(String.valueOf(courseBox.getSelectedItem()), student);

        model.setRowCount(0);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        for (Object[] row : result.getRows()) {
            model.addRow(row);
        }
    }
}
