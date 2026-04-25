package Controllers.NoticeControllers;

import Models.Notice;

public class NoticeDetailsResult {
    private final Notice notice;
    private final String content;
    private final String errorMessage;

    public NoticeDetailsResult(Notice notice, String content, String errorMessage) {
        this.notice = notice;
        this.content = content;
        this.errorMessage = errorMessage;
    }

    public Notice getNotice() {
        return notice;
    }

    public String getContent() {
        return content;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
