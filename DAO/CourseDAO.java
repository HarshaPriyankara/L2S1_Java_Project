package DAO;

import Models.Course;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseDAO {
    // add course
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

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course course) {

        String sql = "UPDATE course SET Course_name=?, Credits=?, Type=?, lecturer_in_charge=?, Dep_id=? WHERE course_code=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getType());
            pstmt.setString(4, course.getLecturerId());
            pstmt.setString(5, course.getDeptId());
            pstmt.setString(6, course.getCourseCode()); // WHERE clause එක සඳහා

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete Course
    public boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM course WHERE course_code = ?";

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
