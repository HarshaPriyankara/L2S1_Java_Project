package Utils;

import java.util.Map;

class Eng2122MarkScheme extends CourseMarkScheme {
    Eng2122MarkScheme() {
        super("ENG2122", 40.0, 60.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double assignmentMarks = fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");
        double midTheoryMarks = weightedMark(marks, "Mid_theory", 20.0);

        return quizMarks + assignmentMarks + midTheoryMarks;
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

    ///  @author dilusha
    public String[] getAllowedMarkTypes() {
        String[] allowedTypes = types(
                "Quiz_1", "Quiz_2", "Quiz_3",
                "Assignment_1", "Assignment_2",
                "Mid_theory", "End_theory"
        );

        return allowedTypes;
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasAssignmentMarks = hasAllMarks(marks, "Assignment_1", "Assignment_2");
        boolean hasMidAndEndMarks = hasAllMarks(marks, "Mid_theory", "End_theory");

        return hasEnoughQuizMarks && hasAssignmentMarks && hasMidAndEndMarks;
    }
}
