package models;

import java.time.LocalDate;

public class MedicalRecord {

    private String    medicalId;
    private String    regNo;
    private LocalDate date;
    private String    reason;
    private String    document;
    private boolean   approved;
    private String    approvedBy;


    public MedicalRecord(String medicalId, String regNo, LocalDate date, String reason, String document) {
        this.medicalId = medicalId;
        this.regNo = regNo;
        this.date = date;
        this.reason = reason;
        this.document = document;
    }

    public  void submitMedical(){
        this.approved =false;
        this.approvedBy =null;
        System.out.println("Medical submitted for :"+regNo);
    }
}
