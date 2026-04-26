package Controllers.MedicalControllers;

import java.util.ArrayList;
import java.util.List;

public class MedicalManagementFormData {
    private final String medicalId;
    private final String regNo;
    private final String sessionDate;
    private final String startDate;
    private final String endDate;
    private final String sessionType;
    private final String examCourse;
    private final String reason;
    private final String medicalFilePath;
    private final boolean approved;
    private final List<Integer> attendanceIds;

    public MedicalManagementFormData(String medicalId, String regNo, String sessionDate, String sessionType,
                                     String examCourse, String reason, boolean approved) {
        this(medicalId, regNo, sessionDate, sessionType, examCourse, reason, null, approved);
    }

    public MedicalManagementFormData(String medicalId, String regNo, String sessionDate, String sessionType,
                                     String examCourse, String reason, String medicalFilePath, boolean approved) {
        this(medicalId, regNo, sessionDate, sessionDate, sessionDate, sessionType, examCourse, reason,
                medicalFilePath, approved, new ArrayList<>());
    }

    public MedicalManagementFormData(String medicalId, String regNo, String sessionDate, String startDate,
                                     String endDate, String sessionType, String examCourse, String reason,
                                     String medicalFilePath, boolean approved, List<Integer> attendanceIds) {
        this.medicalId = normalize(medicalId);
        this.regNo = normalize(regNo);
        this.sessionDate = normalize(sessionDate);
        this.startDate = normalize(startDate);
        this.endDate = normalize(endDate);
        this.sessionType = normalize(sessionType);
        this.examCourse = normalize(examCourse);
        this.reason = normalize(reason);
        this.medicalFilePath = normalize(medicalFilePath);
        this.approved = approved;
        this.attendanceIds = attendanceIds == null ? new ArrayList<>() : new ArrayList<>(attendanceIds);
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

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
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

    public String getMedicalFilePath() {
        return medicalFilePath;
    }

    public boolean isApproved() {
        return approved;
    }

    public List<Integer> getAttendanceIds() {
        return new ArrayList<>(attendanceIds);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
