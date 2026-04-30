package Models;

import java.time.LocalDate;
/// @author harsha
public class CourseMaterial {
    //fields
    private String materialId;
    private String courseCode;
    private String title;
    private String fileURL;
    private String type;
    private String uploadedBy;
    private LocalDate uploadedAt;
    //constructors
    public CourseMaterial() {
    }
    public CourseMaterial(String materialId, String courseCode, String title,
                          String fileURL, String type,
                          String uploadedBy, LocalDate uploadedAt) {
        this.materialId = materialId;
        this.courseCode = courseCode;
        this.title      = title;
        this.fileURL    = fileURL;
        this.type       = type;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getType() { return type; }

    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
}
