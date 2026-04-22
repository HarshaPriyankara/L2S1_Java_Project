package Controllers;

import DAO.AttendanceDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class AttendanceController {
    private AttendanceDAO dao = new AttendanceDAO();

    public ArrayList<String> getCourseList() {
        try {
            return dao.getAllCourseCodes();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public void loadEnrolledToTable(String courseCode, javax.swing.table.DefaultTableModel model) {
        try {
            model.setRowCount(0);
            ArrayList<String> students = dao.getEnrolledStudentIDs(courseCode);
            for (String id : students) {
                model.addRow(new Object[]{id, courseCode, "Present", 2.0});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean saveAttendance(String course, String date, String type, Vector<Vector> data) {
        try {
            for (Vector row : data) {
                dao.addAttendance(row.get(0).toString(), course, date, type,
                        Double.parseDouble(row.get(3).toString()), row.get(2).toString());
            }
            return true;
        } catch (SQLException e) { return false; }
    }

    public ArrayList<String> getStudentCourses(String studentId) {
        try {
            return dao.getStudentEnrolledCourses(studentId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}