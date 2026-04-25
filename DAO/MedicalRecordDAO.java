package DAO;

import Models.MedicalRecord;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public void addMedical(String regNo, LocalDate sessionDate, String sessionType, String examCourse, String reason)
            throws SQLException {
        addMedical(regNo, sessionDate, sessionType, examCourse, reason, null, false);
    }

    public void addMedical(String regNo, LocalDate sessionDate, String sessionType, String examCourse, String reason,
                           String medicalFilePath)
            throws SQLException {
        addMedical(regNo, sessionDate, sessionType, examCourse, reason, medicalFilePath, false);
    }

    public void addMedical(String regNo, LocalDate sessionDate, String sessionType, String examCourse, String reason,
                           String medicalFilePath, boolean approved)
            throws SQLException {
        ensureMedicalFileColumn();
        String sql = "INSERT INTO medical_record (Reg_no, Session_date, Reason, Session_type, Exam_course, Medical_file, Approved) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, regNo);
            pst.setDate(2, Date.valueOf(sessionDate));
            pst.setString(3, reason);
            pst.setString(4, sessionType);
            pst.setString(5, normalizeExamCourse(sessionType, examCourse));
            pst.setString(6, normalizeFilePath(medicalFilePath));
            pst.setBoolean(7, approved);
            pst.executeUpdate();
        }
    }

    public List<MedicalRecord> getMedicalRecordsByStudent(String studentId) throws SQLException {
        ensureMedicalFileColumn();
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_record WHERE Reg_no = ? ORDER BY Session_date DESC, Medical_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, studentId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRow(rs));
                }
            }
        }
        return records;
    }

    public List<MedicalRecord> getAllMedicalRecords() throws SQLException {
        ensureMedicalFileColumn();
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_record ORDER BY Approved ASC, Session_date DESC, Medical_id DESC";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(mapRow(rs));
            }
        }
        return records;
    }

    public void updateMedical(int medicalId, String regNo, LocalDate sessionDate, String sessionType,
                              String examCourse, String reason, boolean approved) throws SQLException {
        String sql = "UPDATE medical_record SET Reg_no = ?, Session_date = ?, Reason = ?, Session_type = ?, " +
                "Exam_course = ?, Approved = ? WHERE Medical_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, regNo);
            pst.setDate(2, Date.valueOf(sessionDate));
            pst.setString(3, reason);
            pst.setString(4, sessionType);
            pst.setString(5, normalizeExamCourse(sessionType, examCourse));
            pst.setBoolean(6, approved);
            pst.setInt(7, medicalId);
            pst.executeUpdate();
        }
    }

    public void deleteMedical(int medicalId) throws SQLException {
        String sql = "DELETE FROM medical_record WHERE Medical_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, medicalId);
            pst.executeUpdate();
        }
    }

    private MedicalRecord mapRow(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setMedicalId(rs.getInt("Medical_id"));
        record.setRegNo(rs.getString("Reg_no"));

        Date sessionDate = rs.getDate("Session_date");
        if (sessionDate != null) {
            record.setSessionDate(sessionDate.toLocalDate());
        }

        record.setReason(rs.getString("Reason"));
        record.setSessionType(rs.getString("Session_type"));
        record.setExamCourse(rs.getString("Exam_course"));
        record.setMedicalFilePath(rs.getString("Medical_file"));
        record.setApproved(rs.getBoolean("Approved"));
        return record;
    }

    private String normalizeExamCourse(String sessionType, String examCourse) {
        if (!"Exam".equalsIgnoreCase(sessionType)) {
            return null;
        }

        if (examCourse == null) {
            return null;
        }

        String trimmed = examCourse.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeFilePath(String medicalFilePath) {
        if (medicalFilePath == null) {
            return null;
        }

        String trimmed = medicalFilePath.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void ensureMedicalFileColumn() throws SQLException {
        String sql = "ALTER TABLE medical_record ADD COLUMN Medical_file VARCHAR(1000) DEFAULT NULL";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            if (!isDuplicateColumnError(ex)) {
                throw ex;
            }
        }
    }

    private boolean isDuplicateColumnError(SQLException ex) {
        return "42S21".equals(ex.getSQLState()) || ex.getErrorCode() == 1060;
    }
}
