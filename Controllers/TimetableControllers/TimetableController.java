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

    //timetable load
    public List<Timetable> loadTimetable(String deptId) {
        return timetableDAO.getFiltered(deptId);
    }

    //Sync
    public TimetableOperationResult syncTimetable(List<TimetableRowInput> rows, String deptId) {
        List<Timetable> newList = new ArrayList<>();

        try {
            for (TimetableRowInput row : rows) {
                if (row.getCourseCode() == null || row.getCourseCode().trim().isEmpty()) {
                    continue;
                }

                Timetable timetable = new Timetable();
                timetable.setCourseCode(row.getCourseCode());
                timetable.setDay(row.getDay());

                //time format
                String startTime = row.getStartTime().replace(".", ":");
                String endTime = row.getEndTime().replace(".", ":");

                timetable.setStartTime(LocalTime.parse(startTime));
                timetable.setEndTime(LocalTime.parse(endTime));
                timetable.setVenue(row.getVenue());
                timetable.setSessionType(row.getSessionType());
                timetable.setDepartmentId(deptId);
                newList.add(timetable);
            }


            if (timetableDAO.syncTimetable(newList, deptId)) {
                return new TimetableOperationResult(true, "Timetable Updated Successfully!");
            }
            return new TimetableOperationResult(false, "Update Failed!");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new TimetableOperationResult(false, "Check Time Formats (HH:mm)!");
        }
    }

    // student timetable
    public TimetableStudentResult loadStudentTimetable(String studentId) {
        String deptId = undergraduateDAO.getStudentDepartmentId(studentId);

        if (deptId == null || deptId.isBlank()) {
            return new TimetableStudentResult(null, "Unable to find your department details.");
        }


        List<Timetable> list = timetableDAO.getStudentTimetable(deptId);
        return new TimetableStudentResult(list, null);
    }

    // Result Class
    public static class TimetableStudentResult {
        private final List<Timetable> timetables;
        private final String errorMessage;

        public TimetableStudentResult(List<Timetable> timetables, String errorMessage) {
            this.timetables = timetables;
            this.errorMessage = errorMessage;
        }

        public List<Timetable> getTimetables() { return timetables; }
        public String getErrorMessage() { return errorMessage; }
        public boolean hasError() { return errorMessage != null; }
    }
}