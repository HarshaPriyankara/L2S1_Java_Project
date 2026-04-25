package Utils;

import java.util.List;
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
        double totalMarks = round(Math.min(100.0, caMarks + endMarks));
        String grade = calculateGrade(caMarks, endMarks, totalMarks, scheme);
        double gpa = calculateGradePoint(grade);

        return new MarkBreakdown(regNo, courseCode, courseName, credits,
                caMarks, endMarks, totalMarks, grade, gpa);
    }

    public static String calculateGrade(double caMarks, double endMarks, double totalMarks) {
        return calculateGrade(caMarks, endMarks, totalMarks, CourseMarkScheme.forCourse(null));
    }

    public static String calculateGrade(double caMarks, double endMarks, double totalMarks, CourseMarkScheme scheme) {
        boolean caFailed = caMarks < scheme.getCaPassMark();
        boolean endFailed = scheme.hasEndAssessment() && endMarks < scheme.getEndPassMark();

        if (caFailed && endFailed) return "E(CA & ESA)";
        if (caFailed) return "E(CA)";
        if (endFailed) return "E(ESA)";

        return calculateGrade(totalMarks);
    }

    public static String calculateGrade(double totalMarks) {
        if (totalMarks >= 85) return "A+";
        if (totalMarks >= 75) return "A";
        if (totalMarks >= 70) return "A-";
        if (totalMarks >= 65) return "B+";
        if (totalMarks >= 60) return "B";
        if (totalMarks >= 55) return "B-";
        if (totalMarks >= 50) return "C+";
        if (totalMarks >= 45) return "C";
        if (totalMarks >= 40) return "C-";
        if (totalMarks >= 35) return "D";
        return "E";
    }

    public static double calculateGradePoint(String grade) {
        switch (grade) {
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
                return 1.0;
            default:
                return 0.0;
        }
    }

    public static double calculateSGPA(List<MarkBreakdown> marks) {
        double weightedPoints = 0.0;
        int totalCredits = 0;

        for (MarkBreakdown mark : marks) {
            if (mark.getCredits() <= 0) continue;
            weightedPoints += mark.getGpa() * mark.getCredits();
            totalCredits += mark.getCredits();
        }

        return totalCredits == 0 ? 0.0 : round(weightedPoints / totalCredits);
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
        private final double gpa;

        public MarkBreakdown(String regNo, String courseCode, String courseName, int credits,
                             double caMarks, double endMarks, double totalMarks,
                             String grade, double gpa) {
            this.regNo = regNo;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
            this.caMarks = caMarks;
            this.endMarks = endMarks;
            this.totalMarks = totalMarks;
            this.grade = grade;
            this.gpa = gpa;
        }

        public String getRegNo() { return regNo; }
        public String getCourseCode() { return courseCode; }
        public String getCourseName() { return courseName; }
        public int getCredits() { return credits; }
        public double getCaMarks() { return caMarks; }
        public double getEndMarks() { return endMarks; }
        public double getTotalMarks() { return totalMarks; }
        public String getGrade() { return grade; }
        public double getGpa() { return gpa; }
    }
}
