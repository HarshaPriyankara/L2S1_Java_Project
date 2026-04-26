package Utils;

import java.util.Map;

class Ict2132MarkScheme extends CourseMarkScheme {
    Ict2132MarkScheme() {
        super("ICT2132", 40.0, 60.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        return weightedMark(marks, "Mid_practical", 10.0)
                + weightedMark(marks, "Mini_project", 30.0);
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return endPractical(marks, 60.0);
    }

    public double getAssessmentWeight(String type) {
        return "End_practical".equals(type) ? 60.0 : 0.0;
    }

    public String getDefaultEndMarkType() {
        return "End_practical";
    }

    public String[] getAllowedMarkTypes() {
        return types("Mid_practical", "Mini_project", "End_practical");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        return hasAllMarks(marks, "Mid_practical", "Mini_project", "End_practical");
    }
}
