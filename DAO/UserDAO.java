package DAO;

import Models.User;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    //check user ib db
    public User validateUser(String username, String password) throws SQLException {
        Connection conn = DBConnection.getConnection(); // db connection
        String sql = "SELECT * FROM user WHERE  User_id= ? AND Password = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                User user = new User();

                user.setUserID(rs.getString("User_id"));
                user.setFname(rs.getString("f_name"));
                user.setLname(rs.getString("l_name"));
                user.setEmail(rs.getString("Email"));
                user.setRole(rs.getString("Role"));

                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null; // if not user return null
    }

    // update profile
    public boolean updateProfile(User user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE users SET F_name=?, L_name=?, date_of_birth=?, Address=?, Address=? WHERE User_id=?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getFname());
            pst.setString(2, user.getLname());
            pst.setString(3, user.getDateOfBirth().toString());
            pst.setString(4, user.getAddress());
            pst.setString(5, user.getUserID());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
