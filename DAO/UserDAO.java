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
                user.setProfilePicPath(rs.getString("profile_pic"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }


    // user create method
    public boolean createUser(User user) {
        String sql = "INSERT INTO user (User_id, F_name, L_name, Email, Password, " +
                "Role, date_of_birth, Address, contact_no, profile_pic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getUserID());
            pst.setString(2, user.getFname());
            pst.setString(3, user.getLname());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getRole());
            pst.setDate(7, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(8, user.getAddress());
            pst.setString(9, user.getContactNo());
            pst.setString(10, user.getProfilePicPath());


            return pst.executeUpdate() > 0;  //check affected rows and return true or false

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Update UPDATE USER
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET User_id=?, F_name=?, L_name=?, Email=?, Password=?, " +
                "Role=?, date_of_birth=?, Address=?, contact_no=?, profile_pic=? WHERE User_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getUserID());
            pst.setString(2, user.getFname());
            pst.setString(3, user.getLname());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getRole());
            pst.setDate(7, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(8, user.getAddress());
            pst.setString(9, user.getContactNo());
            pst.setString(10, user.getProfilePicPath()); // Added this line
            pst.setString(11, user.getOriginalUserID());

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


    // ── Get user by ID  for update user───────────────────────────────────────────────────────

    public User getUserById(String userId) {
        String sql = "SELECT * FROM user WHERE User_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getString("User_id"));
                user.setFname(rs.getString("F_name"));
                user.setLname(rs.getString("L_name"));
                user.setEmail(rs.getString("Email"));
                user.setRole(rs.getString("Role"));
                user.setContactNo(rs.getString("contact_no"));
                user.setAddress(rs.getString("Address"));
                user.setPassword(rs.getString("Password"));
                Date dob = rs.getDate("date_of_birth");
                if (dob != null) user.setDob(dob.toLocalDate());
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}