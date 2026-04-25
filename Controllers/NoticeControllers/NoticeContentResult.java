package Controllers.NoticeControllers;

public class NoticeContentResult {
    private final boolean success;
    private final String content;
    private final String message;

    public NoticeContentResult(boolean success, String content, String message) {
        this.success = success;
        this.content = content;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getContent() {
        return content;
    }

    public String getMessage() {
        return message;
    }
}
