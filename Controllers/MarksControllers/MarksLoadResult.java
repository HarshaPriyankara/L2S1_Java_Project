package Controllers.MarksControllers;

import java.util.List;

public class MarksLoadResult {
    private final List<Object[]> tableRows;
    private final String statusMessage;
    private final String errorMessage;

    public MarksLoadResult(List<Object[]> tableRows, String statusMessage, String errorMessage) {
        this.tableRows = tableRows;
        this.statusMessage = statusMessage;
        this.errorMessage = errorMessage;
    }

    public List<Object[]> getTableRows() {
        return tableRows;
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
