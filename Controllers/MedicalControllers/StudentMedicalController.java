package Controllers.MedicalControllers;

import DAO.AttendanceDAO;
import DAO.MedicalRecordDAO;
import Models.MedicalRecord;

import java.time.LocalDate;
import java.util.List;

public class StudentMedicalController {
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public MedicalActionResult submitMedical(String studentId, LocalDate sessionDate, LocalDate startDate,
                                             LocalDate endDate, String sessionType, String examCourse, String reason,
                                             String medicalFilePath, List<Integer> attendanceIds) {
        String normalizedReason = reason == null ? "" : reason.trim();
        String normalizedSessionType = sessionType == null ? "" : sessionType.trim();
        String normalizedExamCourse = examCourse == null ? "" : examCourse.trim();

        if (normalizedReason.isEmpty()) {
            return new MedicalActionResult(false, "Please enter the medical reason.");
        }

        if ("Exam".equals(normalizedSessionType) && normalizedExamCourse.isEmpty()) {
            return new MedicalActionResult(false, "Please enter the exam course code for exam medicals.");
        }

        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return new MedicalActionResult(false, "Please select a valid medical date range.");
        }

        try {
            int medicalId = medicalRecordDAO.addMedical(studentId, sessionDate, startDate, endDate,
                    normalizedSessionType, normalizedExamCourse, normalizedReason, medicalFilePath, false);
            attendanceDAO.linkAttendanceToMedical(attendanceIds, medicalId);
            return new MedicalActionResult(true, "Medical submitted successfully.");
        } catch (Exception ex) {
            return new MedicalActionResult(false, "Unable to submit medical: " + ex.getMessage());
        }
    }

    public AbsentAttendanceResult loadAbsentAttendance(String studentId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return new AbsentAttendanceResult(null, "Please select a valid date range.");
        }

        try {
            return new AbsentAttendanceResult(
                    attendanceDAO.getAbsentAttendanceByStudentAndDateRange(studentId, startDate, endDate),
                    null
            );
        } catch (Exception ex) {
            return new AbsentAttendanceResult(null, "Unable to load absent attendance: " + ex.getMessage());
        }
    }

    public MedicalRecordsResult loadMedicalRecords(String studentId) {
        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByStudent(studentId);
            return new MedicalRecordsResult(records, null);
        } catch (Exception ex) {
            return new MedicalRecordsResult(null, "Unable to load medical records: " + ex.getMessage());
        }
    }

    public static class MedicalActionResult {
        private final boolean success;
        private final String message;

        public MedicalActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class MedicalRecordsResult {
        private final List<MedicalRecord> records;
        private final String errorMessage;

        public MedicalRecordsResult(List<MedicalRecord> records, String errorMessage) {
            this.records = records;
            this.errorMessage = errorMessage;
        }

        public List<MedicalRecord> getRecords() {
            return records;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }

    public static class AbsentAttendanceResult {
        private final List<Object[]> rows;
        private final String errorMessage;

        public AbsentAttendanceResult(List<Object[]> rows, String errorMessage) {
            this.rows = rows;
            this.errorMessage = errorMessage;
        }

        public List<Object[]> getRows() {
            return rows;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }
}
