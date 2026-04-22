/*package GUI.lecturer;

import javax.swing.*;
import java.awt.*;

public class MarksManagement extends JPanel {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    public MarksManagement() {
        setLayout(new BorderLayout());

        // panels are register
        mainContainer.add(createMenuPanel(), "Menu");
        mainContainer.add(new MarksUploadPanel(this), "AddMarks");
        mainContainer.add(new MarksUpdatePanel(this), "UpdateMarks");
        mainContainer.add(new MarksDeletePanel(this), "DeleteMarks");

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(15, 0, 15, 0); gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAdd = createMenuButton("Upload New Marks");
        JButton btnUpdate = createMenuButton("Update Existing Marks");
        JButton btnDelete = createMenuButton("Delete Student Marks");

        btnAdd.addActionListener(e -> cardLayout.show(mainContainer, "AddMarks"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainContainer, "UpdateMarks"));
        btnDelete.addActionListener(e -> cardLayout.show(mainContainer, "DeleteMarks"));

        menuPanel.add(btnAdd, gbc);
        menuPanel.add(btnUpdate, gbc);
        menuPanel.add(btnDelete, gbc);

        return menuPanel;
    }

    public void showMenu() {
        cardLayout.show(mainContainer, "Menu");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(500, 70));
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setBackground(new Color(46, 125, 192));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}*/
package GUI.lecturer;

import Utils.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class MarksManagement extends JPanel {
    private JComboBox<String> courseComboBox;
    private JComboBox<String> typeComboBox; // Quiz, Exam වර්ග තේරීමට
    private JTable marksTable;
    private DefaultTableModel tableModel;
    private String lecturerID;

    public MarksManagement(String lecturerID) {
        this.lecturerID = lecturerID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        courseComboBox = new JComboBox<>();
        loadLecturerCourses();

        // Marks Type select
        String[] types = {"Quiz_1", "Quiz_2", "Quiz_3", "Assignment_1", "Assignment_2", "Mini_project", "Mid_theory", "Mid_practical", "End_theory", "End_practical"};
        typeComboBox = new JComboBox<>(types);

        JButton btnLoad = new JButton("Show Students");
        styleButton(btnLoad, new Color(46, 125, 192));

        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Assessment:"));
        topPanel.add(typeComboBox);
        topPanel.add(btnLoad);
        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        // Mark ID not showing
        String[] columns = {"Student ID", "Course Code", "Assessment Type", "Mark Value"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Mark Value පමණක් edit කළ හැක
            }
        };
        marksTable = new JTable(tableModel);
        add(new JScrollPane(marksTable), BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnSave = new JButton("Save / Update Marks");
        styleButton(btnSave, new Color(40, 167, 69));

        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadEnrolledStudents());
        btnSave.addActionListener(e -> saveOrUpdateMarks());
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void loadLecturerCourses() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT Course_code FROM course WHERE Lecturer_in_charge = ?")) {
            pst.setString(1, lecturerID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) courseComboBox.addItem(rs.getString("Course_code"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadEnrolledStudents() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String selectedType = (String) typeComboBox.getSelectedItem();
        if (selectedCourse == null) return;

        tableModel.setRowCount(0);

        // LEFT JOIN using and choose not adding mark student
        String query = "SELECT e.Reg_no, e.Course_code, m.Marks_value " +
                "FROM enrollment e " +
                "LEFT JOIN MARK m ON e.Reg_no = m.Reg_no AND e.Course_code = m.Course_code AND m.Marks_type = ? " +
                "WHERE e.Course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, selectedType);
            pst.setString(2, selectedCourse);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("Reg_no"));
                row.add(selectedCourse);
                row.add(selectedType);
                Object mark = rs.getObject("Marks_value");
                row.add(mark == null ? "" : mark); // ලකුණු නැත්නම් හිස්ව පෙන්වයි
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void saveOrUpdateMarks() {
        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) return;

        try (Connection conn = DBConnection.getConnection()) {


            for (int i = 0; i < rowCount; i++) {
                String regNo = tableModel.getValueAt(i, 0).toString();
                String course = tableModel.getValueAt(i, 1).toString();
                String type = tableModel.getValueAt(i, 2).toString();
                String markStr = tableModel.getValueAt(i, 3).toString();

                if (markStr.isEmpty()) continue; // ලකුණු ඇතුළත් කර නැතිනම් skip කරයි

                double markValue = Double.parseDouble(markStr);

                // first see, have record
                String checkQuery = "SELECT Mark_id FROM MARK WHERE Reg_no=? AND Course_code=? AND Marks_type=?";
                PreparedStatement checkPst = conn.prepareStatement(checkQuery);
                checkPst.setString(1, regNo);
                checkPst.setString(2, course);
                checkPst.setString(3, type);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    // Update record
                    String updateQuery = "UPDATE MARK SET Marks_value=? WHERE Mark_id=?";
                    PreparedStatement upPst = conn.prepareStatement(updateQuery);
                    upPst.setDouble(1, markValue);
                    upPst.setInt(2, rs.getInt("Mark_id"));
                    upPst.executeUpdate();
                } else {
                    // Insert new record
                    String insertQuery = "INSERT INTO MARK (Reg_no, Course_code, Marks_type, Marks_value) VALUES (?, ?, ?, ?)";
                    PreparedStatement inPst = conn.prepareStatement(insertQuery);
                    inPst.setString(1, regNo);
                    inPst.setString(2, course);
                    inPst.setString(3, type);
                    inPst.setDouble(4, markValue);
                    inPst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "All marks saved successfully!");
            loadEnrolledStudents(); // Refresh data
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + e.getMessage());
        }
    }
}