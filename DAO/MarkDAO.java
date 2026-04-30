package DAO;

import Utils.DBConnection;
import Utils.CourseMarkScheme;
import Utils.MarksCalculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MarkDAO {

    public List<MarksCalculator.MarkBreakdown> getCourseMarkBreakdowns(String courseCode) throws SQLException {
        String sql = "SELECT e.Reg_no, c.Course_code, c.Course_name, c.Credits, m.Marks_type, m.Marks_value " +
                "FROM enrollment e " +
                "JOIN course c ON e.Course_code = c.Course_code " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "WHERE e.Course_code = ? " +
                "ORDER BY e.Reg_no, m.Marks_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, courseCode);
            return buildBreakdowns(pst.executeQuery());
        }
    }

    public List<MarksCalculator.MarkBreakdown> getStudentMarkBreakdowns(String regNo) throws SQLException {
        String sql = "SELECT e.Reg_no, c.Course_code, c.Course_name, c.Credits, m.Marks_type, m.Marks_value " +
                "FROM enrollment e " +
                "JOIN course c ON e.Course_code = c.Course_code " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "WHERE e.Reg_no = ? " +
                "ORDER BY c.Course_code, m.Marks_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, regNo);
            return buildBreakdowns(pst.executeQuery());
        }
    }

    public MarksCalculator.MarkBreakdown getStudentCourseBreakdown(String regNo, String courseCode) throws SQLException {
        String sql = "SELECT e.Reg_no, c.Course_code, c.Course_name, c.Credits, m.Marks_type, m.Marks_value " +
                "FROM enrollment e " +
                "JOIN course c ON e.Course_code = c.Course_code " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "WHERE e.Reg_no = ? AND e.Course_code = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, regNo);
            pst.setString(2, courseCode);
            List<MarksCalculator.MarkBreakdown> results = buildBreakdowns(pst.executeQuery());

            if (results.isEmpty()) {
                return null;
            }

            return results.get(0);
        }
    }

    public List<MarksCalculator.MarkBreakdown> getAllStudentMarkBreakdowns() throws SQLException {
        String sql = "SELECT e.Reg_no, c.Course_code, c.Course_name, c.Credits, m.Marks_type, m.Marks_value " +
                "FROM enrollment e " +
                "JOIN course c ON e.Course_code = c.Course_code " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "ORDER BY e.Reg_no, c.Course_code, m.Marks_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            return buildBreakdowns(pst.executeQuery());
        }
    }

    ///  @author dilusha
    public Map<String, Map<String, Double>> getCourseMarksByStudent(String courseCode) throws SQLException {
        String sql = "SELECT e.Reg_no, m.Marks_type, m.Marks_value " +
                "FROM enrollment e " +
                "LEFT JOIN mark m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "WHERE e.Course_code = ? " +
                "ORDER BY e.Reg_no, m.Marks_type";

        Map<String, Map<String, Double>> groupedMarks = new LinkedHashMap<>();  //use linked hash map for keep order
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, courseCode);
            //use try for auto closing
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String regNo = rs.getString("Reg_no");  //get reg no
                    Map<String, Double> marks = groupedMarks.get(regNo);  //get details from linked hashed map by regno

                    if (marks == null) {
                        marks = new HashMap<>();
                        groupedMarks.put(regNo, marks); //insert to linked hash map
                    }

                    //if already have regno then insert into inner map
                    String markType = rs.getString("Marks_type");
                    if (markType != null) {
                        String normalizedMarkType = normalizeMarkType(markType);
                        double markValue = rs.getDouble("Marks_value");
                        marks.put(normalizedMarkType, markValue);
                    }
                }
            }
        }
        return groupedMarks;
    }

    ///  @author dilusha
    public Map<String, Double> getStudentCourseMarks(String regNo, String courseCode) throws SQLException {
        Map<String, Map<String, Double>> groupedMarks = getCourseMarksByStudent(courseCode);
        Map<String, Double> marks = groupedMarks.get(regNo);

        if (marks == null) {
            return new HashMap<>();
        }

        return new HashMap<>(marks);
    }

    private List<MarksCalculator.MarkBreakdown> buildBreakdowns(ResultSet rs) throws SQLException {
        Map<String, StudentCourseMarks> groupedMarks = new LinkedHashMap<>();

        while (rs.next()) {
            String regNo = rs.getString("Reg_no");
            String courseCode = rs.getString("Course_code");
            String key = regNo + "|" + courseCode;
            StudentCourseMarks group = groupedMarks.get(key);

            if (group == null) {
                group = new StudentCourseMarks(
                        regNo,
                        courseCode,
                        rs.getString("Course_name"),
                        rs.getInt("Credits")
                );
                groupedMarks.put(key, group);
            }

            String markType = rs.getString("Marks_type");
            if (markType != null) {
                String normalizedMarkType = normalizeMarkType(markType);
                double markValue = rs.getDouble("Marks_value");
                group.marks.put(normalizedMarkType, markValue);
            }
        }

        List<MarksCalculator.MarkBreakdown> result = new ArrayList<>();
        for (StudentCourseMarks group : groupedMarks.values()) {
            result.add(MarksCalculator.calculate(
                    group.regNo,
                    group.courseCode,
                    group.courseName,
                    group.credits,
                    group.marks
            ));
        }
        return result;
    }

    private static class StudentCourseMarks {
        private final String regNo;
        private final String courseCode;
        private final String courseName;
        private final int credits;
        private final Map<String, Double> marks = new HashMap<>();

        private StudentCourseMarks(String regNo, String courseCode, String courseName, int credits) {
            this.regNo = regNo;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
        }
    }

    /// @author dilusha
    private String normalizeMarkType(String markType) {
        if (markType == null) {
            return "";
        }

        String normalized = markType.trim().replace(" ", "_").toLowerCase();
        switch (normalized) {
            case "quiz_1":
                return "Quiz_1";
            case "quiz_2":
                return "Quiz_2";
            case "quiz_3":
                return "Quiz_3";
            case "assignment_1":
                return "Assignment_1";
            case "assignment_2":
                return "Assignment_2";
            case "mini_project":
                return "Mini_project";
            case "mid_theory":
                return "Mid_theory";
            case "mid_practical":
                return "Mid_practical";
            case "end_theory":
                return "End_theory";
            case "end_practical":
                return "End_practical";
            default:
                return markType.trim();
        }
    }

    // Marks Add
    public boolean addMarks(String regNo, String course, String type, double marks) {
        String formattedType = type.replace(" ", "_");
        if (!isAllowedMarkType(course, formattedType)) {
            return false;
        }

        String sql = "INSERT INTO mark (Reg_no, Course_code, Marks_type, Marks_value) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Marks_value = VALUES(Marks_value), Marks_type = VALUES(Marks_type)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, formattedType); // Formatted type එක මෙතනට
            pst.setDouble(4, marks);

            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Marks Update
    public boolean updateMarks(int id, String regNo, String course, String type, double marks) {
        String sql = "UPDATE mark SET Reg_no=?, Course_code=?, Marks_type=?, Marks_value=? WHERE Mark_id=?";

        String formattedType = type.replace(" ", "_");
        if (!isAllowedMarkType(course, formattedType)) {
            return false;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, formattedType); // Formatted type එක මෙතනට
            pst.setDouble(4, marks);
            pst.setInt(5, id);

            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Marks Delete
    public boolean deleteMarks(int id) {
        String sql = "DELETE FROM mark WHERE Mark_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isAllowedMarkType(String course, String type) {
        for (String allowedType : CourseMarkScheme.forCourse(course).getAllowedMarkTypes()) {
            if (allowedType.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
