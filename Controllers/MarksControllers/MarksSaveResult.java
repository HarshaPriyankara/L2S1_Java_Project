package Controllers.MarksControllers;

public class MarksSaveResult {
    private final boolean success;
    private final String message;

    public MarksSaveResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
