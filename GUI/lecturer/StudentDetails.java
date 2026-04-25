package GUI.lecturer;

import Controllers.StudentControllers.StudentDetailsController;
import Controllers.StudentControllers.StudentDetailsResult;
import Models.MedicalRecord;
import Utils.CourseMarkScheme;
import Utils.GpaCalculator;
import Utils.MarksCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StudentDetails extends JPanel {
    private final String lecturerId;
    private final StudentDetailsController studentDetailsController = new StudentDetailsController();

    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JComboBox<String> studentComboBox = new JComboBox<>();
    private final JPanel profilePanel = new JPanel();
    private final JLabel lblEligibility = new JLabel("Eligibility: -");
    private final JLabel lblGpa = new JLabel("SGPA: - | CGPA: -");
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

        marksModel = createModel(new String[]{"Course", "Course Name", "CA", "END", "Total", "Grade", "Grade Value"});
        attendanceModel = createModel(new String[]{"Date", "Type", "Hours", "Status"});
        medicalModel = createModel(new String[]{"Medical ID", "Date", "Type", "Reason", "Status"});

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profile", new JScrollPane(profilePanel));
        tabs.addTab("Marks / Grades", new JScrollPane(new JTable(marksModel)));
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
            for (String course : studentDetailsController.loadLecturerCourses(lecturerId)) {
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
            for (String student : studentDetailsController.loadStudentsByCourse(course)) {
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

        StudentDetailsResult result = studentDetailsController.loadStudentDetails(studentId, courseCode);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        loadProfile(result.getProfile());
        loadMarks(result.getMarks(), result.getSelectedCourseBreakdown());
        loadAttendance(result.getAttendanceRows());
        loadMedical(result.getMedicalRecords());
    }

    private void loadProfile(Map<String, String> details) {
        profilePanel.removeAll();
        for (Map.Entry<String, String> entry : details.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            profilePanel.add(label);
        }
        profilePanel.revalidate();
        profilePanel.repaint();
    }

    private void loadMarks(List<MarksCalculator.MarkBreakdown> breakdowns, MarksCalculator.MarkBreakdown selectedCourse) {
        marksModel.setRowCount(0);
        for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
            marksModel.addRow(new Object[]{
                    breakdown.getCourseCode(),
                    breakdown.getCourseName(),
                    breakdown.getCaMarks(),
                    breakdown.getEndMarks(),
                    breakdown.hasMarks() ? breakdown.getTotalMarks() : "Pending",
                    breakdown.getGrade(),
                    breakdown.hasMarks() ? breakdown.getGradePoint() : "Pending"
            });
        }

        if (selectedCourse == null) {
            lblEligibility.setText("Eligibility: No marks yet for selected course");
        } else {
            double caPassMark = CourseMarkScheme.forCourse(selectedCourse.getCourseCode()).getCaPassMark();
            boolean eligible = selectedCourse.hasMarks() && selectedCourse.getCaMarks() >= caPassMark;
            lblEligibility.setText("Eligibility: " + (eligible ? "Eligible for end assessment" : "Not eligible for end assessment"));
        }

        GpaCalculator.GpaResult gpaResult = GpaCalculator.calculate(breakdowns);
        if (gpaResult.isSgpaAvailable() && gpaResult.isCgpaAvailable()) {
            lblGpa.setText(String.format("SGPA: %.2f | CGPA: %.2f",
                    gpaResult.getSgpa(), gpaResult.getCgpa()));
        } else {
            lblGpa.setText("SGPA: Not available | CGPA: Not available");
        }
    }

    private void loadAttendance(List<Object[]> attendanceRows) {
        attendanceModel.setRowCount(0);
        for (Object[] row : attendanceRows) {
            attendanceModel.addRow(row);
        }
    }

    private void loadMedical(List<MedicalRecord> records) {
        medicalModel.setRowCount(0);
        for (MedicalRecord record : records) {
            medicalModel.addRow(new Object[]{
                    record.getMedicalId(),
                    record.getSessionDate(),
                    record.getSessionType(),
                    record.getReason(),
                    record.isApproved() ? "Approved" : "Pending"
            });
        }
    }
}
