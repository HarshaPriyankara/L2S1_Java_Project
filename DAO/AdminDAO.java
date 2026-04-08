package DAO;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public boolean addNotice(String roles, String title, String contentPath) {
        String sql = "INSERT INTO notice (Target_role, Title, Created_date, Content) VALUES (?, ?, CURDATE(), ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, roles);
            pst.setString(2, title);
            pst.setString(3, contentPath);

            int result = pst.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("DAO Error: " + e.getMessage());
            return false;
        }
    }



    public List<Object[]> getAllNotices() {
        List<Object[]> notices = new ArrayList<>();
        String sql = "SELECT * FROM notice ORDER BY Created_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                notices.add(new Object[]{
                        rs.getInt("Notice_id"), // ඔයාගේ DB එකේ ID එකේ නම බලන්න
                        rs.getString("Title"),
                        rs.getString("Target_role"),
                        rs.getDate("Created_date")
                });
            }
        } catch (SQLException e) {
            System.out.println("DAO Error (View): " + e.getMessage());
        }
        return notices;

    }


    public String getNoticeContentPath(int noticeId) {
        String path = null;
        String sql = "SELECT Content FROM notice WHERE Notice_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, noticeId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                path = rs.getString("Content");
            }
        } catch (SQLException e) {
            System.out.println("DAO Error (GetPath): " + e.getMessage());
        }
        return path;
    }

}
