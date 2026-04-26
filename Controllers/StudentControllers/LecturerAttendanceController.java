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
                double percentage = 0.0;

                if (totalHours > 0) {
                    percentage = round((attendedHours / totalHours) * 100.0);
                }

                summaryRows.add(new LecturerAttendanceSummaryRow(
                        summaryRs.getString("Reg_no"),
                        attendedHours,
                        totalHours,
                        attendedSessions,
                        totalSessions,
                        percentage
                ));
            }

            String detailStudentId = resolveDetailStudentId(studentId);
            if (detailStudentId != null) {
                try (ResultSet detailRs = attendanceDAO.getStudentAttendanceDetails(courseCode, detailStudentId, sessionFilter)) {
                    while (detailRs.next()) {
                        String sessionDate = detailRs.getString("Session_date");
                        String sessionType = detailRs.getString("Session_type");
                        String sessionHours = detailRs.getString("Session_hours");
                        String status = detailRs.getString("Status");

                        Object[] row = {sessionDate, sessionType, sessionHours, status};
                        detailRows.add(row);
                    }
                }
            }

            return new LecturerAttendanceResult(summaryRows, detailRows, null);
        } catch (Exception ex) {
            return new LecturerAttendanceResult(null, null, "Unable to load attendance: " + ex.getMessage());
        }
    }

    private String resolveDetailStudentId(String studentId) {
        if (studentId != null && !studentId.isBlank()) {
            return studentId.trim();
        }
        return null;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
