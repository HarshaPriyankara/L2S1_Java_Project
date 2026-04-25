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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pst.setString(1, user.getUserID());
            pst.setString(2, user.getFname());
            pst.setString(3, user.getLname());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPassword());
            pst.setString(6, normalizeRole(user.getRole()));
            pst.setDate(7, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(8, user.getAddress());
            pst.setString(9, user.getContactNo());
            pst.setString(10, user.getProfilePicPath());

            boolean inserted = pst.executeUpdate() > 0;
            if (!inserted) {
                conn.rollback();
                return false;
            }

            ensureRoleProfileExists(conn, user.getUserID(), normalizeRole(user.getRole()));
            conn.commit();
            return true;

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
            pst.setString(6, normalizeRole(user.getRole()));
            pst.setDate(7, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
            pst.setString(8, user.getAddress());
            pst.setString(9, user.getContactNo());
            pst.setString(10, user.getProfilePicPath()); // Added this line
            pst.setString(11, user.getOriginalUserID());

            boolean updated = pst.executeUpdate() > 0;
            if (updated) {
                ensureRoleProfileExists(conn, user.getUserID(), normalizeRole(user.getRole()));
            }
            return updated;
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
                user.setProfilePicPath(rs.getString("profile_pic"));
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
        String query = "SELECT COUNT(*) FROM user WHERE Email = ?";
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

    private void ensureRoleProfileExists(Connection conn, String userId, String role) throws SQLException {
        if (role == null || role.isBlank()) {
            return;
        }

        String tableName;
        String idColumn;
        switch (role) {
            case "admin":
                tableName = "admin";
                idColumn = "Admin_id";
                break;
            case "student":
                tableName = "student";
                idColumn = "Reg_no";
                break;
            case "lecturer":
                tableName = "lecturer";
                idColumn = "Lecturer_id";
                break;
            case "techofficer":
                tableName = "technical_officer";
                idColumn = "To_id";
                break;
            default:
                return;
        }

        String checkSql = "SELECT 1 FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setString(1, userId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return;
            }
        }

        String insertSql = "INSERT INTO " + tableName + " (" + idColumn + ") VALUES (?)";
        try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
            insert.setString(1, userId);
            insert.executeUpdate();
        }
    }

    private String normalizeRole(String role) {
        if (role == null) return null;

        String value = role.trim().toLowerCase();
        switch (value) {
            case "lecture":
            case "lecturer":
                return "lecturer";
            case "student":
            case "undergraduate":
                return "student";
            case "technical officer":
            case "techofficer":
                return "techofficer";
            case "admin":
                return "admin";
            default:
                return value;
        }
    }
}
