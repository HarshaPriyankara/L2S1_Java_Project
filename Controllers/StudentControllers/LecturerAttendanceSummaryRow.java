package Controllers.StudentControllers;

public class LecturerAttendanceSummaryRow {
    private final String regNo;
    private final double attendedHours;
    private final double totalHours;
    private final int attendedSessions;
    private final int totalSessions;
    private final double percentage;

    public LecturerAttendanceSummaryRow(String regNo, double attendedHours, double totalHours,
                                        int attendedSessions, int totalSessions, double percentage) {
        this.regNo = regNo;
        this.attendedHours = attendedHours;
        this.totalHours = totalHours;
        this.attendedSessions = attendedSessions;
        this.totalSessions = totalSessions;
        this.percentage = percentage;
    }

    public String getRegNo() {
        return regNo;
    }

    public double getAttendedHours() {
        return attendedHours;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public int getAttendedSessions() {
        return attendedSessions;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public double getPercentage() {
        return percentage;
    }
}
