package Controllers.StudentControllers;

import DAO.LecturerStudentDAO;
import Utils.CourseMarkScheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LecturerMarksOverviewController {
    //create lecturerStudentDAO object
    private final LecturerStudentDAO lecturerStudentDAO = new LecturerStudentDAO();

    /// @author dilusha
    public LecturerMarksOverviewResult loadOverview(String lecturerId, String courseCode) {
        try {
            List<String> courses = lecturerStudentDAO.getLecturerCourses(lecturerId); //get lecturer courses
            List<String> markTypes;

            if (courseCode == null || courseCode.isBlank()) {
                markTypes = Collections.emptyList();
            } else {
                String[] allowedMarkTypes = CourseMarkScheme.forCourse(courseCode).getAllowedMarkTypes(); //get allowed markstypes
                markTypes = Arrays.asList(allowedMarkTypes);
            }

            return new LecturerMarksOverviewResult(courses, markTypes, null);
        } catch (Exception ex) {
            return new LecturerMarksOverviewResult(null, null, "Unable to load marks overview: " + ex.getMessage());
        }
    }
}
