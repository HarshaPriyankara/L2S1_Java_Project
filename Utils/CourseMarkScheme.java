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
        String code = "";
        if (courseCode != null) {
            code = courseCode.trim().toUpperCase(Locale.ROOT);
        }

        switch (code) {
            case "ENG2122":
                return new Eng2122MarkScheme();
            case "ICT2113":
                return new Ict2113MarkScheme();
            case "ICT2122":
                return new Ict2122MarkScheme();
            case "ICT2132":
                return new Ict2132MarkScheme();
            case "ICT2142":
                return new Ict2142MarkScheme();
            case "ICT2152":
                return new Ict2152MarkScheme();
            case "TCS2112":
                return new Tcs2112MarkScheme();
            case "TCS2122":
                return new Tcs2122MarkScheme();
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
        return round(caWeight * 0.5);
    }

    public final double getEndPassMark() {
        return round(endWeight / 2.0);
    }

    ///  @author dilusha
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
        double calculatedMarks = calculateCaMarks(marks);

        if (calculatedMarks < 0.0) {
            calculatedMarks = 0.0;
        }

        if (calculatedMarks > caWeight) {
            calculatedMarks = caWeight;
        }

        return round(calculatedMarks);
    }

    public final double calculateEnd(Map<String, Double> marks) {
        double calculatedMarks = calculateEndMarks(marks);

        if (calculatedMarks < 0.0) {
            calculatedMarks = 0.0;
        }

        if (calculatedMarks > endWeight) {
            calculatedMarks = endWeight;
        }

        return round(calculatedMarks);
    }

    protected abstract double calculateCaMarks(Map<String, Double> marks);

    protected abstract double calculateEndMarks(Map<String, Double> marks);

    protected double topQuizAverage(Map<String, Double> marks, int count, double weight) {
        List<Double> values = valuesFor(marks, QUIZZES);
        values.sort(Collections.reverseOrder());

        if (values.isEmpty()) {
            return 0.0;
        }

        int selectedCount = Math.min(count, values.size());
        double total = 0.0;

        for (int i = 0; i < selectedCount; i++) {
            total += values.get(i);
        }

        double average = total / count;
        return weighted(average, weight);
    }

    protected double fixedAverage(Map<String, Double> marks, double weight, String... types) {
        if (types.length == 0) {
            return 0.0;
        }

        double total = 0.0;
        for (String type : types) {
            Double mark = getMark(marks, type);
            if (mark != null) {
                total += mark;
            }
        }

        double average = total / types.length;
        return weighted(average, weight);
    }

    protected double assignmentAverage(Map<String, Double> marks, double weight) {
        return averageWeighted(marks, ASSIGNMENTS, weight);
    }

    protected double weightedMark(Map<String, Double> marks, String type, double weight) {
        Double mark = getMark(marks, type);
        return weighted(mark, weight);
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

        if (values.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Double value : values) {
            total += value;
        }

        double average = total / values.size();
        return weighted(average, weight);
    }

    private List<Double> valuesFor(Map<String, Double> marks, String[] types) {
        List<Double> values = new ArrayList<>();

        if (marks == null) {
            return values;
        }

        for (String type : types) {
            Double value = getMark(marks, type);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
    }

    private Double getMark(Map<String, Double> marks, String type) {
        if (marks == null) {
            return null;
        }

        return marks.get(type);
    }

    private double weighted(Double mark, double weight) {
        if (mark == null) {
            return 0.0;
        }

        return mark * weight / 100.0;
    }

    protected static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    protected static String[] types(String... types) {
        return Arrays.copyOf(types, types.length);
    }

    protected boolean hasMark(Map<String, Double> marks, String type) {
        Double mark = getMark(marks, type);
        return mark != null;
    }

    protected boolean hasAllMarks(Map<String, Double> marks, String... types) {
        if (marks == null) {
            return false;
        }

        for (String type : types) {
            if (!hasMark(marks, type)) {
                return false;
            }
        }
        return true;
    }

    protected int countMarks(Map<String, Double> marks, String... types) {
        if (marks == null) {
            return 0;
        }

        int count = 0;
        for (String type : types) {
            if (hasMark(marks, type)) {
                count++;
            }
        }
        return count;
    }

    private static class DefaultScheme extends CourseMarkScheme {
        private DefaultScheme(String courseCode) {
            super(courseCode, 30.0, 70.0);
        }

        protected double calculateCaMarks(Map<String, Double> marks) {
            double quizMarks = topQuizAverage(marks, 2, 10.0);
            double assignmentMarks = assignmentAverage(marks, 10.0);
            double midMarks = theoryOrPracticalMid(marks, 10.0);

            return quizMarks + assignmentMarks + midMarks;
        }

        protected double calculateEndMarks(Map<String, Double> marks) {
            Double theory = null;
            Double practical = null;

            if (marks != null) {
                theory = marks.get("End_theory");
                practical = marks.get("End_practical");
            }

            if (theory != null && practical != null) {
                double theoryMarks = endTheory(marks, 35.0);
                double practicalMarks = endPractical(marks, 35.0);
                return theoryMarks + practicalMarks;
            }

            if (practical != null) {
                return endPractical(marks, 70.0);
            }

            return endTheory(marks, 70.0);
        }

        public double getAssessmentWeight(String type) {
            if ("End_theory".equals(type)) {
                return 70.0;
            }

            if ("End_practical".equals(type)) {
                return 70.0;
            }

            return 0.0;
        }
    }
}
