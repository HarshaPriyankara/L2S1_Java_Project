package Models;

import java.util.List;

public class TechnicalOfficer extends User {

    private String officerID;

    // Getters and Setters
    public String getOfficerID() { return officerID; }
    public void setOfficerID(String officerID) { this.officerID = officerID; }

    public void addAttendance() { /* Implementation */ }
    public void updateAttendance() { /* Implementation */ }
    public void addMedical() { /* Implementation */ }
    public void updateMedical() { /* Implementation */ }

    /**
     * TO ට අදාළ මට්ටම (Level) සහ සෙමෙස්ටර් එක අනුව
     * මුළු ෆැකල්ටියේම කාලසටහන ලබා දීම.
     */
    /**
     * TO ට අදාළ මට්ටම (Level), සෙමෙස්ටර් එක සහ
     * අංශය (Department) අනුව කාලසටහන ලබා දීම.
     */
    public List<Timetable> viewTimetable(String level, String semester, String deptName) {
        // දැන් මෙතනට deptName එකත් එකතු කළා. එතකොට Error එක යනවා.
        return Timetable.getAllTimetables(level, semester, deptName);
    }
    public void viewNotices() { /* Implementation */ }
}