package Controllers.StudentControllers;

import Models.MedicalRecord;
import Utils.MarksCalculator;

import java.util.List;
import java.util.Map;

public class StudentDetailsResult {
    private final Map<String, String> profile;
    private final List<MarksCalculator.MarkBreakdown> marks;
    private final MarksCalculator.MarkBreakdown selectedCourseBreakdown;
    private final List<Object[]> attendanceRows;
    private final List<MedicalRecord> medicalRecords;
    private final String errorMessage;

    public StudentDetailsResult(Map<String, String> profile,
                                List<MarksCalculator.MarkBreakdown> marks,
                                MarksCalculator.MarkBreakdown selectedCourseBreakdown,
                                List<Object[]> attendanceRows,
                                List<MedicalRecord> medicalRecords,
                                String errorMessage) {
        this.profile = profile;
        this.marks = marks;
        this.selectedCourseBreakdown = selectedCourseBreakdown;
        this.attendanceRows = attendanceRows;
        this.medicalRecords = medicalRecords;
        this.errorMessage = errorMessage;
    }

    public Map<String, String> getProfile() {
        return profile;
    }

    public List<MarksCalculator.MarkBreakdown> getMarks() {
        return marks;
    }

    public MarksCalculator.MarkBreakdown getSelectedCourseBreakdown() {
        return selectedCourseBreakdown;
    }

    public List<Object[]> getAttendanceRows() {
        return attendanceRows;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
