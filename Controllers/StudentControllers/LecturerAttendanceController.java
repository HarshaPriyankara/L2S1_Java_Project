package Controllers.StudentControllers;

import DAO.AttendanceDAO;
import DAO.LecturerStudentDAO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LecturerAttendanceController {
    private final LecturerStudentDAO lecturerStudentDAO = new LecturerStudentDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public List<String> loadLecturerCourses(String lecturerId) throws Exception {
        return lecturerStudentDAO.getLecturerCourses(lecturerId);
    }

    public LecturerAttendanceResult loadAttendance(String courseCode, String sessionFilter, String studentId) {
        if (courseCode == null || courseCode.isBlank()) {
            return new LecturerAttendanceResult(null, null, "Please select a course.");
        }

        List<LecturerAttendanceSummaryRow> summaryRows = new ArrayList<>();
        List<Object[]> detailRows = new ArrayList<>();

        try (ResultSet summaryRs = attendanceDAO.getCourseAttendanceSummary(courseCode, studentId, sessionFilter)) {
            while (summaryRs.next()) {
                double attendedHours = summaryRs.getDouble("attended_hours");
                double totalHours = summaryRs.getDouble("total_hours");
                int attendedSessions = summaryRs.getInt("attended_sessions");
                int totalSessions = summaryRs.getInt("total_sessions");
                double percentage = totalHours <= 0 ? 0.0 : round((attendedHours / totalHours) * 100.0);

                summaryRows.add(new LecturerAttendanceSummaryRow(
                        summaryRs.getString("Reg_no"),
                        attendedHours,
                        totalHours,
                        attendedSessions,
                        totalSessions,
                        percentage
                ));
            }

            String detailStudentId = resolveDetailStudentId(summaryRows, studentId);
            if (detailStudentId != null) {
                try (ResultSet detailRs = attendanceDAO.getStudentAttendanceDetails(courseCode, detailStudentId, sessionFilter)) {
                    while (detailRs.next()) {
                        detailRows.add(new Object[]{
                                detailRs.getString("Session_date"),
                                detailRs.getString("Session_type"),
                                detailRs.getString("Session_hours"),
                                detailRs.getString("Status")
                        });
                    }
                }
            }

            return new LecturerAttendanceResult(summaryRows, detailRows, null);
        } catch (Exception ex) {
            return new LecturerAttendanceResult(null, null, "Unable to load attendance: " + ex.getMessage());
        }
    }

    private String resolveDetailStudentId(List<LecturerAttendanceSummaryRow> summaryRows, String studentId) {
        if (studentId != null && !studentId.isBlank()) {
            return studentId.trim();
        }
        if (summaryRows == null || summaryRows.isEmpty()) {
            return null;
        }
        return summaryRows.get(0).getRegNo();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
