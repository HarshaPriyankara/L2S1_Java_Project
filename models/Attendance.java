package models;

import java.time.LocalDate;

public class Attendance {

    private String attendanceId;
    private String regNo;
    private String courseCode;
    private LocalDate sessionDate;
    private String sessionType;
    private int sessionHours;
    private String status;
    private String medicalId;


    public Attendance(String attendanceId, String courseCode, String regNo, LocalDate sessionDate, String sessionType, int sessionHours, String status, String medicalId) {
        this.attendanceId = attendanceId;
        this.courseCode = courseCode;
        this.regNo = regNo;
        this.sessionDate = sessionDate;
        this.sessionType = sessionType;
        this.sessionHours = sessionHours;
        this.status = status;
        this.medicalId = medicalId;
    }

    public void markAttendance(){
        this.status ="PRESENT";
    }


}
