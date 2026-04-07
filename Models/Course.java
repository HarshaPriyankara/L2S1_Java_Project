package Models;

public class Course {


    //fields
    private String courseCode;
    private String courseName;
    private int credits;
    private int theoryHours;
    private int practicalHours;
    private String departmentId;


    //constructors
    public Course() {
    }

    public Course(String courseCode, String courseName, int credits, int theoryHours, int practicalHours, String departmentId) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.theoryHours = theoryHours;
        this.practicalHours = practicalHours;
        this.departmentId = departmentId;
    }



    public void addMaterial(String materialId,String type,String fileURL,String uploadedBy, String title) {
        System.out.println("[Course] Material added to " + courseCode + ": " + title);

    }

    public void updateCourse(String courseName, int credits, int theoryHours, int practicalHours) {
        this.courseName = courseName;
        this.credits = credits;
        this.theoryHours = theoryHours;
        this.practicalHours = practicalHours;
        System.out.println("[Course] updated : " + courseCode);
    }

    public String getCourseDetails() {

        return "Course Code    : " + courseCode + "\n" +
                "Course Name    : " + courseName + "\n" +
                "Credits        : " + credits + "\n" +
                "Theory Hours   : " + theoryHours + "\n" +
                "Practical Hours: " + practicalHours + "\n" +
                "Department     : " + departmentId;
    }



}
