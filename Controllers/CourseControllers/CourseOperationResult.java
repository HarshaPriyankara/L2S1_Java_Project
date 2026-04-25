package Controllers.CourseControllers;

public class CourseOperationResult {
    private final boolean success;
    private final String message;

    public CourseOperationResult(boolean success, String message) {
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
