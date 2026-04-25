package Controllers.StudentControllers;

import DAO.AttendanceDAO;
import DAO.LecturerStudentDAO;
import DAO.MarkDAO;
import DAO.MedicalRecordDAO;
import Models.MedicalRecord;
import Utils.MarksCalculator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDetailsController {
    private final LecturerStudentDAO lecturerStudentDAO = new LecturerStudentDAO();
    private final MarkDAO markDAO = new MarkDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public List<String> loadLecturerCourses(String lecturerId) throws Exception {
        return lecturerStudentDAO.getLecturerCourses(lecturerId);
    }

    public List<String> loadStudentsByCourse(String courseCode) throws Exception {
        return lecturerStudentDAO.getStudentsByCourse(courseCode);
    }

    public StudentDetailsResult loadStudentDetails(String studentId, String courseCode) {
        try {
            Map<String, String> profile = lecturerStudentDAO.getStudentProfile(studentId);
            List<MarksCalculator.MarkBreakdown> marks = markDAO.getStudentMarkBreakdowns(studentId);
            MarksCalculator.MarkBreakdown selectedCourse = markDAO.getStudentCourseBreakdown(studentId, courseCode);
            List<Object[]> attendanceRows = loadAttendanceRows(studentId, courseCode);
            List<MedicalRecord> medicalRecords = medicalRecordDAO.getMedicalRecordsByStudent(studentId);

            return new StudentDetailsResult(profile, marks, selectedCourse, attendanceRows, medicalRecords, null);
        } catch (Exception ex) {
            return new StudentDetailsResult(null, null, null, null, null, "Unable to load student records: " + ex.getMessage());
        }
    }

    private List<Object[]> loadAttendanceRows(String studentId, String courseCode) throws Exception {
        List<Object[]> rows = new ArrayList<>();
        try (ResultSet rs = attendanceDAO.getStudentAttendance(studentId, courseCode)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("Session_date"),
                        rs.getString("Session_type"),
                        rs.getString("Session_hours"),
                        rs.getString("Status")
                });
            }
        }
        return rows;
    }
}
