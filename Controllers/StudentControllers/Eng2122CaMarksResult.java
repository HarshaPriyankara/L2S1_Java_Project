package Controllers.StudentControllers;

import java.util.List;

public class Eng2122CaMarksResult {
    private final String[] columns;
    private final List<Object[]> rows;
    private final String title;
    private final String note;
    private final String errorMessage;

    public Eng2122CaMarksResult(String[] columns, List<Object[]> rows, String title, String note, String errorMessage) {
        this.columns = columns;
        this.rows = rows;
        this.title = title;
        this.note = note;
        this.errorMessage = errorMessage;
    }

    public String[] getColumns() {
        return columns;
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
