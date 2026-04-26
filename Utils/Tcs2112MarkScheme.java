package Utils;

import java.util.Map;

class Tcs2112MarkScheme extends CourseMarkScheme {
    Tcs2112MarkScheme() {
        super("TCS2112", 30.0, 70.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double assignmentMarks = fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");
        double midTheoryMarks = weightedMark(marks, "Mid_theory", 10.0);

        return quizMarks + assignmentMarks + midTheoryMarks;
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return endTheory(marks, 70.0);
    }

    public double getAssessmentWeight(String type) {
        if ("End_theory".equals(type)) {
            return 70.0;
        }

        return 0.0;
    }

    public String[] getAllowedMarkTypes() {
        return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2", "Mid_theory", "End_theory");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasOtherMarks = hasAllMarks(marks, "Assignment_1", "Assignment_2", "Mid_theory", "End_theory");

        return hasEnoughQuizMarks && hasOtherMarks;
    }
}
