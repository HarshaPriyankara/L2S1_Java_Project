package Controllers.StudentControllers;

import java.util.List;

public class LecturerGpaResult {
    private final List<Object[]> summaryRows;
    private final List<Object[]> detailRows;
    private final String selectedStudentId;
    private final String selectedStudentSummary;
    private final String errorMessage;

    public LecturerGpaResult(List<Object[]> summaryRows, List<Object[]> detailRows,
                             String selectedStudentId, String selectedStudentSummary,
                             String errorMessage) {
        this.summaryRows = summaryRows;
        this.detailRows = detailRows;
        this.selectedStudentId = selectedStudentId;
        this.selectedStudentSummary = selectedStudentSummary;
        this.errorMessage = errorMessage;
    }

    public List<Object[]> getSummaryRows() {
        return summaryRows;
    }

    public List<Object[]> getDetailRows() {
        return detailRows;
    }

    public String getSelectedStudentId() {
        return selectedStudentId;
    }

    public String getSelectedStudentSummary() {
        return selectedStudentSummary;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
