package DAO;

import Models.Timetable;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    // add new time table
    public boolean saveTimetable(Timetable tt) {
        String sql = "INSERT INTO timetable (Timetable_id, Start_time, End_time, Day, Session_type, Venue, Department_id, Course_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getTimeTableId());
            ps.setTime(2, Time.valueOf(tt.getStartTime()));
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

    // 2. Id search
    public Timetable searchTimetableById(String id) {
        String sql = "SELECT * FROM timetable WHERE Timetable_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timetable tt = new Timetable();
                tt.setTimeTableId(rs.getString("Timetable_id"));
                tt.setStartTime(rs.getTime("Start_time").toLocalTime());
                tt.setEndTime(rs.getTime("End_time").toLocalTime());
                tt.setDay(rs.getString("Day"));
                tt.setVenue(rs.getString("Venue"));
                tt.setDepartmentId(rs.getString("Department_id"));
                tt.setCourseCode(rs.getString("Course_code"));
                tt.setSessionType(rs.getString("Session_type"));
                return tt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


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


    // 5. get coursecode fot dropdown
    public List<String> getCoursesByLevelAndSem(String level, String semester, String deptId) {
        List<String> list = new ArrayList<>();

        // get level and semester num
        String l = level.replaceAll("[^0-9]", "");
        String s = semester.replaceAll("[^0-9]", "");

        // use LIKE and get year and sem
        // eg: %21%  ICT2112
        String pattern = "%" + l + s + "%";

        String sql = "SELECT Course_code FROM course WHERE Dep_id = ? AND Course_code LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptId);
            ps.setString(2, pattern);

            System.out.println("Querying for Dept: " + deptId + " with Pattern: " + pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Course_code"));
            }

            System.out.println("Courses Found: " + list.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. filtered timetable for student and to
    public List<Timetable> getStudentTimetable(String deptId, String level, String semester) {
        return getFiltered(level, semester, deptId);
    }

    public List<Timetable> getTOTimetableFiltered(String level, String semester, String deptId) {
        return getFiltered(level, semester, deptId);
    }

    public List<Timetable> getFiltered(String level, String semester, String deptId) {
        List<Timetable> list = new ArrayList<>();
        String l = level.replaceAll("[^0-9]", "");
        String s = semester.replaceAll("[^0-9]", "");

        String sql = "SELECT t.*, c.Course_name, d.Dep_name " +
                "FROM timetable t " +
                "LEFT JOIN course c ON t.Course_code = c.Course_code " +
                "LEFT JOIN department d ON t.Department_id = d.Department_id " +
                "WHERE (t.Department_id = ? OR t.Department_id = 'D4') " +
                "AND SUBSTRING(t.Course_code, 4, 1) = ? AND SUBSTRING(t.Course_code, 5, 1) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptId);
            ps.setString(2, l);
            ps.setString(3, s);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timetable tt = new Timetable();
                tt.setTimeTableId(rs.getString("Timetable_id"));

                //show Course
                String cName = rs.getString("Course_name");
                String cCode = rs.getString("Course_code");
                tt.setCourseCode(cName != null ? cCode + " : " + cName : cCode);


                String dName = rs.getString("Dep_name");
                tt.setDepartmentId(dName != null ? dName : rs.getString("Department_id"));

                tt.setDay(rs.getString("Day"));
                tt.setVenue(rs.getString("Venue"));

                // show time
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
}