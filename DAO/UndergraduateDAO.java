package DAO;

public class UndergraduateDAO {
    public String getStudentDepartmentId(String studentId) {
        String sql = "SELECT Dep_id FROM student WHERE Reg_no = ?";

        try (java.sql.Connection conn = Utils.DBConnection.getConnection();
             java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, studentId);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("Dep_id");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
