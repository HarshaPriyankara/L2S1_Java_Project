package Controllers.StudentControllers;

import DAO.LecturerStudentDAO;
import Utils.CourseMarkScheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LecturerMarksOverviewController {
    private final LecturerStudentDAO lecturerStudentDAO = new LecturerStudentDAO();

    public LecturerMarksOverviewResult loadOverview(String lecturerId, String courseCode) {
        try {
            List<String> courses = lecturerStudentDAO.getLecturerCourses(lecturerId);
            List<String> markTypes;

            if (courseCode == null || courseCode.isBlank()) {
                markTypes = Collections.emptyList();
            } else {
                String[] allowedMarkTypes = CourseMarkScheme.forCourse(courseCode).getAllowedMarkTypes();
                markTypes = Arrays.asList(allowedMarkTypes);
            }

            return new LecturerMarksOverviewResult(courses, markTypes, null);
        } catch (Exception ex) {
            return new LecturerMarksOverviewResult(null, null, "Unable to load marks overview: " + ex.getMessage());
        }
    }
}
