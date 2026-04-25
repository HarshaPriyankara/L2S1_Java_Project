package Controllers.TimetableControllers;

public class TimetableOperationResult {
    private final boolean success;
    private final String message;

    public TimetableOperationResult(boolean success, String message) {
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
