package DAO;

import Models.Notice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {


    public List<Notice> getNoticesByRole(String role) {
        List<Notice> notices = new ArrayList<>();

          String sql = "SELECT * FROM notice WHERE target_role = ? OR target_role = 'All'";

        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                notices.add(new Notice(
                        rs.getInt("Notice_id"),
                        rs.getString("Title"),
                        rs.getString("Target_role"),
                        rs.getString("Created_date"),
                        rs.getString("Content") // මෙතන තමා path එක තියෙන්නේ
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }
}
