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

    // --- Database Operations (New Table Sync Logic) ---

    /**
     * මුළු ටේබල් එකම එකවර Sync කිරීම (Update/Delete/Add සියල්ලම මෙතනින් සිදුවේ)
     */
    public static boolean syncFullTimetable(List<Timetable> list, String level, String semester, String deptId) {
        TimetableDAO dao = new TimetableDAO();
        return dao.syncTimetable(list, level, semester, deptId);
    }

    /**
     * ශිෂ්‍යයාට තමන්ගේ අංශයේ කාලසටහන පෙන්වීමට
     */
    public static List<Timetable> getTimeTableByDept(String deptId, String level, String semester) {
        TimetableDAO dao = new TimetableDAO();
        return dao.getStudentTimetable(deptId, level, semester);
    }

    /**
     * TO හට සියලු දත්ත පෙන්වීමට/ලෝඩ් කිරීමට
     */
    public static List<Timetable> getAllTimetables(String level, String semester, String deptId) {
        TimetableDAO dao = new TimetableDAO();
        return dao.getTOTimetableFiltered(level, semester, deptId);
    }
}