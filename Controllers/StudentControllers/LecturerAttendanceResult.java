package Controllers.StudentControllers;

import java.util.List;

public class LecturerAttendanceResult {
    private final List<LecturerAttendanceSummaryRow> summaryRows;
    private final List<Object[]> detailRows;
    private final String errorMessage;

    public LecturerAttendanceResult(List<LecturerAttendanceSummaryRow> summaryRows,
                                    List<Object[]> detailRows,
                                    String errorMessage) {
        this.summaryRows = summaryRows;
        this.detailRows = detailRows;
        this.errorMessage = errorMessage;
    }

    public List<LecturerAttendanceSummaryRow> getSummaryRows() {
        return summaryRows;
    }

    public List<Object[]> getDetailRows() {
        return detailRows;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
