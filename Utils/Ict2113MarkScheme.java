package Utils;

import java.util.Map;

class Ict2113MarkScheme extends CourseMarkScheme {
    Ict2113MarkScheme() {
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
