package Utils;

import java.util.Map;

class Eng2122MarkScheme extends CourseMarkScheme {
    Eng2122MarkScheme() {
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
