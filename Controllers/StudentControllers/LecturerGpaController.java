package Controllers.StudentControllers;

import DAO.MarkDAO;
import Utils.GpaCalculator;
import Utils.MarksCalculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LecturerGpaController {
    private final MarkDAO markDAO = new MarkDAO();

    public LecturerGpaResult loadGpaData(String studentId) {
        try {
            List<MarksCalculator.MarkBreakdown> allBreakdowns = markDAO.getAllStudentMarkBreakdowns();
            Map<String, List<MarksCalculator.MarkBreakdown>> breakdownsByStudent = groupByStudent(allBreakdowns);

            List<Object[]> summaryRows = new ArrayList<>();
            for (Map.Entry<String, List<MarksCalculator.MarkBreakdown>> entry : breakdownsByStudent.entrySet()) {
                GpaCalculator.GpaResult gpaResult = GpaCalculator.calculate(entry.getValue());
                summaryRows.add(buildSummaryRow(entry.getKey(), gpaResult));
            }

            String trimmedStudentId = "";
            if (studentId != null) {
                trimmedStudentId = studentId.trim();
            }

            if (trimmedStudentId.isEmpty()) {
                return new LecturerGpaResult(summaryRows, new ArrayList<>(), null, null, null);
            }

            List<MarksCalculator.MarkBreakdown> studentBreakdowns = markDAO.getStudentMarkBreakdowns(trimmedStudentId);
            if (studentBreakdowns.isEmpty()) {
                return new LecturerGpaResult(summaryRows, new ArrayList<>(), trimmedStudentId, null,
                        "No subject grade data found for " + trimmedStudentId + ".");
            }

            GpaCalculator.GpaResult studentGpa = GpaCalculator.calculate(studentBreakdowns);
            List<Object[]> detailRows = new ArrayList<>();
            for (MarksCalculator.MarkBreakdown breakdown : studentBreakdowns) {
                Object finalMarks = "Pending";
                Object gradePoint = "Pending";
                Object weightedPoint = "Pending";

                if (breakdown.hasMarks()) {
                    finalMarks = breakdown.getTotalMarks();
                    gradePoint = breakdown.getGradePoint();
                    weightedPoint = round(breakdown.getCredits() * breakdown.getGradePoint());
                }

                Object[] row = {
                        breakdown.getCourseCode(),
                        breakdown.getCourseName(),
                        breakdown.getCredits(),
                        finalMarks,
                        breakdown.getGrade(),
                        gradePoint,
                        weightedPoint
                };
                detailRows.add(row);
            }

            return new LecturerGpaResult(
                    summaryRows,
                    detailRows,
                    trimmedStudentId,
                    buildStudentSummary(studentGpa),
                    null
            );
        } catch (Exception ex) {
            return new LecturerGpaResult(null, null, null, null,
                    "Unable to load SGPA and CGPA data: " + ex.getMessage());
        }
    }

    private Map<String, List<MarksCalculator.MarkBreakdown>> groupByStudent(List<MarksCalculator.MarkBreakdown> breakdowns) {
        Map<String, List<MarksCalculator.MarkBreakdown>> grouped = new LinkedHashMap<>();
        if (breakdowns == null) {
            return grouped;
        }

        for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
            List<MarksCalculator.MarkBreakdown> studentBreakdowns = grouped.get(breakdown.getRegNo());

            if (studentBreakdowns == null) {
                studentBreakdowns = new ArrayList<>();
                grouped.put(breakdown.getRegNo(), studentBreakdowns);
            }

            studentBreakdowns.add(breakdown);
        }
        return grouped;
    }

    private Object[] buildSummaryRow(String regNo, GpaCalculator.GpaResult gpaResult) {
        Object sgpa = "Pending";
        Object cgpa = "Pending";

        if (gpaResult.isSgpaAvailable()) {
            sgpa = gpaResult.getSgpa();
        }

        if (gpaResult.isCgpaAvailable()) {
            cgpa = gpaResult.getCgpa();
        }

        return new Object[]{
                regNo,
                sgpa,
                cgpa
        };
    }

    private String buildStudentSummary(GpaCalculator.GpaResult gpaResult) {
        if (gpaResult.isSgpaAvailable() && gpaResult.isCgpaAvailable()) {
            return String.format("SGPA: %.2f | CGPA: %.2f", gpaResult.getSgpa(), gpaResult.getCgpa());
        }
        return "SGPA: Pending | CGPA: Pending";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
