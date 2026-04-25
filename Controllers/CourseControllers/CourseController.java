package Controllers.CourseControllers;

import DAO.CourseDAO;
import Models.Course;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CourseController {
    private final CourseDAO courseDAO = new CourseDAO();

    public CourseOperationResult addCourse(CourseFormData formData) {
        if (isRequiredCourseDataMissing(formData)) {
            return new CourseOperationResult(false, "Please fill all required fields!");
        }

        Integer credits = parseCredits(formData.getCredits());
        if (credits == null) {
            return new CourseOperationResult(false, "Credits must be a number!");
        }

        Course course = new Course(
                formData.getCode(),
                formData.getName(),
                credits,
                formData.getType(),
                formData.getLecturer(),
                formData.getDepartment()
        );

        boolean added = courseDAO.addCourse(course);
        if (added) {
            return new CourseOperationResult(true, "Course Added Successfully!");
        }

        return new CourseOperationResult(false, "Failed to add course. Code might already exist.");
    }

    public CourseOperationResult updateCourse(CourseFormData formData) {
        if (isRequiredCourseDataMissing(formData)) {
            return new CourseOperationResult(false, "Required fields are empty!");
        }

        Integer credits = parseCredits(formData.getCredits());
        if (credits == null) {
            return new CourseOperationResult(false, "Credits must be a number!");
        }

        Course course = new Course(
                formData.getCode(),
                formData.getName(),
                credits,
                formData.getType(),
                formData.getLecturer(),
                formData.getDepartment()
        );

        boolean updated = courseDAO.updateCourse(course);
        if (updated) {
            return new CourseOperationResult(true, "Course Updated Successfully!");
        }

        return new CourseOperationResult(false, "Update Failed!");
    }

    public CourseOperationResult deleteCourse(String courseCode) {
        String normalizedCode = courseCode == null ? "" : courseCode.trim();
        if (normalizedCode.isEmpty()) {
            return new CourseOperationResult(false, "Please enter a Course Code!");
        }

        boolean deleted = courseDAO.deleteCourse(normalizedCode);
        if (deleted) {
            return new CourseOperationResult(true, "Course Deleted Successfully!");
        }

        return new CourseOperationResult(false, "Delete Failed! Course Code not found.");
    }

    public List<String> getCourseCodes() {
        try {
            return courseDAO.getAllCourseCodes();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> getLecturerIds() {
        try {
            return courseDAO.getAllLecturerIds();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> getDepartmentIds() {
        try {
            return courseDAO.getAllDepartmentIds();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private boolean isRequiredCourseDataMissing(CourseFormData formData) {
        return formData.getCode().isEmpty() || formData.getName().isEmpty() || formData.getCredits().isEmpty();
    }

    private Integer parseCredits(String creditsText) {
        try {
            return Integer.parseInt(creditsText);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
