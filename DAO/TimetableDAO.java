package DAO;

import Models.Timetable;
import Utils.DBConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    /**
     * කාලසටහන එකවර Update/Sync කිරීම (Delete old & Insert new)
     */
    public boolean syncTimetable(List<Timetable> list, String level, String semester, String deptId) {
        String l = level.replaceAll("[^0-9]", ""); // Level 2 -> 2
        String s = semester.replaceAll("[^0-9]", ""); // Semester 1 -> 1

        // 1. අදාළ Level/Sem එකේ පරණ දත්ත විතරක් මකන්න
        String deleteSql = "DELETE FROM timetable WHERE (Department_id = ? OR Department_id = 'D4') " +
                "AND SUBSTRING(Course_code, 4, 1) = ? AND SUBSTRING(Course_code, 5, 1) = ?";

        // 2. අලුත් දත්ත ඇතුළත් කරන්න (Timetable_id එක AUTO_INCREMENT නිසා ඒක අයින් කළා)
        String insertSql = "INSERT INTO timetable (Start_time, End_time, Day, Session_type, Venue, Department_id, Course_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // පරණ දත්ත මකා දැමීම
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setString(1, deptId);
                psDel.setString(2, l);
                psDel.setString(3, s);
                psDel.executeUpdate();
            }

            // අලුත් දත්ත Batch එකක් ලෙස ඇතුළත් කිරීම
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (Timetable tt : list) {
                    psIns.setTime(1, Time.valueOf(tt.getStartTime()));
                    psIns.setTime(2, Time.valueOf(tt.getEndTime()));
                    psIns.setString(3, tt.getDay());
                    psIns.setString(4, tt.getSessionType());
                    psIns.setString(5, tt.getVenue());
                    psIns.setString(6, tt.getDepartmentId());
                    psIns.setString(7, tt.getCourseCode());
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Filtered දත්ත ලබා ගැනීම (Level සහ Semester වලට අනුව හරියටම)
     */
    public List<Timetable> getFiltered(String level, String semester, String deptId) {
        List<Timetable> list = new ArrayList<>();

        String l = level.replaceAll("[^0-9]", "");
        String s = semester.replaceAll("[^0-9]", "");

        // LIKE වෙනුවට SUBSTRING පාවිච්චි කිරීමෙන් Level 3 ඒවා පෙන්වන එක නතර වෙනවා
        String sql = "SELECT t.*, c.Course_name FROM timetable t " +
                "LEFT JOIN course c ON t.Course_code = c.Course_code " +
                "WHERE (t.Department_id = ? OR t.Department_id = 'D4') " +
                "AND SUBSTRING(t.Course_code, 4, 1) = ? " +
                "AND SUBSTRING(t.Course_code, 5, 1) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptId);
            ps.setString(2, l);
            ps.setString(3, s);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timetable tt = new Timetable();
                tt.setTimeTableId(String.valueOf(rs.getInt("Timetable_id")));
                tt.setCourseCode(rs.getString("Course_code"));
                tt.setCourseName(rs.getString("Course_name"));
                tt.setDay(rs.getString("Day"));
                tt.setVenue(rs.getString("Venue"));

                Time st = rs.getTime("Start_time");
                Time et = rs.getTime("End_time");
                if (st != null) tt.setStartTime(st.toLocalTime());
                if (et != null) tt.setEndTime(et.toLocalTime());

                tt.setSessionType(rs.getString("Session_type"));
                list.add(tt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Student / Time Officer පැනල් සඳහා Methods ---
    public List<Timetable> getStudentTimetable(String deptId, String level, String semester) {
        return getFiltered(level, semester, deptId);
    }

    public List<Timetable> getTOTimetableFiltered(String level, String semester, String deptId) {
        return getFiltered(level, semester, deptId);
    }
}