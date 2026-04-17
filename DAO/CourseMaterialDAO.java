package DAO;

import Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseMaterialDAO {

    public static boolean addMaterial(String title, String courseCode, String uploadedBy, String fileUrl) {
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
}