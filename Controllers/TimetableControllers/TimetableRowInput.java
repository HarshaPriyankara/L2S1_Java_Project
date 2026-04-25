package Controllers.TimetableControllers;

public class TimetableRowInput {
    private final String day;
    private final String courseCode;
    private final String startTime;
    private final String endTime;
    private final String venue;
    private final String sessionType;

    public TimetableRowInput(String day, String courseCode, String startTime, String endTime, String venue, String sessionType) {
        this.day = normalize(day);
        this.courseCode = normalize(courseCode);
        this.startTime = normalize(startTime);
        this.endTime = normalize(endTime);
        this.venue = normalize(venue);
        this.sessionType = normalize(sessionType);
    }

    public String getDay() {
        return day;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getVenue() {
        return venue;
    }

    public String getSessionType() {
        return sessionType;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
