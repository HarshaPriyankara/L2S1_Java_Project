package dao;

import models.User;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Database එකේ user කෙනෙක් ඉන්නවද කියලා බලන method එක
    public User validateUser(String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection(); // ඔයාගේ DB Connection class එක මෙතැනට දාන්න
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // User කෙනෙක් හමු වුණොත් අදාළ දත්ත object එකකට දානවා
                User user = new User();

                // User class එකේ private fields නිසා setters පාවිච්චි කරන්න
                user.setUserID(rs.getString("userID"));
                user.setFname(rs.getString("fname"));
                user.setLname(rs.getString("lname"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role")); // "Admin", "Lecturer" වගේ Role එක

                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null; // User කෙනෙක් නැත්නම් null return කරනවා
    }

    // පැතිකඩ යාවත්කාලීන කිරීම (Update Profile)
    public boolean updateProfile(User user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE users SET fname=?, lname=?, contactNo=?, address=? WHERE userID=?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getFname());
            pst.setString(2, user.getLname());
            pst.setString(3, user.getContactNo());
            pst.setString(4, user.getAddress());
            pst.setString(5, user.getUserID());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
