package Utils;

import java.util.Map;

class Ict2152MarkScheme extends CourseMarkScheme {
    Ict2152MarkScheme() {
        super("ICT2152", 40.0, 60.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double miniProjectMarks = weightedMark(marks, "Mini_project", 20.0);
        double assignmentMarks = fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");

        return quizMarks + miniProjectMarks + assignmentMarks;
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return endTheory(marks, 60.0);
    }

    public double getAssessmentWeight(String type) {
        if ("End_theory".equals(type)) {
            return 60.0;
        }

        return 0.0;
    }

    public String[] getAllowedMarkTypes() {
        return types("Quiz_1", "Quiz_2", "Quiz_3", "Mini_project", "Assignment_1", "Assignment_2", "End_theory");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasCaMarks = hasAllMarks(marks, "Mini_project", "Assignment_1", "Assignment_2");
        boolean hasEndMarks = hasAllMarks(marks, "End_theory");

        return hasEnoughQuizMarks && hasCaMarks && hasEndMarks;
    }
}
