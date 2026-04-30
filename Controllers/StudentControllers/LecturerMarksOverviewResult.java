package Controllers.StudentControllers;

import java.util.List;

public class LecturerMarksOverviewResult {
    private final List<String> lecturerCourses;
    private final List<String> allowedMarkTypes;
    private final String errorMessage;

    public LecturerMarksOverviewResult(List<String> lecturerCourses, List<String> allowedMarkTypes, String errorMessage) {
        this.lecturerCourses = lecturerCourses;
        this.allowedMarkTypes = allowedMarkTypes;
        this.errorMessage = errorMessage;
    }

    ///  @author dilusha
    public List<String> getLecturerCourses() {
        return lecturerCourses;
    }

    /// @author dlisha
    public List<String> getAllowedMarkTypes() {
        return allowedMarkTypes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
