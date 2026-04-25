package GUI.student;

import Controllers.AttendanceControllers.AttendanceController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AttendancePanel extends JPanel {
    private final AttendanceController controller = new AttendanceController();
    private JComboBox<String> courseComboBox;
    private JTable attendanceTable;
    private DefaultTableModel model;
    private JLabel lblPercentage;
    private final String studentID;

    public AttendancePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        courseComboBox = new JComboBox<>();
        for (String course : controller.getStudentCourses(studentID)) {
            courseComboBox.addItem(course);
        }

        JButton btnView = new JButton("View My Attendance");
        btnView.setBackground(new Color(46, 125, 192));
        btnView.setForeground(Color.WHITE);

        topPanel.add(new JLabel("Select Course: "));
        topPanel.add(courseComboBox);
        topPanel.add(btnView);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Date", "Type", "Hours", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(model);
        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        lblPercentage = new JLabel("Attendance Percentage: 0%");
        lblPercentage.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(lblPercentage);
        add(bottomPanel, BorderLayout.SOUTH);

        btnView.addActionListener(e -> loadAttendanceData());
    }

    private void loadAttendanceData() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if (selectedCourse == null) {
            return;
        }

        model.setRowCount(0);
        AttendanceController.StudentAttendanceResult result = controller.loadStudentAttendance(studentID, selectedCourse);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        for (Object[] row : result.getRows()) {
            model.addRow(row);
        }

        int totalSessions = result.getTotalSessions();
        int presentSessions = result.getPresentSessions();
        if (totalSessions > 0) {
            double percentage = (double) presentSessions / totalSessions * 100;
            lblPercentage.setText(String.format("Attendance Percentage: %.2f%%", percentage));
            if (percentage < 80) {
                lblPercentage.setForeground(Color.RED);
            } else {
                lblPercentage.setForeground(new Color(40, 167, 69));
            }
        } else {
            lblPercentage.setText("Attendance Percentage: N/A");
            lblPercentage.setForeground(new Color(80, 80, 80));
        }
    }
}
