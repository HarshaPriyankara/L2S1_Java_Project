package Controllers.NoticeControllers;

public class NoticeFormData {
    private final String title;
    private final String content;
    private final boolean lecturerSelected;
    private final boolean technicalSelected;
    private final boolean undergraduateSelected;

    public NoticeFormData(String title, String content, boolean lecturerSelected,
                          boolean technicalSelected, boolean undergraduateSelected) {
        this.title = title == null ? "" : title.trim();
        this.content = content == null ? "" : content.trim();
        this.lecturerSelected = lecturerSelected;
        this.technicalSelected = technicalSelected;
        this.undergraduateSelected = undergraduateSelected;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isLecturerSelected() {
        return lecturerSelected;
    }

    public boolean isTechnicalSelected() {
        return technicalSelected;
    }

    public boolean isUndergraduateSelected() {
        return undergraduateSelected;
    }
}
