package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class CourseMarkScheme {
    private static final String[] QUIZZES = {"Quiz_1", "Quiz_2", "Quiz_3"};
    private static final String[] ASSIGNMENTS = {"Assignment_1", "Assignment_2"};

    private final String courseCode;
    private final double caWeight;
    private final double endWeight;

    protected CourseMarkScheme(String courseCode, double caWeight, double endWeight) {
        this.courseCode = courseCode;
        this.caWeight = caWeight;
        this.endWeight = endWeight;
    }

    public static CourseMarkScheme forCourse(String courseCode) {
        String code = courseCode == null ? "" : courseCode.trim().toUpperCase(Locale.ROOT);
        switch (code) {
            case "ENG2122":
                return new Eng2122Scheme();
            case "ICT2113":
                return new Ict2113Scheme();
            case "ICT2122":
                return new Ict2122Scheme();
            case "ICT2132":
                return new Ict2132Scheme();
            case "ICT2142":
                return new Ict2142Scheme();
            case "ICT2152":
                return new Ict2152Scheme();
            case "TCS2112":
                return new Tcs2112Scheme();
            case "TCS2122":
                return new Tcs2122Scheme();
            default:
                return new DefaultScheme(code);
        }
    }

    public final String getCourseCode() {
        return courseCode;
    }

    public final double getCaWeight() {
        return caWeight;
    }

    public final double getEndWeight() {
        return endWeight;
    }

    public final double getCaPassMark() {
        return round(caWeight * 0.4);
    }

    public final double getEndPassMark() {
        return round(endWeight / 2.0);
    }

    public final boolean hasEndAssessment() {
        return endWeight > 0.0;
    }

    public double getAssessmentWeight(String type) {
        return 0.0;
    }

    public String[] getAllowedMarkTypes() {
        return MarksCalculator.MARK_TYPES;
    }

    public String getDefaultEndMarkType() {
        return "End_theory";
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        return hasAllMarks(marks, getAllowedMarkTypes());
    }

    public final double calculateCA(Map<String, Double> marks) {
        return round(Math.min(caWeight, Math.max(0.0, calculateCaMarks(marks))));
    }

    public final double calculateEnd(Map<String, Double> marks) {
        return round(Math.min(endWeight, Math.max(0.0, calculateEndMarks(marks))));
    }

    protected abstract double calculateCaMarks(Map<String, Double> marks);

    protected abstract double calculateEndMarks(Map<String, Double> marks);

    protected double topQuizAverage(Map<String, Double> marks, int count, double weight) {
        List<Double> values = valuesFor(marks, QUIZZES);
        values.sort(Collections.reverseOrder());
        if (values.isEmpty()) return 0.0;

        int selectedCount = Math.min(count, values.size());
        double total = 0.0;
        for (int i = 0; i < selectedCount; i++) {
            total += values.get(i);
        }
        return weighted(total / count, weight);
    }

    protected double fixedAverage(Map<String, Double> marks, double weight, String... types) {
        if (types.length == 0) return 0.0;

        double total = 0.0;
        for (String type : types) {
            Double mark = getMark(marks, type);
            if (mark != null) {
                total += mark;
            }
        }
        return weighted(total / types.length, weight);
    }

    protected double allQuizAverage(Map<String, Double> marks, double weight) {
        return averageWeighted(marks, QUIZZES, weight);
    }

    protected double assignmentAverage(Map<String, Double> marks, double weight) {
        return averageWeighted(marks, ASSIGNMENTS, weight);
    }

    protected double combinedAverage(Map<String, Double> marks, double weight, String... types) {
        return averageWeighted(marks, types, weight);
    }

    protected double weightedMark(Map<String, Double> marks, String type, double weight) {
        return weighted(getMark(marks, type), weight);
    }

    protected double theoryOrPracticalMid(Map<String, Double> marks, double weight) {
        return averageWeighted(marks, new String[]{"Mid_theory", "Mid_practical"}, weight);
    }

    protected double endTheory(Map<String, Double> marks, double weight) {
        return weightedMark(marks, "End_theory", weight);
    }

    protected double endPractical(Map<String, Double> marks, double weight) {
        return weightedMark(marks, "End_practical", weight);
    }

    private double averageWeighted(Map<String, Double> marks, String[] types, double weight) {
        List<Double> values = valuesFor(marks, types);
        if (values.isEmpty()) return 0.0;

        double total = 0.0;
        for (Double value : values) {
            total += value;
        }
        return weighted(total / values.size(), weight);
    }

    private List<Double> valuesFor(Map<String, Double> marks, String[] types) {
        List<Double> values = new ArrayList<>();
        if (marks == null) return values;

        for (String type : types) {
            Double value = getMark(marks, type);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    private Double getMark(Map<String, Double> marks, String type) {
        return marks == null ? null : marks.get(type);
    }

    private double weighted(Double mark, double weight) {
        return mark == null ? 0.0 : mark * weight / 100.0;
    }

    protected static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    protected static String[] types(String... types) {
        return Arrays.copyOf(types, types.length);
    }

    protected boolean hasMark(Map<String, Double> marks, String type) {
        return getMark(marks, type) != null;
    }

    protected boolean hasAllMarks(Map<String, Double> marks, String... types) {
        if (marks == null) return false;
        for (String type : types) {
            if (!hasMark(marks, type)) {
                return false;
            }
        }
        return true;
    }

    protected int countMarks(Map<String, Double> marks, String... types) {
        if (marks == null) return 0;
        int count = 0;
        for (String type : types) {
            if (hasMark(marks, type)) {
                count++;
            }
        }
        return count;
    }

    private static class Eng2122Scheme extends CourseMarkScheme {
        private Eng2122Scheme() {
            super("ENG2122", 40.0, 60.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2")
                    + weightedMark(marks, "Mid_theory", 20.0);
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 60.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_theory".equals(type) ? 60.0 : 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2",
                    "Mid_theory", "End_theory");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Assignment_1", "Assignment_2")
                    && hasAllMarks(marks, "Mid_theory", "End_theory");
        }
    }

    private static class Ict2113Scheme extends CourseMarkScheme {
        private Ict2113Scheme() {
            super("ICT2113", 40.0, 60.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + weightedMark(marks, "Mid_practical", 20.0)
                    + fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 40.0)
                    + endPractical(marks, 20.0);
        }

        public double getAssessmentWeight(String type) {
            if ("End_theory".equals(type)) return 40.0;
            if ("End_practical".equals(type)) return 20.0;
            return 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3",
                    "Assignment_1", "Assignment_2", "Mid_practical", "End_theory", "End_practical");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Assignment_1", "Assignment_2", "Mid_practical", "End_theory", "End_practical");
        }
    }

    private static class Ict2122Scheme extends CourseMarkScheme {
        private Ict2122Scheme() {
            super("ICT2122", 30.0, 70.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + weightedMark(marks, "Mid_theory", 20.0);
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 70.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_theory".equals(type) ? 70.0 : 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Mid_theory", "End_theory");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Mid_theory", "End_theory");
        }
    }

    private static class Ict2132Scheme extends CourseMarkScheme {
        private Ict2132Scheme() {
            super("ICT2132", 40.0, 60.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return weightedMark(marks, "Mid_practical", 10.0)
                    + weightedMark(marks, "Mini_project", 30.0);
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endPractical(marks, 60.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_practical".equals(type) ? 60.0 : 0.0;
        }

        public String getDefaultEndMarkType() {
            return "End_practical";
        }

        public String[] getAllowedMarkTypes() {
            return types("Mid_practical", "Mini_project", "End_practical");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return hasAllMarks(marks, "Mid_practical", "Mini_project", "End_practical");
        }
    }

    private static class Ict2142Scheme extends CourseMarkScheme {
        private Ict2142Scheme() {
            super("ICT2142", 30.0, 70.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + fixedAverage(marks, 20.0, "Assignment_1", "Assignment_2");
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 70.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_theory".equals(type) ? 70.0 : 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2", "End_theory");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Assignment_1", "Assignment_2", "End_theory");
        }
    }

    private static class Ict2152Scheme extends CourseMarkScheme {
        private Ict2152Scheme() {
            super("ICT2152", 40.0, 60.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + weightedMark(marks, "Mini_project", 20.0)
                    + fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 60.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_theory".equals(type) ? 60.0 : 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Mini_project", "Assignment_1", "Assignment_2", "End_theory");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Mini_project", "Assignment_1", "Assignment_2")
                    && hasAllMarks(marks, "End_theory");
        }
    }

    private static class Tcs2112Scheme extends CourseMarkScheme {
        private Tcs2112Scheme() {
            super("TCS2112", 30.0, 70.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2")
                    + weightedMark(marks, "Mid_theory", 10.0);
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return endTheory(marks, 70.0);
        }

        public double getAssessmentWeight(String type) {
            return "End_theory".equals(type) ? 70.0 : 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2", "Mid_theory", "End_theory");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Assignment_1", "Assignment_2", "Mid_theory", "End_theory");
        }
    }

    private static class Tcs2122Scheme extends CourseMarkScheme {
        private Tcs2122Scheme() {
            super("TCS2122", 30.0, 0.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + fixedAverage(marks, 20.0, "Assignment_1", "Assignment_2");
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            return 0.0;
        }

        public String[] getAllowedMarkTypes() {
            return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2");
        }

        public boolean hasCompleteMarks(Map<String, Double> marks) {
            return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                    && hasAllMarks(marks, "Assignment_1", "Assignment_2");
        }
    }

    private static class DefaultScheme extends CourseMarkScheme {
        private DefaultScheme(String courseCode) {
            super(courseCode, 30.0, 70.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            return topQuizAverage(marks, 2, 10.0)
                    + assignmentAverage(marks, 10.0)
                    + theoryOrPracticalMid(marks, 10.0);
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            Double theory = marks == null ? null : marks.get("End_theory");
            Double practical = marks == null ? null : marks.get("End_practical");
            if (theory != null && practical != null) {
                return endTheory(marks, 35.0) + endPractical(marks, 35.0);
            }
            if (practical != null) {
                return endPractical(marks, 70.0);
            }
            return endTheory(marks, 70.0);
        }

        public double getAssessmentWeight(String type) {
            if ("End_theory".equals(type)) return 70.0;
            if ("End_practical".equals(type)) return 70.0;
            return 0.0;
        }
    }
}
