package Utils;

import java.util.Map;

class Ict2132MarkScheme extends CourseMarkScheme {
    Ict2132MarkScheme() {
        super("ICT2132", 40.0, 60.0);
    }

    protected double calculateCaMarks(Map<String, Double> marks) {
        double midPracticalMarks = weightedMark(marks, "Mid_practical", 10.0);
        double miniProjectMarks = weightedMark(marks, "Mini_project", 30.0);

        return midPracticalMarks + miniProjectMarks;
    }

    protected double calculateEndMarks(Map<String, Double> marks) {
        return endPractical(marks, 60.0);
    }

    public double getAssessmentWeight(String type) {
        if ("End_practical".equals(type)) {
            return 60.0;
        }

        return 0.0;
    }

    public String getDefaultEndMarkType() {
        return "End_practical";
    }

    ///  @author dilusha
    public String[] getAllowedMarkTypes() {
        return types("Mid_practical", "Mini_project", "End_practical");
    }

    public boolean hasCompleteMarks(Map<String, Double> marks) {
        boolean hasRequiredMarks = hasAllMarks(marks, "Mid_practical", "Mini_project", "End_practical");
        return hasRequiredMarks;
    }
}
