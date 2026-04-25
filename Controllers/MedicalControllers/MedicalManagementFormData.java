package Controllers.MedicalControllers;

public class MedicalManagementFormData {
    private final String medicalId;
    private final String regNo;
    private final String sessionDate;
    private final String sessionType;
    private final String examCourse;
    private final String reason;
    private final boolean approved;

    public MedicalManagementFormData(String medicalId, String regNo, String sessionDate, String sessionType,
                                     String examCourse, String reason, boolean approved) {
        this.medicalId = normalize(medicalId);
        this.regNo = normalize(regNo);
        this.sessionDate = normalize(sessionDate);
        this.sessionType = normalize(sessionType);
        this.examCourse = normalize(examCourse);
        this.reason = normalize(reason);
        this.approved = approved;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public String getSessionType() {
        return sessionType;
    }

    public String getExamCourse() {
        return examCourse;
    }

    public String getReason() {
        return reason;
    }

    public boolean isApproved() {
        return approved;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
