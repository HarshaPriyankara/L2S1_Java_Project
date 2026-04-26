package Controllers.MarksControllers;

import Utils.CourseMarkScheme;
import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarksManagementController {
    public List<String> loadLecturerCourses(String lecturerId) throws SQLException {
        List<String> courses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT Course_code FROM course WHERE Lecturer_in_charge = ?")) {
            pst.setString(1, lecturerId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("Course_code"));
            }
        }
        return courses;
    }

    public String[] getAllowedMarkTypes(String courseCode) {
        return CourseMarkScheme.forCourse(courseCode).getAllowedMarkTypes();
    }

    public MarksLoadResult loadEnrolledStudents(String selectedCourse, String selectedType) {
        if (!isAllowedMarkType(selectedCourse, selectedType)) {
            return new MarksLoadResult(null, null, null,
                    selectedType + " is not valid for " + selectedCourse);
        }

        List<String> studentIds = new ArrayList<>();
        List<Object[]> rows = new ArrayList<>();

        String query = "SELECT e.Reg_no, e.Course_code, m.Mark_id, m.Marks_value " +
                "FROM enrollment e " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code " +
                "AND LOWER(REPLACE(TRIM(m.Marks_type), ' ', '_')) = LOWER(?) " +
                "WHERE e.Course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, selectedType);
            pst.setString(2, selectedCourse);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String regNo = rs.getString("Reg_no");
                studentIds.add(regNo);

                List<Object> row = new ArrayList<>();
                row.add(regNo);
                row.add(selectedCourse);
                row.add(selectedType);

                Object mark = rs.getObject("Marks_value");
                row.add(formatMarkForEntry(mark));
                rows.add(row.toArray());
            }

            return new MarksLoadResult(studentIds, rows, rows.size() + " students loaded", null);
        } catch (SQLException ex) {
            return new MarksLoadResult(null, null, null, "Error: " + ex.getMessage());
        }
    }

    public MarksSaveResult saveAllMarks(List<Object[]> rows) {
        if (rows == null || rows.isEmpty()) {
            return new MarksSaveResult(true, "Nothing to save.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            for (Object[] row : rows) {
                String regNo = String.valueOf(row[0]);
                String course = String.valueOf(row[1]);
                String type = String.valueOf(row[2]);
                String markStr = String.valueOf(row[3]).trim();

                if (!isAllowedMarkType(course, type)) {
                    return new MarksSaveResult(false, type + " is not valid for " + course);
                }

                if (markStr.isEmpty() || "null".equalsIgnoreCase(markStr)) {
                    continue;
                }

                double markValue = Double.parseDouble(markStr);
                if (markValue < 0 || markValue > 100) {
                    return new MarksSaveResult(false, "Marks must be between 0 and 100 for " + regNo);
                }
                saveSingleAssessmentMark(conn, regNo, course, type, markValue);
            }

            return new MarksSaveResult(true, "All marks saved successfully!");
        } catch (NumberFormatException ex) {
            return new MarksSaveResult(false, "Please enter valid numeric marks only.");
        } catch (Exception ex) {
            return new MarksSaveResult(false, "Error saving marks: " + ex.getMessage());
        }
    }

    private boolean isAllowedMarkType(String course, String type) {
        if (course == null || type == null) return false;
        for (String allowedType : CourseMarkScheme.forCourse(course).getAllowedMarkTypes()) {
            if (allowedType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    private Object formatMarkForEntry(Object mark) {
        if (mark == null) {
            return "";
        }

        if (mark instanceof Number number && Math.abs(number.doubleValue()) < 0.001) {
            return "";
        }

        String markText = String.valueOf(mark).trim();
        return "0".equals(markText) || "0.0".equals(markText) || "0.00".equals(markText) ? "" : mark;
    }

    private void saveSingleAssessmentMark(Connection conn, String regNo, String course,
                                          String type, double markValue) throws SQLException {
        String checkQuery = "SELECT Mark_id FROM MARK WHERE Reg_no=? AND Course_code=? " +
                "AND LOWER(REPLACE(TRIM(Marks_type), ' ', '_')) = LOWER(?)";
        try (PreparedStatement checkPst = conn.prepareStatement(checkQuery)) {
            checkPst.setString(1, regNo);
            checkPst.setString(2, course);
            checkPst.setString(3, type);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                String updateQuery = "UPDATE MARK SET Marks_value=?, Marks_type=? WHERE Mark_id=?";
                try (PreparedStatement upPst = conn.prepareStatement(updateQuery)) {
                    upPst.setDouble(1, markValue);
                    upPst.setString(2, type);
                    upPst.setInt(3, rs.getInt("Mark_id"));
                    upPst.executeUpdate();
                }
            } else {
                String insertQuery = "INSERT INTO MARK (Reg_no, Course_code, Marks_type, Marks_value) VALUES (?, ?, ?, ?)";
                try (PreparedStatement inPst = conn.prepareStatement(insertQuery)) {
                    inPst.setString(1, regNo);
                    inPst.setString(2, course);
                    inPst.setString(3, type);
                    inPst.setDouble(4, markValue);
                    inPst.executeUpdate();
                }
            }
        }
    }

}
