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

    // 1. කාලසටහන Load කිරීම (දැන් deptId විතරයි)
    public List<Timetable> loadTimetable(String deptId) {
        return timetableDAO.getFiltered(deptId);
    }

    // 2. කාලසටහන Sync කිරීම
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

                // වෙලාව Format කිරීම (. තිබුණොත් : වලට හරවනවා)
                String startTime = row.getStartTime().replace(".", ":");
                String endTime = row.getEndTime().replace(".", ":");

                timetable.setStartTime(LocalTime.parse(startTime));
                timetable.setEndTime(LocalTime.parse(endTime));
                timetable.setVenue(row.getVenue());
                timetable.setSessionType(row.getSessionType());
                timetable.setDepartmentId(deptId);
                newList.add(timetable);
            }

            // DAO එකේ අප්ඩේට් කරපු syncTimetable මෙතඩ් එක කෝල් කරනවා
            if (timetableDAO.syncTimetable(newList, deptId)) {
                return new TimetableOperationResult(true, "Timetable Updated Successfully!");
            }
            return new TimetableOperationResult(false, "Update Failed!");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new TimetableOperationResult(false, "Check Time Formats (HH:mm)!");
        }
    }

    // 3. ශිෂ්‍යයාට අදාළ කාලසටහන ලබා ගැනීම
    public TimetableStudentResult loadStudentTimetable(String studentId) {
        String deptId = undergraduateDAO.getStudentDepartmentId(studentId);

        if (deptId == null || deptId.isBlank()) {
            return new TimetableStudentResult(null, "Unable to find your department details.");
        }

        // DAO එකේ පරාමිතීන් අයින් කළ නිසා මෙතනත් සරලයි
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