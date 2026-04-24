package GUI.to;

import Controllers.AttendanceControllers.AttendanceController;
import DAO.AttendanceDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class UpdateDeleteAttendancePanel extends JPanel {
    private AttendanceController controller = new AttendanceController();
    private AttendanceDAO dao = new AttendanceDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtStudentId = new JTextField(10);
    private JComboBox<String> courseBox = new JComboBox<>();

    public UpdateDeleteAttendancePanel(AttendanceManagement parent) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header Filter Bar ---
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("← Back");
        btnBack.addActionListener(e -> parent.showPanel("Menu"));

        for (String c : controller.getCourseList()) courseBox.addItem(c);
        JButton btnFilter = new JButton("Filter Student");
        JButton btnAll = new JButton("Show All");

        filterBar.add(btnBack);
        filterBar.add(new JLabel("Course:")); filterBar.add(courseBox);
        filterBar.add(new JLabel("Student ID:")); filterBar.add(txtStudentId);
        filterBar.add(btnFilter);
        filterBar.add(btnAll);

        model = new DefaultTableModel(new String[]{"ID", "Student", "Course", "Date", "Status", "Hours"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Status සහ Hours පමණක් edit කළ හැක
            }
        };
        table = new JTable(model);

        // add dropdown for status collum
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Medical"});
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusCombo));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom control panel
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

        // Load data
        btnFilter.addActionListener(e -> loadFilteredData(true));
        btnAll.addActionListener(e -> loadFilteredData(false));

        // UPDATE Logic
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    int id = (int) model.getValueAt(row, 0);
                    String status = model.getValueAt(row, 4).toString();
                    double hours = Double.parseDouble(model.getValueAt(row, 5).toString());

                    // update db via dao
                    dao.updateAttendance(id, status, hours);
                    JOptionPane.showMessageDialog(this, "Attendance record updated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to update.");
            }
        });

        // Delete logic
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        dao.deleteAttendance((int) model.getValueAt(row, 0));
                        model.removeRow(row);
                        JOptionPane.showMessageDialog(this, "Record deleted!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });

        add(filterBar, BorderLayout.NORTH);
    }

    private void loadFilteredData(boolean isFilter) {
        try {
            String student = isFilter ? txtStudentId.getText().trim() : "";
            ResultSet rs = dao.getAttendanceRecords(courseBox.getSelectedItem().toString(), student);
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("Attendance_id"),
                        rs.getString("Reg_no"),
                        rs.getString("Course_code"),
                        rs.getString("Session_date"),
                        rs.getString("Status"),
                        rs.getString("Session_hours")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}