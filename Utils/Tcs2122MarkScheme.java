package Utils;

import java.util.Map;

class Tcs2122MarkScheme extends CourseMarkScheme {
    Tcs2122MarkScheme() {
        super("TCS2122", 30.0, 0.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double quizMarks = topQuizAverage(marks, 2, 10.0);
        double assignmentMarks = fixedAverage(marks, 20.0, "Assignment_1", "Assignment_2");

        return quizMarks + assignmentMarks;
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return 0.0;
    }

    public String[] getAllowedMarkTypes() {
        return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasEnoughQuizMarks = countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2;
        boolean hasAssignmentMarks = hasAllMarks(marks, "Assignment_1", "Assignment_2");

        return hasEnoughQuizMarks && hasAssignmentMarks;
    }
}
