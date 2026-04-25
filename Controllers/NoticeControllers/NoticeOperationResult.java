package Controllers.NoticeControllers;

public class NoticeOperationResult {
    private final boolean success;
    private final String message;

    public NoticeOperationResult(boolean success, String message) {
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
