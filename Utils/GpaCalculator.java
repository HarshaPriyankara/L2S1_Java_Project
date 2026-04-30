package Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GpaCalculator {
    public static final String ENGLISH_COURSE_CODE = "ENG2122";
    public static final String[] SEMESTER_COURSES = {
            "ENG2122", "ICT2113", "ICT2122", "ICT2132",
            "ICT2142", "ICT2152", "TCS2112", "TCS2122"
    };

    public static double getGradePoint(String grade) {
        String normalizedGrade = normalizeGrade(grade);

        switch (normalizedGrade) {
            case "A+":
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D":
                return 1.3;
            default:
                return 0.0;
        }
    }

    /**
     *
     * @Author isuru */
    public static GpaResult calculate(List<MarksCalculator.MarkBreakdown> breakdowns) {
        Map<String, MarksCalculator.MarkBreakdown> marksByCourse = new HashMap<>();
        if (breakdowns != null) {
            for (MarksCalculator.MarkBreakdown breakdown : breakdowns) {
                marksByCourse.put(breakdown.getCourseCode(), breakdown);
            }
        }

        double semesterWeightedPoints = 0.0;
        double semesterCredits = 0.0;
        for (String courseCode : SEMESTER_COURSES) {
            MarksCalculator.MarkBreakdown breakdown = marksByCourse.get(courseCode);
            if (!hasValidSubjectResult(breakdown)) {
                return new GpaResult(false, false, 0.0, 0.0);
            }

            double subjectWeightedPoints = breakdown.getCredits() * breakdown.getGradePoint();
            semesterWeightedPoints += subjectWeightedPoints;
            semesterCredits += breakdown.getCredits();
        }

        double cgpaWeightedPoints = 0.0;
        double cgpaCredits = 0.0;
        for (String courseCode : SEMESTER_COURSES) {
            if (ENGLISH_COURSE_CODE.equals(courseCode)) {
                continue;
            }
            MarksCalculator.MarkBreakdown breakdown = marksByCourse.get(courseCode);

            double subjectWeightedPoints = breakdown.getCredits() * breakdown.getGradePoint();
            cgpaWeightedPoints += subjectWeightedPoints;
            cgpaCredits += breakdown.getCredits();
        }

        boolean sgpaAvailable = semesterCredits > 0.0;
        boolean cgpaAvailable = cgpaCredits > 0.0;
        double sgpa = 0.0;
        double cgpa = 0.0;

        if (sgpaAvailable) {
            sgpa = round(semesterWeightedPoints / semesterCredits);
        }

        if (cgpaAvailable) {
            cgpa = round(cgpaWeightedPoints / cgpaCredits);
        }

        return new GpaResult(sgpaAvailable, cgpaAvailable, sgpa, cgpa);
    }

    private static boolean hasValidSubjectResult(MarksCalculator.MarkBreakdown breakdown) {
        if (breakdown == null) {
            return false;
        }

        return breakdown.hasCompleteMarks();
    }

    private static String normalizeGrade(String grade) {
        if (grade == null) {
            return "";
        }

        int detailStart = grade.indexOf('(');

        if (detailStart >= 0) {
            return grade.substring(0, detailStart);
        }

        return grade;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static class GpaResult {
        private final boolean sgpaAvailable;
        private final boolean cgpaAvailable;
        private final double sgpa;
        private final double cgpa;

        public GpaResult(boolean sgpaAvailable, boolean cgpaAvailable, double sgpa, double cgpa) {
            this.sgpaAvailable = sgpaAvailable;
            this.cgpaAvailable = cgpaAvailable;
            this.sgpa = sgpa;
            this.cgpa = cgpa;
        }

        public boolean isSgpaAvailable() {
            return sgpaAvailable;
        }

        public boolean isCgpaAvailable() {
            return cgpaAvailable;
        }

        public double getSgpa() {
            return sgpa;
        }

        public double getCgpa() {
            return cgpa;
        }
    }
}
