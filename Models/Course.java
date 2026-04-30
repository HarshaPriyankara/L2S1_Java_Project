package Models;

public class Course {
    //fields
    private String courseCode;
    private String courseName;
    private int credits;
    private String type;
    private String lecturer;
    private String department;
    //constructors
    public Course() {
    }

    public Course(String courseCode, String courseName, int credits, String type , String lecturer, String departmentId) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.type = type;
        this.lecturer = lecturer;
        this.department = departmentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return courseName;
    }

    public String getType() {
        return type;
    }

    public int getCredits() {
        return credits;
    }

    public String getLecturerId() {return lecturer;}

    public String getDeptId() {
        return department;
    }



}
