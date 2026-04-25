package Controllers.MarksControllers;

import DAO.MarkDAO;
import Utils.CourseMarkScheme;
import Utils.DBConnection;
import Utils.MarksCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarksManagementController {
    private final MarkDAO markDAO = new MarkDAO();

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

    public boolean hasEndAssessment(String courseCode) {
        return CourseMarkScheme.forCourse(courseCode).hasEndAssessment();
    }

    public MarksLoadResult loadEnrolledStudents(String selectedCourse, String selectedType) {
        if (!isAllowedMarkType(selectedCourse, selectedType)) {
            return new MarksLoadResult(null, null, null, null,
                    selectedType + " is not valid for " + selectedCourse);
        }

        Map<String, MarksCalculator.MarkBreakdown> breakdowns = loadBreakdownMap(selectedCourse);
        Map<String, Double> loadedEndMarks = new HashMap<>();
        List<String> studentIds = new ArrayList<>();
        List<Object[]> rows = new ArrayList<>();

        String query = "SELECT e.Reg_no, e.Course_code, m.Marks_value " +
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
                row.add(mark == null ? "" : mark);

                MarksCalculator.MarkBreakdown breakdown = breakdowns.get(regNo);
                addBreakdownColumns(row, breakdown);
                if (breakdown != null) {
                    loadedEndMarks.put(createStudentCourseKey(regNo, selectedCourse), breakdown.getEndMarks());
                }
                rows.add(row.toArray());
            }

            return new MarksLoadResult(studentIds, rows, loadedEndMarks, rows.size() + " students loaded", null);
        } catch (SQLException ex) {
            return new MarksLoadResult(null, null, null, null, "Error: " + ex.getMessage());
        }
    }

    public MarksSaveResult saveQuickMark(String regNo, String course, String type, String markText) {
        if (regNo == null || course == null || type == null) {
            return new MarksSaveResult(false, "Please load students first.");
        }

        if (!isAllowedMarkType(course, type)) {
            return new MarksSaveResult(false, type + " is not valid for " + course);
        }

        if (markText == null || markText.trim().isEmpty()) {
            return new MarksSaveResult(false, "Please enter a mark value.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            double markValue = Double.parseDouble(markText.trim());
            if (markValue < 0 || markValue > 100) {
                return new MarksSaveResult(false, "Marks must be between 0 and 100.");
            }

            saveSingleAssessmentMark(conn, regNo, course, type, markValue);
            return new MarksSaveResult(true, "Saved " + type + " for " + regNo);
        } catch (NumberFormatException ex) {
            return new MarksSaveResult(false, "Please enter a valid numeric mark.");
        } catch (SQLException ex) {
            return new MarksSaveResult(false, "Error saving mark: " + ex.getMessage());
        }
    }

    public MarksSaveResult saveAllMarks(List<Object[]> rows, Map<String, Double> loadedEndMarks) {
        if (rows == null || rows.isEmpty()) {
            return new MarksSaveResult(true, "Nothing to save.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            for (Object[] row : rows) {
                String regNo = String.valueOf(row[0]);
                String course = String.valueOf(row[1]);
                String type = String.valueOf(row[2]);
                String markStr = String.valueOf(row[3]).trim();
                String endStr = String.valueOf(row[5]).trim();
                CourseMarkScheme scheme = CourseMarkScheme.forCourse(course);

                if (!isAllowedMarkType(course, type)) {
                    return new MarksSaveResult(false, type + " is not valid for " + course);
                }

                if ((markStr.isEmpty() || "null".equalsIgnoreCase(markStr)) &&
                        (!scheme.hasEndAssessment() || isBlankValue(endStr))) {
                    continue;
                }

                if (!markStr.isEmpty() && !"null".equalsIgnoreCase(markStr)) {
                    double markValue = Double.parseDouble(markStr);
                    if (markValue < 0 || markValue > 100) {
                        return new MarksSaveResult(false, "Marks must be between 0 and 100 for " + regNo);
                    }
                    saveSingleAssessmentMark(conn, regNo, course, type, markValue);
                }

                if (scheme.hasEndAssessment() && !isBlankValue(endStr)) {
                    double endMark = Double.parseDouble(endStr);
                    if (endMark < 0 || endMark > scheme.getEndWeight()) {
                        return new MarksSaveResult(false, "END marks must be between 0 and "
                                + (int) scheme.getEndWeight() + " for " + regNo);
                    }
                    saveEndMarkIfChanged(conn, regNo, course, type, endMark, loadedEndMarks);
                }
            }

            return new MarksSaveResult(true, "All marks saved successfully!");
        } catch (NumberFormatException ex) {
            return new MarksSaveResult(false, "Please enter valid numeric marks only.");
        } catch (Exception ex) {
            return new MarksSaveResult(false, "Error saving marks: " + ex.getMessage());
        }
    }

    private Map<String, MarksCalculator.MarkBreakdown> loadBreakdownMap(String selectedCourse) {
        Map<String, MarksCalculator.MarkBreakdown> breakdowns = new HashMap<>();
        try {
            List<MarksCalculator.MarkBreakdown> courseBreakdowns = markDAO.getCourseMarkBreakdowns(selectedCourse);
            for (MarksCalculator.MarkBreakdown breakdown : courseBreakdowns) {
                breakdowns.put(breakdown.getRegNo(), breakdown);
            }
        } catch (SQLException ignored) {
        }
        return breakdowns;
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

    private void addBreakdownColumns(List<Object> row, MarksCalculator.MarkBreakdown breakdown) {
        if (breakdown == null) {
            row.add(0.0);
            row.add(0.0);
            row.add(0.0);
            row.add("E");
            row.add(0.0);
            return;
        }

        CourseMarkScheme scheme = CourseMarkScheme.forCourse(breakdown.getCourseCode());
        row.add(breakdown.getCaMarks());
        row.add(scheme.hasEndAssessment() ? breakdown.getEndMarks() : "N/A");
        row.add(breakdown.getTotalMarks());
        row.add(breakdown.getGrade());
        row.add(breakdown.getGradePoint());
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

    private void saveEndMarkIfChanged(Connection conn, String regNo, String course, String selectedType, double endMark,
                                      Map<String, Double> loadedEndMarks) throws SQLException {
        Double loadedEndMark = loadedEndMarks.get(createStudentCourseKey(regNo, course));
        if (loadedEndMark != null && Math.abs(loadedEndMark - endMark) < 0.01) {
            return;
        }

        CourseMarkScheme scheme = CourseMarkScheme.forCourse(course);
        String endType = selectedType != null && selectedType.startsWith("End_")
                ? selectedType
                : scheme.getDefaultEndMarkType();
        double endWeight = scheme.getAssessmentWeight(endType);
        if (endWeight <= 0.0) {
            endType = scheme.getDefaultEndMarkType();
            endWeight = scheme.getAssessmentWeight(endType);
        }
        if (endWeight <= 0.0) {
            return;
        }

        double rawEndMark = Math.round((endMark * 100.0 / endWeight) * 100.0) / 100.0;
        if (rawEndMark > 100) {
            rawEndMark = 100;
        }

        saveSingleAssessmentMark(conn, regNo, course, endType, rawEndMark);
    }

    private String createStudentCourseKey(String regNo, String course) {
        return regNo + "|" + course;
    }

    private boolean isBlankValue(String value) {
        return value == null
                || value.isEmpty()
                || "null".equalsIgnoreCase(value)
                || "N/A".equalsIgnoreCase(value);
    }
}
