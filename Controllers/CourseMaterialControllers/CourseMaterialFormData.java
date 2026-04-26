package Controllers.CourseMaterialControllers;

public class CourseMaterialFormData {
    private final int materialId;
    private final String title;
    private final String courseCode;
    private final String uploadedBy;
    private final String filePath;

    public CourseMaterialFormData(int materialId, String title, String courseCode, String uploadedBy, String filePath) {
        this.materialId = materialId;
        this.title = title == null ? "" : title.trim();
        this.courseCode = courseCode == null ? "" : courseCode.trim();
        this.uploadedBy = uploadedBy == null ? "" : uploadedBy.trim();
        this.filePath = filePath == null ? "" : filePath.trim();
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getFilePath() {
        return filePath;
    }
}
