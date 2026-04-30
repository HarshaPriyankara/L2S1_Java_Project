package DAO;

import Models.Notice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 interface INoticeDAO {
    List<Notice> getNoticesByRole(String role);
    String getNoticeContentPath(int noticeId);
    boolean updateNotice(int id, String roles, String title, String path);
    Notice getNoticeById(int noticeId);
}

 public class NoticeDAO implements INoticeDAO {

    // Method Overloading
    // Providing an overload method without a parameter to  'All' roles.
    public List<Notice> getNoticesByRole() {
        return getNoticesByRole("All");
    }

    @Override
    public List<Notice> getNoticesByRole(String role) {
        List<Notice> notices = new ArrayList<>();

        String sql = "SELECT * FROM notice WHERE target_role LIKE ? OR target_role = 'All'";

        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + role + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                notices.add(new Notice(
                        rs.getInt("Notice_id"),
                        rs.getString("Title"),
                        rs.getString("Target_role"),
                        rs.getString("Created_date"),
                        rs.getString("Content")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }

    @Override
    public String getNoticeContentPath(int noticeId) {
        String sql = "SELECT Content FROM notice WHERE Notice_id = ?";
        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noticeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Content");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateNotice(int id, String roles, String title, String path) {
        // ENCAPSULATION/ABSTRACTION: Complex DB logic is hidden here
        String sql = "UPDATE notice SET Title = ?, Target_role = ?, Content = ? WHERE Notice_id = ?";
        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, roles);
            pstmt.setString(3, path);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notice getNoticeById(int noticeId) {
        String sql = "SELECT * FROM notice WHERE Notice_id = ?";
        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noticeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Notice(
                        rs.getInt("Notice_id"),
                        rs.getString("Title"),
                        rs.getString("Target_role"),
                        rs.getString("Created_date"),
                        rs.getString("Content")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}