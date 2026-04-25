package DAO;

import Models.User;
import Utils.DBConnection;

import java.sql.*;

public class UserDAO {

    // ── Validate login ───────────────────────────────────────────────────────

    public User validateUser(String username, String password) {
        String sql = "SELECT * FROM user WHERE User_id = ? AND Password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Login Error: " + e.getMessage(), e);
        }
        return null;
    }


    // user create method
    /// @author dilusha
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
        String originalUserId = user.getOriginalUserID();
        String newUserId = user.getUserID();
        String newRole = normalizeRole(user.getRole());

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String oldRole = getStoredRole(conn, originalUserId);
            boolean requiresProfileSync = requiresProfileSync(oldRole, newRole) ||
                    (originalUserId != null && !originalUserId.equals(newUserId));

            if (requiresProfileSync) {
                setForeignKeyChecks(conn, false);
            }

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, newUserId);
                pst.setString(2, user.getFname());
                pst.setString(3, user.getLname());
                pst.setString(4, user.getEmail());
                pst.setString(5, user.getPassword());
                pst.setString(6, newRole);
                pst.setDate(7, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
                pst.setString(8, user.getAddress());
                pst.setString(9, user.getContactNo());
                pst.setString(10, user.getProfilePicPath());
                pst.setString(11, originalUserId);

                boolean updated = pst.executeUpdate() > 0;
                if (!updated) {
                    conn.rollback();
                    if (requiresProfileSync) {
                        setForeignKeyChecks(conn, true);
                    }
                    return false;
                }
            }

            syncRoleProfilesOnUpdate(conn, originalUserId, newUserId, oldRole, newRole);
            ensureRoleProfileExists(conn, newUserId, newRole);

            if (requiresProfileSync) {
                setForeignKeyChecks(conn, true);
            }
            conn.commit();
            return true;
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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error: " + e.getMessage(), e);
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
                // The current schema stores admins only in the user table.
                return;
            case "student":
                tableName = "student";
                idColumn = "Reg_no";
                break;
            case "lecturer":
                tableName = "lecturer";
                idColumn = "Lecturer_id";
                break;
            case "techofficer":
                // The current schema stores technical officers only in the user table.
                return;
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

    private void syncRoleProfilesOnUpdate(Connection conn, String oldUserId, String newUserId,
                                          String oldRole, String newRole) throws SQLException {
        String oldTable = getRoleProfileTable(oldRole);
        String newTable = getRoleProfileTable(newRole);
        String oldIdColumn = getRoleProfileIdColumn(oldRole);
        String newIdColumn = getRoleProfileIdColumn(newRole);

        boolean idChanged = oldUserId != null && !oldUserId.equals(newUserId);

        if (oldTable == null && newTable == null) {
            return;
        }

        if (oldTable != null && oldTable.equals(newTable)) {
            if (idChanged) {
                String updateSql = "UPDATE " + oldTable + " SET " + oldIdColumn + " = ? WHERE " + oldIdColumn + " = ?";
                try (PreparedStatement pst = conn.prepareStatement(updateSql)) {
                    pst.setString(1, newUserId);
                    pst.setString(2, oldUserId);
                    pst.executeUpdate();
                }
            }
            return;
        }

        if (oldTable != null) {
            String deleteSql = "DELETE FROM " + oldTable + " WHERE " + oldIdColumn + " = ?";
            try (PreparedStatement pst = conn.prepareStatement(deleteSql)) {
                pst.setString(1, oldUserId);
                pst.executeUpdate();
            }
        }

        if (newTable != null) {
            String insertSql = "INSERT INTO " + newTable + " (" + newIdColumn + ") VALUES (?)";
            try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                pst.setString(1, newUserId);
                pst.executeUpdate();
            }
        }
    }

    private boolean requiresProfileSync(String oldRole, String newRole) {
        return getRoleProfileTable(oldRole) != null || getRoleProfileTable(newRole) != null;
    }

    private String getStoredRole(Connection conn, String userId) throws SQLException {
        String sql = "SELECT Role FROM user WHERE User_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return normalizeRole(rs.getString("Role"));
            }
            return null;
        }
    }

    private void setForeignKeyChecks(Connection conn, boolean enabled) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = " + (enabled ? "1" : "0"))) {
            pst.execute();
        }
    }

    private String getRoleProfileTable(String role) {
        String value = normalizeRole(role);
        if (value == null) {
            return null;
        }

        switch (value) {
            case "student":
                return "student";
            case "lecturer":
                return "lecturer";
            default:
                return null;
        }
    }

    private String getRoleProfileIdColumn(String role) {
        String value = normalizeRole(role);
        if (value == null) {
            return null;
        }

        switch (value) {
            case "student":
                return "Reg_no";
            case "lecturer":
                return "Lecturer_id";
            default:
                return null;
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

    private User mapUser(ResultSet rs) throws SQLException {
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
        if (dob != null) {
            user.setDob(dob.toLocalDate());
        }
        return user;
    }
}
