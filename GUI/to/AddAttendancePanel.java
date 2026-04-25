package GUI.to;

import Controllers.AttendanceControllers.AttendanceController;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class AddAttendancePanel extends JPanel {
    private final AttendanceController controller = new AttendanceController();
    private final JComboBox<String> courseBox = new JComboBox<>();
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Theory", "Practical"});
    private JTable table;
    private DefaultTableModel model;

    public AddAttendancePanel(AttendanceManagement parent) {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(UITheme.APP_BACKGROUND);

        JButton btnBack = new JButton("Back");
        UITheme.styleNeutralButton(btnBack);
        UITheme.setStandardButtonSize(btnBack);
        btnBack.addActionListener(e -> parent.showPanel("Menu"));

        for (String c : controller.getCourseList()) {
            courseBox.addItem(c);
        }
        UITheme.styleComboBox(courseBox);
        UITheme.styleComboBox(typeBox);

        JButton btnLoad = new JButton("Load Students");
        UITheme.stylePrimaryButton(btnLoad);
        UITheme.setWideButtonSize(btnLoad);

        top.add(btnBack);
        top.add(new JLabel("Course:"));
        top.add(courseBox);
        top.add(new JLabel("Type:"));
        top.add(typeBox);
        top.add(btnLoad);

        model = new DefaultTableModel(new String[]{"Student ID", "Course", "Status", "Hours"}, 0);
        table = new JTable(model);
        UITheme.styleTable(table);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Medical"});
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(statusCombo));

        btnLoad.addActionListener(e -> controller.loadEnrolledToTable(courseBox.getSelectedItem().toString(), model));

        JButton btnSave = new JButton("Submit All Records");
        UITheme.styleSuccessButton(btnSave);
        UITheme.setWideButtonSize(btnSave);
        btnSave.addActionListener(e -> {
            boolean success = controller.saveAttendance(
                    courseBox.getSelectedItem().toString(),
                    LocalDate.now().toString(),
                    typeBox.getSelectedItem().toString(),
                    model.getDataVector()
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Attendance Saved!");
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setBackground(UITheme.APP_BACKGROUND);
        bottom.add(btnSave);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
