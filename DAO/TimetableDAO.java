package DAO;

import Models.Timetable;
import Utils.DBConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    /*
      sync timetable
     */
    public boolean syncTimetable(List<Timetable> list, String deptId) {
        String deleteSql = "DELETE FROM timetable WHERE Department_id = ?";

        // insert new data
        String insertSql = "INSERT INTO timetable (Start_time, End_time, Day, Session_type, Venue, Department_id, Course_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaction start


            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setString(1, deptId);
                psDel.executeUpdate();
            }

            // add new data
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (Timetable tt : list) {
                    psIns.setTime(1, Time.valueOf(tt.getStartTime()));
                    psIns.setTime(2, Time.valueOf(tt.getEndTime()));
                    psIns.setString(3, tt.getDay());
                    psIns.setString(4, tt.getSessionType());
                    psIns.setString(5, tt.getVenue());
                    psIns.setString(6, deptId);
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
     get data
     */
    public List<Timetable> getFiltered(String deptId) {
        List<Timetable> list = new ArrayList<>();

        // JOIN එකක් මගින් Course Name එක ලබා ගනී
        String sql = "SELECT t.*, c.Course_name FROM timetable t " +
                "LEFT JOIN course c ON t.Course_code = c.Course_code " +
                "WHERE t.Department_id = ? " +
                "ORDER BY FIELD(t.Day, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), " +
                "t.Start_time ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timetable tt = new Timetable();
                tt.setCourseCode(rs.getString("Course_code"));
                tt.setCourseName(rs.getString("Course_name")); // Course table name
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



    public List<Timetable> getStudentTimetable(String deptId) {
        return getFiltered(deptId);
    }


    public List<Timetable> getTOTimetableFiltered(String deptId) {
        return getFiltered(deptId);
    }
}