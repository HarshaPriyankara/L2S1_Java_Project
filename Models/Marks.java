package models;

import java.util.ArrayList;
import java.util.List;

public class Marks {

    //fields
    private String markId;
    private String regNo;
    private String courseCode;
    private String examType;
    private double marksValue;
    private double totalMarks;
    private String sessionType;
    private String uploadedBy;

    //constructors
    public Marks() {
    }

    public Marks(String markId, String regNo, String courseCode,
                 String examType, double marksValue, double totalMarks,
                 String sessionType, String uploadedBy) {
        this.markId      = markId;
        this.regNo       = regNo;
        this.courseCode  = courseCode;
        this.examType    = examType;
        this.marksValue  = marksValue;
        this.totalMarks  = totalMarks;
        this.sessionType = sessionType;
        this.uploadedBy  = uploadedBy;
    }


    public void uploadMarks() {
        System.out.println("[Marks] Uploaded: " + examType
                + " = " + marksValue + " for " + regNo);
    }

    public double getCAMarks(List<Marks> caRecords) {
        if (caRecords == null || caRecords.isEmpty()) return 0.0;
        double sum = 0;
        for (Marks m : caRecords) sum += m.marksValue;
        return Math.round(sum / caRecords.size() * 100.0) / 100.0;
    }

    public double getFinalMarks(double caAverage, double endTheory, double endPractical) {
        double endAvg = endPractical > 0
                ? (endTheory + endPractical) / 2.0
                : endTheory;
        double final_ = (caAverage * 0.4) + (endAvg * 0.6);
        return Math.round(final_ * 100.0) / 100.0;
    }

    public boolean checkCAEligibility(double caAverage) {
        return caAverage >= 40.0;
    }

    public List<String> getBatchSummary() {
        return new ArrayList<>();
    }


}
