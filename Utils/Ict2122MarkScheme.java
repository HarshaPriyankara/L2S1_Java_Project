package Utils;

import java.util.Map;

class Ict2122MarkScheme extends CourseMarkScheme {
    Ict2122MarkScheme() {
        super("ICT2122", 30.0, 70.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double midTheoryMarks = weightedMark(marks, "Mid_theory", 20.0);

        return quizMarks + midTheoryMarks;
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
    ///  @author dilusha
    public String[] getAllowedMarkTypes() {
        return types("Quiz_1", "Quiz_2", "Quiz_3", "Mid_theory", "End_theory");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasOtherMarks = hasAllMarks(marks, "Mid_theory", "End_theory");

        return hasEnoughQuizMarks && hasOtherMarks;
    }
}
