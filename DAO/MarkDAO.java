package DAO;

import Utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MarkDAO {

    // Marks Add
    public boolean addMarks(String regNo, String course, String type, double marks) {
        String sql = "INSERT INTO mark (Reg_no, Course_code, Marks_type, Marks_value) VALUES (?, ?, ?, ?)";

        // "Quiz 1" convert "Quiz_1"
        String formattedType = type.replace(" ", "_");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, formattedType); // Formatted type එක මෙතනට
            pst.setDouble(4, marks);

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Marks Update
    public boolean updateMarks(int id, String regNo, String course, String type, double marks) {
        String sql = "UPDATE mark SET Reg_no=?, Course_code=?, Marks_type=?, Marks_value=? WHERE Mark_id=?";

        // convert
        String formattedType = type.replace(" ", "_");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, regNo);
            pst.setString(2, course);
            pst.setString(3, formattedType); // Formatted type එක මෙතනට
            pst.setDouble(4, marks);
            pst.setInt(5, id);

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Marks Delete
    public boolean deleteMarks(int id) {
        String sql = "DELETE FROM mark WHERE Mark_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}