package Controllers.CourseMaterialControllers;

public class CourseMaterialRow {
    private final int materialId;
    private final String title;
    private final String courseCode;
    private final String uploadedAt;
    private final String filePath;

    public CourseMaterialRow(int materialId, String title, String courseCode, String uploadedAt, String filePath) {
        this.materialId = materialId;
        this.title = title;
        this.courseCode = courseCode;
        this.uploadedAt = uploadedAt;
        this.filePath = filePath;
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

    public String getUploadedAt() {
        return uploadedAt;
    }

    public String getFilePath() {
        return filePath;
    }
}
