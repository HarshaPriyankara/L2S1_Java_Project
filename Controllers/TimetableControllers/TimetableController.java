package Controllers.TimetableControllers;

import DAO.TimetableDAO;
import DAO.UndergraduateDAO;
import Models.Timetable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimetableController {
    private final TimetableDAO timetableDAO = new TimetableDAO();
    private final UndergraduateDAO undergraduateDAO = new UndergraduateDAO();

    public List<Timetable> loadTimetable(String level, String semester, String deptId) {
        return timetableDAO.getFiltered(level, semester, deptId);
    }

    public TimetableOperationResult syncTimetable(List<TimetableRowInput> rows, String level, String semester, String deptId) {
        List<Timetable> newList = new ArrayList<>();

        try {
            for (TimetableRowInput row : rows) {
                if (row.getCourseCode().isEmpty()) {
                    continue;
                }

                Timetable timetable = new Timetable();
                timetable.setCourseCode(row.getCourseCode());
                timetable.setDay(row.getDay());
                timetable.setStartTime(LocalTime.parse(row.getStartTime().replace(".", ":")));
                timetable.setEndTime(LocalTime.parse(row.getEndTime().replace(".", ":")));
                timetable.setVenue(row.getVenue());
                timetable.setSessionType(row.getSessionType());
                timetable.setDepartmentId(deptId);
                newList.add(timetable);
            }

            if (Timetable.syncFullTimetable(newList, level, semester, deptId)) {
                return new TimetableOperationResult(true, "Timetable Updated Successfully!");
            }
            return new TimetableOperationResult(false, "Update Failed!");
        } catch (Exception ex) {
            return new TimetableOperationResult(false, "Check Time Formats (HH:mm)!");
        }
    }

    public TimetableStudentResult loadStudentTimetable(String studentId, String level, String semester) {
        String deptId = undergraduateDAO.getStudentDepartmentId(studentId);
        if (deptId == null || deptId.isBlank()) {
            return new TimetableStudentResult(null, "Unable to find your department details.");
        }

        List<Timetable> list = timetableDAO.getStudentTimetable(deptId, level, semester);
        return new TimetableStudentResult(list, null);
    }

    public static class TimetableStudentResult {
        private final List<Timetable> timetables;
        private final String errorMessage;

        public TimetableStudentResult(List<Timetable> timetables, String errorMessage) {
            this.timetables = timetables;
            this.errorMessage = errorMessage;
        }

        public List<Timetable> getTimetables() {
            return timetables;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return errorMessage != null;
        }
    }
}
