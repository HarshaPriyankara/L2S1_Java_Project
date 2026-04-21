package Models;

import DAO.TimetableDAO;
import java.time.LocalTime;
import java.util.List;

public class Timetable {
    private String timeTableId;
    private String courseCode;
    private String departmentId;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private String sessionType;
    private String courseName;

    public Timetable() {}

    // --- Getters and Setters ---
    //get GUI data
    public String getTimeTableId() { return timeTableId; }
    public void setTimeTableId(String timeTableId) { this.timeTableId = timeTableId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    //Add entry
    public boolean createTimeTable() {
        TimetableDAO dao = new TimetableDAO();
        //success variable define
        boolean success = dao.saveTimetable(this);
        return success;
    }

    //update method
    public boolean updateTimeTable() {
        TimetableDAO dao = new TimetableDAO();
        return dao.updateTimetable(this);
    }

    //gui delete button
    public static boolean deleteTimeTable(String id) {
        TimetableDAO dao = new TimetableDAO();
        return dao.deleteTimetable(id);
    }

    // show student timetable
    public static List<Timetable> getTimeTableByDept(String deptId, String level, String semester) {
        TimetableDAO dao = new TimetableDAO();
        return dao.getStudentTimetable(deptId, level, semester);
    }
}