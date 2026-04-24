package GUI.student;

import Controllers.AttendanceControllers.AttendanceController;
import DAO.AttendanceDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class AttendancePanel extends JPanel {
    private AttendanceController controller = new AttendanceController();
    private AttendanceDAO dao = new AttendanceDAO();
    private JComboBox<String> courseComboBox;
    private JTable attendanceTable;
    private DefaultTableModel model;
    private JLabel lblPercentage;
    private String studentID;

    public AttendancePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Selection Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        courseComboBox = new JComboBox<>();
        // ශිෂ්‍යයාගේ විෂයන් පමණක් load කරයි
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

        // --- Table Panel ---
        String[] columns = {"Date", "Type", "Hours", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ශිෂ්‍යයාට edit කළ නොහැක
            }
        };
        attendanceTable = new JTable(model);
        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        // --- Bottom Info Panel (Attendance Percentage) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        lblPercentage = new JLabel("Attendance Percentage: 0%");
        lblPercentage.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(lblPercentage);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listener
        btnView.addActionListener(e -> loadAttendanceData());
    }

    private void loadAttendanceData() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;

        model.setRowCount(0);
        int totalSessions = 0;
        int presentSessions = 0;

        try {
            ResultSet rs = dao.getStudentAttendance(studentID, selectedCourse);
            while (rs.next()) {
                totalSessions++;
                String status = rs.getString("Status");
                if (status.equalsIgnoreCase("Present") || status.equalsIgnoreCase("Medical")) {
                    presentSessions++;
                }

                model.addRow(new Object[]{
                        rs.getString("Session_date"),
                        rs.getString("Session_type"),
                        rs.getString("Session_hours"),
                        status
                });
            }

            // ප්‍රතිශතය ගණනය කිරීම
            if (totalSessions > 0) {
                double percentage = (double) presentSessions / totalSessions * 100;
                lblPercentage.setText(String.format("Attendance Percentage: %.2f%%", percentage));

                // 80% ට වඩා අඩු නම් රතු පැහැයෙන් පෙන්වීම
                if (percentage < 80) {
                    lblPercentage.setForeground(Color.RED);
                } else {
                    lblPercentage.setForeground(new Color(40, 167, 69));
                }
            } else {
                lblPercentage.setText("Attendance Percentage: N/A");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}