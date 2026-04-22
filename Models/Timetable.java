package Models;

import DAO.TimetableDAO;
import java.time.LocalTime;
import java.util.List;

public class Timetable {
    private String timeTableId;
    private String courseCode;
    private String departmentId;
    private String departmentName;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private String sessionType;
    private String courseName;
    private String level;
    private String semester;

    public Timetable() {}

    // --- Getters and Setters ---
    public String getTimeTableId() { return timeTableId; }
    public void setTimeTableId(String timeTableId) { this.timeTableId = timeTableId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

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

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    // --- Database Operations ---

    // කාලසටහනක් අලුතින් ඇතුළත් කිරීම
    public boolean createTimeTable() {
        TimetableDAO dao = new TimetableDAO();
        return dao.saveTimetable(this);
    }

    // කාලසටහනක් යාවත්කාලීන කිරීම
    public boolean updateTimeTable() {
        TimetableDAO dao = new TimetableDAO();
        return dao.updateTimetable(this);
    }

    // කාලසටහනක් මකා දැමීම
    public static boolean deleteTimeTable(String id) {
        TimetableDAO dao = new TimetableDAO();
        // DAO එකේ deleteTimetable මෙතඩ් එක නිවැරදිව කෝල් කරනවා
        return dao.deleteTimetable(id);
    }

    /**
     * ශිෂ්‍යයාට තමන්ගේ අංශයේ සහ පොදු (D4) කාලසටහන පෙන්වීමට
     */
    public static List<Timetable> getTimeTableByDept(String deptId, String level, String semester) {
        TimetableDAO dao = new TimetableDAO();
        // DAO එකේ getStudentTimetable මෙතඩ් එකට පරාමිතීන් යවනවා
        return dao.getStudentTimetable(deptId, level, semester);
    }

    /**
     * Technical Officer (TO) හට Level, Semester සහ Department අනුව
     * කාලසටහන පෙන්වීමට
     */
    public static List<Timetable> getAllTimetables(String level, String semester, String deptId) {
        TimetableDAO dao = new TimetableDAO();
        // DAO එකේ getTOTimetableFiltered මෙතඩ් එකට පරාමිතීන් 3ම පාස් කරනවා
        return dao.getTOTimetableFiltered(level, semester, deptId);
    }
}