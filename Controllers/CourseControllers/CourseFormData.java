package Controllers.CourseControllers;

public class CourseFormData {
    private final String code;
    private final String name;
    private final String credits;
    private final String type;
    private final String lecturer;
    private final String department;

    public CourseFormData(String code, String name, String credits, String type, String lecturer, String department) {
        this.code = code == null ? "" : code.trim();
        this.name = name == null ? "" : name.trim();
        this.credits = credits == null ? "" : credits.trim();
        this.type = type == null ? "" : type.trim();
        this.lecturer = lecturer == null ? "" : lecturer.trim();
        this.department = department == null ? "" : department.trim();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCredits() {
        return credits;
    }

    public String getType() {
        return type;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getDepartment() {
        return department;
    }
}
