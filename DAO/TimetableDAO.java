package DAO;

import Models.Timetable;
import Utils.DBConnection; // ඔයාගේ DBConnection එක තියෙන්නේ Utils පැකේජ් එකේ නිසා
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    // 1. කාලසටහනක් Database එකට ඇතුළත් කිරීම (Create)
    public boolean saveTimetable(Timetable tt) {
        String sql = "INSERT INTO timetable (Timetable_id, Start_time, End_time, Day, Session_type, Venue, Department_id, Course_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tt.getTimeTableId());
            ps.setTime(2, Time.valueOf(tt.getStartTime())); // LocalTime -> SQL Time
            ps.setTime(3, Time.valueOf(tt.getEndTime()));
            ps.setString(4, tt.getDay());
            ps.setString(5, tt.getSessionType());
            ps.setString(6, tt.getVenue());
            ps.setString(7, tt.getDepartmentId());
            ps.setString(8, tt.getCourseCode());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. කාලසටහනක් යාවත්කාලීන කිරීම (Update)
    public boolean updateTimetable(Timetable tt) {
        String sql = "UPDATE timetable SET Start_time=?, End_time=?, Day=?, Session_type=?, Venue=?, Department_id=?, Course_code=? WHERE Timetable_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTime(1, Time.valueOf(tt.getStartTime()));
            ps.setTime(2, Time.valueOf(tt.getEndTime()));
            ps.setString(3, tt.getDay());
            ps.setString(4, tt.getSessionType());
            ps.setString(5, tt.getVenue());
            ps.setString(6, tt.getDepartmentId());
            ps.setString(7, tt.getCourseCode());
            ps.setString(8, tt.getTimeTableId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. කාලසටහනක් ඉවත් කිරීම (Delete)
    public boolean deleteTimetable(String id) {
        String sql = "DELETE FROM timetable WHERE Timetable_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. ශිෂ්‍යයාට අදාළ කාලසටහන ලබා ගැනීම (Read/View)
    public List<Timetable> getStudentTimetable(String deptId, String level, String semester) {
        List<Timetable> list = new ArrayList<>();

        // මෙතනදී අපි timetable (t) සහ course (c) කියන table දෙක join කරනවා
        String sql = "SELECT t.*, c.Course_name FROM timetable t " +
                "INNER JOIN course c ON t.Course_code = c.Course_code " +
                "WHERE t.Department_id = ? " + // මෙතන Dep_id වෙනුවට Department_id ලෙස වෙනස් කළා
                "AND SUBSTRING(t.Course_code, 4, 1) = ? " +
                "AND SUBSTRING(t.Course_code, 5, 1) = ? " +
                "ORDER BY FIELD(t.Day, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'), t.Start_time";

        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptId);
            ps.setString(2, level);
            ps.setString(3, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timetable tt = new Timetable();
                tt.setCourseCode(rs.getString("Course_code"));

                // මෙන්න මෙතනදී Course Name එකත් සෙට් කරනවා
                // හැබැයි මීට කලින් ඔයාගේ Models.Timetable එකේ 'courseName' කියලා field එකක් හදලා තියෙන්න ඕනේ
                tt.setCourseName(rs.getString("Course_name"));

                tt.setDay(rs.getString("Day"));
                tt.setStartTime(rs.getTime("Start_time").toLocalTime());
                tt.setEndTime(rs.getTime("End_time").toLocalTime());
                tt.setVenue(rs.getString("Venue"));
                tt.setSessionType(rs.getString("Session_type"));
                list.add(tt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // ID එකෙන් කාලසටහනක් සෙවීම (Search Logic)
    public Timetable searchTimetableById(String id) {
        String sql = "SELECT * FROM timetable WHERE Timetable_id = ?";
        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Timetable tt = new Timetable();
                tt.setTimeTableId(rs.getString("Timetable_id"));
                tt.setCourseCode(rs.getString("Course_code"));
                tt.setDay(rs.getString("Day"));
                tt.setStartTime(rs.getTime("Start_time").toLocalTime());
                tt.setEndTime(rs.getTime("End_time").toLocalTime());
                tt.setVenue(rs.getString("Venue"));
                tt.setSessionType(rs.getString("Session_type"));
                tt.setDepartmentId(rs.getString("Department_id"));
                return tt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // මුකුත් හම්බවුණේ නැත්නම් null යවනවා
    }
}


