package DAO;

import Models.Course;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseDAO {
    // 1. Course එකක් ඇතුළත් කිරීම (Add Course)
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (Course_code, Course_name, Type, Credits, Lecturer_in_charge, Dep_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getName());
            pstmt.setString(3, course.getType());
            pstmt.setInt(4, course.getCredits());
            pstmt.setString(5, course.getLecturerId());
            pstmt.setString(6, course.getDeptId());

            return pstmt.executeUpdate() > 0; // පේළියක් ඇතුළත් වුණොත් true ලබා දෙයි

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Course එකක් මකා දැමීම (Delete Course)
    public boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM courses WHERE course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
