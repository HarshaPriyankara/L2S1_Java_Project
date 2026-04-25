package Controllers.MarksControllers;

import java.util.List;
import java.util.Map;

public class MarksLoadResult {
    private final List<String> studentIds;
    private final List<Object[]> tableRows;
    private final Map<String, Double> loadedEndMarks;
    private final String statusMessage;
    private final String errorMessage;

    public MarksLoadResult(List<String> studentIds, List<Object[]> tableRows, Map<String, Double> loadedEndMarks,
                           String statusMessage, String errorMessage) {
        this.studentIds = studentIds;
        this.tableRows = tableRows;
        this.loadedEndMarks = loadedEndMarks;
        this.statusMessage = statusMessage;
        this.errorMessage = errorMessage;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public List<Object[]> getTableRows() {
        return tableRows;
    }

    public Map<String, Double> getLoadedEndMarks() {
        return loadedEndMarks;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
