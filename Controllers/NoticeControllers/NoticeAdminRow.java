package Controllers.NoticeControllers;

public class NoticeAdminRow {
    private final int id;
    private final String title;
    private final String targetRoles;
    private final Object addedDate;

    public NoticeAdminRow(int id, String title, String targetRoles, Object addedDate) {
        this.id = id;
        this.title = title;
        this.targetRoles = targetRoles;
        this.addedDate = addedDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTargetRoles() {
        return targetRoles;
    }

    public Object getAddedDate() {
        return addedDate;
    }
}
