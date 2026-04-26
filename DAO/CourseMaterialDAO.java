package DAO;

import Controllers.CourseMaterialControllers.CourseMaterialRow;
import Utils.DBConnection;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseMaterialDAO {

    public boolean addMaterial(String title, String courseCode, String uploadedBy, String fileUrl) {
        String sql = "INSERT INTO course_material (Title, Course_code, Uploaded_by, File_URL) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, title);
            pstmt.setString(2, courseCode);
            pstmt.setString(3, uploadedBy);
            pstmt.setString(4, fileUrl);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseMaterialRow> getMaterialsByLecturer(String lecturerId) {
        List<CourseMaterialRow> materials = new ArrayList<>();
        String sql = "SELECT Material_id, Title, Course_code, Uploaded_at, File_URL FROM course_material " +
                "WHERE Uploaded_by = ? ORDER BY Material_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lecturerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                materials.add(new CourseMaterialRow(
                        rs.getInt("Material_id"),
                        rs.getString("Title"),
                        rs.getString("Course_code"),
                        rs.getString("Uploaded_at"),
                        rs.getString("File_URL")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }

    public List<String> getCourseCodesByLecturer(String lecturerId) {
        List<String> courseCodes = new ArrayList<>();
        String sql = "SELECT Course_code FROM course WHERE Lecturer_in_charge = ? ORDER BY Course_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lecturerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courseCodes.add(rs.getString("Course_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseCodes;
    }

    public List<Object[]> getMaterialsByCourse(String courseCode) {
        List<Object[]> materials = new ArrayList<>();
        String sql = "SELECT Title, Uploaded_at, File_URL FROM course_material " +
                "WHERE Course_code = ? ORDER BY Material_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                materials.add(new Object[]{
                        rs.getString("Title"),
                        rs.getString("Uploaded_at"),
                        rs.getString("File_URL")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materials;
    }

    public boolean updateMaterial(int materialId, String title, String courseCode, String fileUrl) {
        String sql = "UPDATE course_material SET Title = ?, Course_code = ?, File_URL = ? WHERE Material_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, courseCode);
            pstmt.setString(3, fileUrl);
            pstmt.setInt(4, materialId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMaterial(int materialId, String lecturerId) {
        String sql = "DELETE FROM course_material WHERE Material_id = ? AND Uploaded_by = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, materialId);
            pstmt.setString(2, lecturerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
