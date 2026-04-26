package Utils;

import java.util.Map;

class Ict2142MarkScheme extends CourseMarkScheme {
    Ict2142MarkScheme() {
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
