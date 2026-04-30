package Utils;

import java.util.Map;

public class MarksCalculator {
    public static final String[] MARK_TYPES = {
            "Quiz_1", "Quiz_2", "Quiz_3",
            "Assignment_1", "Assignment_2",
            "Mini_project",
            "Mid_theory", "Mid_practical",
            "End_theory", "End_practical"
    };

    public static MarkBreakdown calculate(String regNo, String courseCode, String courseName,
                                          int credits, Map<String, Double> marks) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        double caMarks = scheme.calculateCA(marks);
        double endMarks = scheme.calculateEnd(marks);

        double totalMarks = caMarks + endMarks;
        if (totalMarks > 100.0) {
            totalMarks = 100.0;
        }
        totalMarks = round(totalMarks);

        String grade = calculateGrade(caMarks, endMarks, totalMarks, scheme);
        double gradePoint = GpaCalculator.getGradePoint(grade);
        boolean hasMarks = marks != null && !marks.isEmpty();
        boolean completeMarks = scheme.hasCompleteMarks(marks);

        return new MarkBreakdown(regNo, courseCode, courseName, credits,
                caMarks, endMarks, totalMarks, grade, gradePoint, hasMarks, completeMarks);
    }


    public static String calculateGrade(double caMarks, double endMarks, double totalMarks, CourseMarkScheme scheme) {
        boolean caFailed = caMarks < scheme.getCaPassMark();
        boolean endFailed = scheme.hasEndAssessment() && endMarks < scheme.getEndPassMark();

        if (caFailed && endFailed) {
            return "E(CA & ESA)";
        }

        if (caFailed) {
            return "E(CA)";
        }

        if (endFailed) {
            return "E(ESA)";
        }

        return calculateGrade(totalMarks);
    }

    public static String calculateGrade(double totalMarks) {
        if (totalMarks >= 85) {
            return "A+";
        }

        if (totalMarks >= 75) {
            return "A";
        }

        if (totalMarks >= 70) {
            return "A-";
        }

        if (totalMarks >= 65) {
            return "B+";
        }

        if (totalMarks >= 60) {
            return "B";
        }

        if (totalMarks >= 55) {
            return "B-";
        }

        if (totalMarks >= 50) {
            return "C+";
        }

        if (totalMarks >= 45) {
            return "C";
        }

        if (totalMarks >= 40) {
            return "C-";
        }

        if (totalMarks >= 35) {
            return "D";
        }

        return "E";
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static class MarkBreakdown {
        private final String regNo;
        private final String courseCode;
        private final String courseName;
        private final int credits;
        private final double caMarks;
        private final double endMarks;
        private final double totalMarks;
        private final String grade;
        private final double gradePoint;
        private final boolean hasMarks;
        private final boolean completeMarks;

        public MarkBreakdown(String regNo, String courseCode, String courseName, int credits,
                             double caMarks, double endMarks, double totalMarks,
                             String grade, double gradePoint, boolean hasMarks, boolean completeMarks) {
            this.regNo = regNo;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
            this.caMarks = caMarks;
            this.endMarks = endMarks;
            this.totalMarks = totalMarks;
            this.grade = grade;
            this.gradePoint = gradePoint;
            this.hasMarks = hasMarks;
            this.completeMarks = completeMarks;
        }

        public String getRegNo() {
            return regNo;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getCredits() {
            return credits;
        }

        public double getCaMarks() {
            return caMarks;
        }

        public double getEndMarks() {
            return endMarks;
        }

        public double getTotalMarks() {
            return totalMarks;
        }

        public String getGrade() {
            return grade;
        }

        public double getGradePoint() {
            return gradePoint;
        }

        public boolean hasMarks() {
            return hasMarks;
        }

        public boolean hasCompleteMarks() {
            return completeMarks;
        }
    }
}
