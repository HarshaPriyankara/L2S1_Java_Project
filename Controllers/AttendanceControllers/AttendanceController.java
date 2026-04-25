package Controllers.AttendanceControllers;

import DAO.AttendanceDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AttendanceController {
    private final AttendanceDAO dao = new AttendanceDAO();

    public ArrayList<String> getCourseList() {
        try {
            return dao.getAllCourseCodes();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public void loadEnrolledToTable(String courseCode, javax.swing.table.DefaultTableModel model) {
        try {
            model.setRowCount(0);
            ArrayList<String> students = dao.getEnrolledStudentIDs(courseCode);
            for (String id : students) {
                model.addRow(new Object[]{id, courseCode, "Present", 2.0});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean saveAttendance(String course, String date, String type, Vector<Vector> data) {
        try {
            for (Vector row : data) {
                dao.addAttendance(row.get(0).toString(), course, date, type,
                        Double.parseDouble(row.get(3).toString()), row.get(2).toString());
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<String> getStudentCourses(String studentId) {
        try {
            return dao.getStudentEnrolledCourses(studentId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public StudentAttendanceResult loadStudentAttendance(String studentId, String courseCode) {
        List<Object[]> rows = new ArrayList<>();
        int totalSessions = 0;
        int presentSessions = 0;

        try (ResultSet rs = dao.getStudentAttendance(studentId, courseCode)) {
            while (rs.next()) {
                totalSessions++;
                String status = rs.getString("Status");
                if ("Present".equalsIgnoreCase(status) || "Medical".equalsIgnoreCase(status)) {
                    presentSessions++;
                }

                rows.add(new Object[]{
                        rs.getString("Session_date"),
                        rs.getString("Session_type"),
                        rs.getString("Session_hours"),
                        status
                });
            }
            return new StudentAttendanceResult(rows, totalSessions, presentSessions, null);
        } catch (Exception ex) {
            return new StudentAttendanceResult(null, 0, 0, "Error loading data: " + ex.getMessage());
        }
    }

    public AttendanceRecordsResult loadAttendanceRecords(String course, String studentId) {
        List<Object[]> rows = new ArrayList<>();
        try (ResultSet rs = dao.getAttendanceRecords(course, studentId == null ? "" : studentId.trim())) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("Attendance_id"),
                        rs.getString("Reg_no"),
                        rs.getString("Course_code"),
                        rs.getString("Session_date"),
                        rs.getString("Status"),
                        rs.getString("Session_hours")
                });
            }
            return new AttendanceRecordsResult(rows, null);
        } catch (Exception ex) {
            return new AttendanceRecordsResult(null, "Error loading data: " + ex.getMessage());
        }
    }

    public AttendanceActionResult updateAttendanceRecord(int id, String status, String hoursText) {
        try {
            double hours = Double.parseDouble(hoursText);
            dao.updateAttendance(id, status, hours);
            return new AttendanceActionResult(true, "Attendance record updated successfully!");
        } catch (Exception ex) {
            return new AttendanceActionResult(false, "Update failed: " + ex.getMessage());
        }
    }

    public AttendanceActionResult deleteAttendanceRecord(int id) {
        try {
            dao.deleteAttendance(id);
            return new AttendanceActionResult(true, "Record deleted!");
        } catch (Exception ex) {
            return new AttendanceActionResult(false, "Delete failed: " + ex.getMessage());
        }
    }

    public static class StudentAttendanceResult {
        private final List<Object[]> rows;
        private final int totalSessions;
        private final int presentSessions;
        private final String errorMessage;

        public StudentAttendanceResult(List<Object[]> rows, int totalSessions, int presentSessions, String errorMessage) {
            this.rows = rows;
            this.totalSessions = totalSessions;
            this.presentSessions = presentSessions;
            this.errorMessage = errorMessage;
        }

        public List<Object[]> getRows() {
            return rows;
        }

        public int getTotalSessions() {
            return totalSessions;
        }

        public int getPresentSessions() {
            return presentSessions;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }

    public static class AttendanceRecordsResult {
        private final List<Object[]> rows;
        private final String errorMessage;

        public AttendanceRecordsResult(List<Object[]> rows, String errorMessage) {
            this.rows = rows;
            this.errorMessage = errorMessage;
        }

        public List<Object[]> getRows() {
            return rows;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }

    public static class AttendanceActionResult {
        private final boolean success;
        private final String message;

        public AttendanceActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
