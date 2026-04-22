package DAO;

import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;

public class AttendanceDAO {

    // Get all subjects
    public ArrayList<String> getAllCourseCodes() throws SQLException {
        ArrayList<String> courses = new ArrayList<>();
        String sql = "SELECT Course_code FROM course";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(rs.getString("Course_code"));
            }
        }
        return courses;
    }

    // Get enroll students
    public ArrayList<String> getEnrolledStudentIDs(String courseCode) throws SQLException {
        ArrayList<String> students = new ArrayList<>();
        String sql = "SELECT Reg_no FROM enrollment WHERE Course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, courseCode);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    students.add(rs.getString("Reg_no"));
                }
            }
        }
        return students;
    }

    // Insert data
    public void addAttendance(String regNo, String course, String date, String type, double hours, String status) throws SQLException {
        String sql = "INSERT INTO attendance (Reg_no, Course_code, Session_date, Session_type, Session_hours, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, date);
            pst.setString(4, type);
            pst.setDouble(5, hours);
            pst.setString(6, status);
            pst.executeUpdate();
        }
    }

    // Update data
    public void updateAttendance(int id, String status, double hours) throws SQLException {
        String sql = "UPDATE attendance SET Status = ?, Session_hours = ? WHERE Attendance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setDouble(2, hours);
            pst.setInt(3, id);
            pst.executeUpdate();
        }
    }

    // Delete data
    public void deleteAttendance(int id) throws SQLException {
        String sql = "DELETE FROM attendance WHERE Attendance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Filter by Course and Student
    public ResultSet getAttendanceRecords(String course, String studentID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM attendance WHERE Course_code = ?";
        if (studentID != null && !studentID.isEmpty()) {
            sql += " AND Reg_no = ?";
        }
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, course);
        if (studentID != null && !studentID.isEmpty()) {
            pst.setString(2, studentID);
        }
        return pst.executeQuery();
    }

    public ArrayList<String> getStudentEnrolledCourses(String studentId) throws SQLException {
        ArrayList<String> courses = new ArrayList<>();
        String sql = "SELECT Course_code FROM enrollment WHERE Reg_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("Course_code"));
            }
        }
        return courses;
    }

    public ResultSet getStudentAttendance(String studentId, String courseCode) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT Session_date, Session_type, Session_hours, Status FROM attendance " +
                "WHERE Reg_no = ? AND Course_code = ? ORDER BY Session_date DESC";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, studentId);
        pst.setString(2, courseCode);
        return pst.executeQuery();
    }
}