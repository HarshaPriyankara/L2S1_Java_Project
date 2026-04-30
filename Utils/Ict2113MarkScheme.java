package Utils;

import java.util.Map;

class Ict2113MarkScheme extends CourseMarkScheme {
    Ict2113MarkScheme() {
        super("ICT2113", 40.0, 60.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double midPracticalMarks = weightedMark(marks, "Mid_practical", 20.0);
        double assignmentMarks = fixedAverage(marks, 10.0, "Assignment_1", "Assignment_2");

        return quizMarks + midPracticalMarks + assignmentMarks;
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        double theoryMarks = endTheory(marks, 40.0);
        double practicalMarks = endPractical(marks, 20.0);

        return theoryMarks + practicalMarks;
    }

    public double getAssessmentWeight(String type) {
        if ("End_theory".equals(type)) {
            return 40.0;
        }

        if ("End_practical".equals(type)) {
            return 20.0;
        }

        return 0.0;
    }

    ///  @author dilusha
    public String[] getAllowedMarkTypes() {
        String[] allowedTypes = types(
                "Quiz_1", "Quiz_2", "Quiz_3",
                "Assignment_1", "Assignment_2",
                "Mid_practical",
                "End_theory", "End_practical"
        );

        return allowedTypes;
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasAssignmentMarks = hasAllMarks(marks, "Assignment_1", "Assignment_2");
        boolean hasMidPracticalMarks = hasAllMarks(marks, "Mid_practical");
        boolean hasEndMarks = hasAllMarks(marks, "End_theory", "End_practical");

        return hasEnoughQuizMarks && hasAssignmentMarks && hasMidPracticalMarks && hasEndMarks;
    }
}
