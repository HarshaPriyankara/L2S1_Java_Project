package models;

import java.time.LocalDate;

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

    public void upload(){
        this.uploadedAt = LocalDate.now();
        System.out.println("[CourseMaterial] Uploaded: " + title + " for course " + courseCode);
    }

    public void delete(){
        System.out.println("[CourseMaterial] Delete course " + materialId);
    }

    public void download(){
        System.out.println("[CourseMaterial] Download URL " + fileURL);
    }

    public String getMaterialsDetails() {
        return "Material ID : " + materialId + "\n" +
                "Course Code : " + courseCode + "\n" +
                "Title       : " + title + "\n" +
                "Type        : " + type + "\n" +
                "File URL    : " + fileURL + "\n" +
                "Uploaded By : " + uploadedBy + "\n" +
                "Uploaded At : " + (uploadedAt != null ? uploadedAt.toString() : "N/A");
    }


}
