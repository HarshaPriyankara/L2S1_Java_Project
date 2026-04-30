package DAO;
import Models.Course;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            pstmt.setString(6, course.getCourseCode());

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

    public List<String> getAllCourseCodes() throws SQLException {
        return getSingleColumnValues("SELECT Course_code FROM course ORDER BY Course_code", "Course_code");
    }

    public List<String> getAllLecturerIds() throws SQLException {
        return getSingleColumnValues("SELECT Lecturer_id FROM lecturer ORDER BY Lecturer_id", "Lecturer_id");
    }

    public List<String> getAllDepartmentIds() throws SQLException {
        return getSingleColumnValues("SELECT Department_id FROM department ORDER BY Department_id", "Department_id");
    }

    private List<String> getSingleColumnValues(String sql, String columnName) throws SQLException {
        List<String> values = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                values.add(rs.getString(columnName));
            }
        }
        return values;
    }

    public List<String[]> getStudentCourses(String regNo) throws SQLException {
        List<String[]> courses = new ArrayList<>();
        String sql = "SELECT c.Course_code, c.Course_name, c.Type, c.Credits, " +
                "CONCAT(u.F_name, ' ', u.L_name) AS Lecturer_Name " +
                "FROM course c " +
                "JOIN enrollment e ON c.Course_code = e.Course_code " +
                "JOIN lecturer l ON c.Lecturer_in_charge = l.Lecturer_id " +
                "JOIN user u ON l.Lecturer_id = u.User_id " +
                "WHERE e.Reg_no = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, regNo);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                courses.add(new String[]{
                        rs.getString("Course_code"),
                        rs.getString("Course_name"),
                        rs.getString("Type"),
                        rs.getString("Credits"),
                        rs.getString("Lecturer_Name")
                });
            }
        }
        return courses;
    }
}
