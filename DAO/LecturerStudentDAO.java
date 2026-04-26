package DAO;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LecturerStudentDAO {

    public List<String> getLecturerCourses(String lecturerId) throws SQLException {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT Course_code FROM course WHERE Lecturer_in_charge = ? ORDER BY Course_code";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, lecturerId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    courses.add(rs.getString("Course_code"));
                }
            }
        }
        return courses;
    }

    public List<String> getStudentsByCourse(String courseCode) throws SQLException {
        List<String> students = new ArrayList<>();
        String sql = "SELECT Reg_no FROM enrollment WHERE Course_code = ? ORDER BY Reg_no";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, courseCode);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    students.add(rs.getString("Reg_no"));
                }
            }
        }
        return students;
    }

    public Map<String, String> getStudentProfile(String studentId) throws SQLException {
        Map<String, String> profile = new LinkedHashMap<>();
        String sql = "SELECT u.User_id, u.F_name, u.L_name, u.Email, u.contact_no, u.Address, " +
                "u.date_of_birth, s.Batch, s.Student_type, d.Dep_name " +
                "FROM user u " +
                "JOIN student s ON u.User_id = s.Reg_no " +
                "LEFT JOIN department d ON s.Dep_id = d.Department_id " +
                "WHERE s.Reg_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    profile.put("Registration No", safe(rs.getString("User_id")));
                    profile.put("First Name", safe(rs.getString("F_name")));
                    profile.put("Last Name", safe(rs.getString("L_name")));
                    profile.put("Email", safe(rs.getString("Email")));
                    profile.put("Contact No", safe(rs.getString("contact_no")));
                    profile.put("Address", safe(rs.getString("Address")));
                    profile.put("Date of Birth", safe(rs.getString("date_of_birth")));
                    profile.put("Batch", safe(rs.getString("Batch")));
                    profile.put("Student Type", safe(rs.getString("Student_type")));
                    profile.put("Department", safe(rs.getString("Dep_name")));
                }
            }
        }
        return profile;
    }

    private String safe(String value) {
        if (value == null) {
            return "-";
        }

        return value;
    }
}
