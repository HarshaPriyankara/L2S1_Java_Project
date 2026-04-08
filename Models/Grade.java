package Models;

public class Grade {

    private String gradeId;
    private String regNo;
    private String courseCode;
    private int    credits;
    private String grade;
    private double gradePoint;
    private String semester;


    public Grade(String gradeId, String regNo, String courseCode, int credits, String semester) {
        this.gradeId = gradeId;
        this.regNo = regNo;
        this.courseCode = courseCode;
        this.credits = credits;
        this.semester = semester;
        this.grade =calculateGrade();
        this.gradePoint =toGradePoint(this.grade);
    }

    private String calculateGrade() {
        return "x";
    }

    private double toGradePoint(String grade) {
        return 0;
    }


}
