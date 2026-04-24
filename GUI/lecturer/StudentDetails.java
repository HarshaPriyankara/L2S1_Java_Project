package GUI.lecturer;

import DAO.AttendanceDAO;
import DAO.LecturerStudentDAO;
import DAO.MarkDAO;
import DAO.MedicalRecordDAO;
import Models.MedicalRecord;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class StudentDetails extends JPanel {
    private final String lecturerId;
    private final LecturerStudentDAO lecturerStudentDAO = new LecturerStudentDAO();
    private final MarkDAO markDAO = new MarkDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JComboBox<String> studentComboBox = new JComboBox<>();
    private final JPanel profilePanel = new JPanel();
    private final JLabel lblEligibility = new JLabel("Eligibility: -");
    private final JLabel lblGpa = new JLabel("Overall GPA: -");
    private final DefaultTableModel marksModel;
    private final DefaultTableModel attendanceModel;
    private final DefaultTableModel medicalModel;

    public StudentDetails(String lecturerId) {
        this.lecturerId = lecturerId;

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JButton btnLoad = new JButton("Load Student Records");
        btnLoad.setBackground(new Color(46, 125, 192));
        btnLoad.setForeground(Color.WHITE);

        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Student:"));
        topPanel.add(studentComboBox);
        topPanel.add(btnLoad);

        add(topPanel, BorderLayout.NORTH);

        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(Color.WHITE);

        marksModel = createModel(new String[]{"Course", "Course Name", "CA", "END", "Total", "Grade", "GPA"});
        attendanceModel = createModel(new String[]{"Date", "Type", "Hours", "Status"});
        medicalModel = createModel(new String[]{"Medical ID", "Date", "Type", "Reason", "Status"});

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profile", new JScrollPane(profilePanel));
        tabs.addTab("Marks / Grades / GPA", new JScrollPane(new JTable(marksModel)));
        tabs.addTab("Attendance", new JScrollPane(new JTable(attendanceModel)));
        tabs.addTab("Medical", new JScrollPane(new JTable(medicalModel)));
        add(tabs, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(lblEligibility);
        infoPanel.add(lblGpa);
        add(infoPanel, BorderLayout.SOUTH);

        courseComboBox.addActionListener(e -> loadStudentsForSelectedCourse());
        btnLoad.addActionListener(e -> loadSelectedStudentData());

        loadLecturerCourses();
    }

    private DefaultTableModel createModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadLecturerCourses() {
        try {
            courseComboBox.removeAllItems();
            for (String course : lecturerStudentDAO.getLecturerCourses(lecturerId)) {
                courseComboBox.addItem(course);
            }
            loadStudentsForSelectedCourse();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load lecturer courses: " + ex.getMessage());
        }
    }

    private void loadStudentsForSelectedCourse() {
        studentComboBox.removeAllItems();
        String course = (String) courseComboBox.getSelectedItem();
        if (course == null) {
            return;
        }

        try {
            for (String student : lecturerStudentDAO.getStudentsByCourse(course)) {
                studentComboBox.addItem(student);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load enrolled students: " + ex.getMessage());
        }
    }

    private void loadSelectedStudentData() {
        String studentId = (String) studentComboBox.getSelectedItem();
        String courseCode = (String) courseComboBox.getSelectedItem();
        if (studentId == null || courseCode == null) {
            JOptionPane.showMessageDialog(this, "Please select a course and a student.");
            return;
        }

        loadProfile(studentId);
        loadMarks(studentId, courseCode);
        loadAttendance(studentId, courseCode);
        loadMedical(studentId);
    }

    private void loadProfile(String studentId) {
        profilePanel.removeAll();
        try {
            Map<String, String> details = lecturerStudentDAO.getStudentProfile(studentId);
            for (Map.Entry<String, String> entry : details.entrySet()) {
                JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
                label.setFont(new Font("SansSerif", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                profilePanel.add(label);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load student profile: " + ex.getMessage());
        }
        profilePanel.revalidate();
        profilePanel.repaint();
    }

    private void loadMarks(String studentId, String courseCode) {
        marksModel.setRowCount(0);
        try {
            List<MarksCalculator.MarkBreakdown> breakdowns = markDAO.getStudentMarkBreakdowns(studentId);
            for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
                marksModel.addRow(new Object[]{
                        breakdown.getCourseCode(),
                        breakdown.getCourseName(),
                        breakdown.getCaMarks(),
                        breakdown.getEndMarks(),
                        breakdown.getTotalMarks(),
                        breakdown.getGrade(),
                        breakdown.getGpa()
                });
            }

            MarksCalculator.MarkBreakdown selectedCourse = markDAO.getStudentCourseBreakdown(studentId, courseCode);
            if (selectedCourse == null) {
                lblEligibility.setText("Eligibility: No marks yet for selected course");
            } else {
                boolean eligible = selectedCourse.getCaMarks() >= 15.0;
                lblEligibility.setText("Eligibility: " + (eligible ? "Eligible for end assessment" : "Not eligible for end assessment"));
            }

            double sgpa = MarksCalculator.calculateSGPA(breakdowns);
            lblGpa.setText(String.format("Overall GPA: %.2f", sgpa));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load marks: " + ex.getMessage());
        }
    }

    private void loadAttendance(String studentId, String courseCode) {
        attendanceModel.setRowCount(0);
        try {
            ResultSet rs = attendanceDAO.getStudentAttendance(studentId, courseCode);
            while (rs.next()) {
                attendanceModel.addRow(new Object[]{
                        rs.getString("Session_date"),
                        rs.getString("Session_type"),
                        rs.getString("Session_hours"),
                        rs.getString("Status")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load attendance: " + ex.getMessage());
        }
    }

    private void loadMedical(String studentId) {
        medicalModel.setRowCount(0);
        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByStudent(studentId);
            for (MedicalRecord record : records) {
                medicalModel.addRow(new Object[]{
                        record.getMedicalId(),
                        record.getSessionDate(),
                        record.getSessionType(),
                        record.getReason(),
                        record.isApproved() ? "Approved" : "Pending"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load medical records: " + ex.getMessage());
        }
    }
}
