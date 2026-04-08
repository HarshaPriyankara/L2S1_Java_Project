package DAO;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
