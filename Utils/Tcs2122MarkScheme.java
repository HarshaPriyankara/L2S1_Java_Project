package Utils;

import java.util.Map;

class Tcs2122MarkScheme extends CourseMarkScheme {
    Tcs2122MarkScheme() {
        super("TCS2122", 30.0, 0.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        return topQuizAverage(marks, 2, 10.0)
                + fixedAverage(marks, 20.0, "Assignment_1", "Assignment_2");
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return 0.0;
    }

    public String[] getAllowedMarkTypes() {
        return types("Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        return countMarks(marks, "Quiz_1", "Quiz_2", "Quiz_3") >= 2
                && hasAllMarks(marks, "Assignment_1", "Assignment_2");
    }
}
