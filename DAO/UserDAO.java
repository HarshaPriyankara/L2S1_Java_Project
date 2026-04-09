package DAO;

import Models.User;
import Utils.DBConnection;

import java.sql.*;

public class UserDAO {

    // ── Validate login ───────────────────────────────────────────────────────

    public User validateUser(String username, String password) {
        String sql = "SELECT * FROM user WHERE User_id = ? AND Password = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getString("User_id"));
                user.setFname(rs.getString("F_name"));
                user.setLname(rs.getString("L_name"));
                user.setEmail(rs.getString("Email"));
                user.setRole(rs.getString("Role"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }

    // ── Update profile ───────────────────────────────────────────────────────

    public boolean updateProfile(User user) {
        String sql = "UPDATE user SET F_name=?, L_name=?, date_of_birth=?, Address=?, " +
                "Email=?, contact_no=? WHERE User_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getFname());
            pst.setString(2, user.getLname());
            pst.setDate  (3, user.getDateOfBirth() != null
                    ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(4, user.getAddress());
            pst.setString(5, user.getEmail());
            pst.setString(6, user.getContactNo());
            pst.setString(7, user.getUserID());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Create user ──────────────────────────────────────────────────────────

    public boolean createUser(User user) {
        String sql = "INSERT INTO user (User_id, F_name, L_name, Email, Password, " +
                "Role, date_of_birth, Address, contact_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getUserID());
            pst.setString(2, user.getFname());
            pst.setString(3, user.getLname());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getRole());
            pst.setDate  (7, user.getDateOfBirth() != null
                    ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(8, user.getAddress());
            pst.setString(9, user.getContactNo());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Update user (admin) ──────────────────────────────────────────────────

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET F_name=?, L_name=?, Email=?, Password=?, " +
                "Role=?, date_of_birth=?, Address=?, contact_no=? WHERE User_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user.getFname());
            pst.setString(2, user.getLname());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getRole());
            pst.setDate  (6, user.getDateOfBirth() != null
                    ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(7, user.getAddress());
            pst.setString(8, user.getContactNo());
            pst.setString(9, user.getUserID());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Delete user ──────────────────────────────────────────────────────────

    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM user WHERE User_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Check user exists ────────────────────────────────────────────────────

    public boolean userExists(String userId) {
        String sql = "SELECT 1 FROM user WHERE User_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}