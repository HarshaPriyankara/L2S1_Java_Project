package GUI.lecturer;

import Controllers.StudentControllers.Eng2122CaMarksController;
import Controllers.StudentControllers.Eng2122CaMarksResult;
import Controllers.StudentControllers.LecturerMarksOverviewController;
import Controllers.StudentControllers.LecturerMarksOverviewResult;
import GUI.common.UITheme;
import Utils.CourseMarkScheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentMarksPanel extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    private final LecturerMarksOverviewController overviewController = new LecturerMarksOverviewController();
    private final Eng2122CaMarksController caMarksController = new Eng2122CaMarksController();
    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JTextField studentSearchField = new JTextField(12);
    private final JComboBox<String> scopeComboBox = new JComboBox<>(new String[]{"Whole Batch", "Individual Student"});
    private final DefaultListModel<String> markTypeModel = new DefaultListModel<>();
    private final JLabel caRuleLabel = new JLabel("CA Eligibility Rule: -");
    private final JLabel attendanceRuleLabel = new JLabel("Attendance + CA Rule: -");
    private final Runnable onBack;
    private final String lecturerId;

    private final CardLayout centerCardLayout = new CardLayout();
    private final JPanel centerCardPanel = new JPanel(centerCardLayout);
    private DefaultTableModel caMarksModel;
    private DefaultTableModel eligibilityModel;
    private DefaultTableModel attendanceEligibilityModel;
    private DefaultTableModel finalMarksModel;
    private JTable caTable;
    private JTable eligibilityTable;
    private JTable attendanceEligibilityTable;
    private JTable finalMarksTable;
    private final JLabel caViewTitleLabel = new JLabel("ENG2122 CA Marks");
    private final JLabel caNoteLabel = new JLabel();
    private final JLabel eligibilityTitleLabel = new JLabel("End Exam Eligibility by CA");
    private final JLabel eligibilityNoteLabel = new JLabel();
    private final JLabel attendanceEligibilityTitleLabel = new JLabel("End Exam Eligibility by Attendance + CA");
    private final JLabel attendanceEligibilityNoteLabel = new JLabel();
    private final JLabel finalMarksTitleLabel = new JLabel("Final Marks (CA + END)");
    private final JLabel finalMarksNoteLabel = new JLabel();

    public StudentMarksPanel(String lecturerId, Runnable onBack) {
        this.lecturerId = lecturerId;
        this.onBack = onBack;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JPanel topPanel = buildTopPanel();
        add(topPanel, BorderLayout.NORTH);

        //create table structures
        caMarksModel = createCaMarksModel(new String[]{"Reg No"});
        eligibilityModel = createCaMarksModel(new String[]{"Reg No"});
        attendanceEligibilityModel = createCaMarksModel(new String[]{"Reg No"});
        finalMarksModel = createCaMarksModel(new String[]{"Reg No", "CA Marks", "End Marks", "Final Marks", "Grade"});

        //cards of main 4
        centerCardPanel.setBackground(UITheme.APP_BACKGROUND);
        centerCardPanel.add(buildOverviewPanel(), "Overview");
        centerCardPanel.add(buildCaMarksPanel(), "CA");
        centerCardPanel.add(buildEligibilityPanel(), "EligibilityByCa");
        centerCardPanel.add(buildAttendanceEligibilityPanel(), "EligibilityByAttendanceAndCa");
        centerCardPanel.add(buildFinalMarksPanel(), "FinalMarks");
        add(centerCardPanel, BorderLayout.CENTER);

        scopeComboBox.addActionListener(e -> updateSearchState());

        updateSearchState();
        loadInitialData();
        centerCardLayout.show(centerCardPanel, "Overview");
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        JButton backButton = new JButton("Back");
        UITheme.styleNeutralButton(backButton);
        UITheme.setStandardButtonSize(backButton);
        backButton.addActionListener(e -> this.onBack.run());

        UITheme.styleComboBox(courseComboBox);
        UITheme.styleComboBox(scopeComboBox);
        UITheme.styleTextField(studentSearchField);

        JButton loadButton = new JButton("Load Marks Base");
        UITheme.stylePrimaryButton(loadButton);
        UITheme.setWideButtonSize(loadButton);
        loadButton.addActionListener(e -> {
            loadOverview();
            centerCardLayout.show(centerCardPanel, "Overview");
        });

        topPanel.add(backButton);
        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("View Scope:"));
        topPanel.add(scopeComboBox);
        topPanel.add(new JLabel("Reg No:"));
        topPanel.add(studentSearchField);
        topPanel.add(loadButton);
        return topPanel;
    }

    private JPanel buildOverviewPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel infoPanel = new JPanel(new BorderLayout(0, 10));
        infoPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel markTypesPanel = new JPanel(new BorderLayout(0, 8));
        markTypesPanel.setBackground(UITheme.APP_BACKGROUND);
        JLabel typesTitle = new JLabel("Available Assessment Types");
        typesTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        typesTitle.setForeground(UITheme.TEXT_PRIMARY);
        markTypesPanel.add(typesTitle, BorderLayout.NORTH);
        JList<String> markTypesList = new JList<>(markTypeModel);
        markTypesList.setVisibleRowCount(8);
        markTypesPanel.add(new JScrollPane(markTypesList), BorderLayout.CENTER);

        JPanel rulePanel = new JPanel();
        rulePanel.setBackground(UITheme.APP_BACKGROUND);
        rulePanel.setLayout(new BoxLayout(rulePanel, BoxLayout.Y_AXIS));
        rulePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 224, 234)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));

        JLabel ruleTitle = new JLabel("Current Rule Base");
        ruleTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        ruleTitle.setForeground(UITheme.TEXT_PRIMARY);
        rulePanel.add(ruleTitle);
        rulePanel.add(Box.createVerticalStrut(12));

        caRuleLabel.setForeground(UITheme.TEXT_PRIMARY);
        attendanceRuleLabel.setForeground(UITheme.TEXT_PRIMARY);
        rulePanel.add(caRuleLabel);
        rulePanel.add(Box.createVerticalStrut(10));
        rulePanel.add(attendanceRuleLabel);

        infoPanel.add(markTypesPanel, BorderLayout.CENTER);
        infoPanel.add(rulePanel, BorderLayout.SOUTH);

        JPanel actionCardsPanel = new JPanel();
        actionCardsPanel.setBackground(UITheme.APP_BACKGROUND);
        actionCardsPanel.setLayout(new BoxLayout(actionCardsPanel, BoxLayout.Y_AXIS));
        actionCardsPanel.add(createActionCard("CA Marks", this::showCaMarksView));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("End Exam Eligibility by CA", this::showEligibilityByCaView));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("End Exam Eligibility by Attendance + CA", this::showEligibilityByAttendanceAndCaView));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("Final Marks (CA + END)", this::showFinalMarksBaseView));

        centerPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(actionCardsPanel, BorderLayout.EAST);
        return centerPanel;
    }

    private JPanel buildCaMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UITheme.APP_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.APP_BACKGROUND);

        caViewTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        caViewTitleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(caViewTitleLabel, BorderLayout.WEST);

        //back to marks base menu
        JButton backToOverviewButton = new JButton("Back to Marks Menu");
        UITheme.styleNeutralButton(backToOverviewButton);
        backToOverviewButton.addActionListener(e -> centerCardLayout.show(centerCardPanel, "Overview"));
        headerPanel.add(backToOverviewButton, BorderLayout.EAST);

        caTable = new JTable(caMarksModel);
        caTable.setRowHeight(28);
        UITheme.styleTable(caTable);

        caNoteLabel.setForeground(UITheme.TEXT_MUTED);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(caTable), BorderLayout.CENTER);
        panel.add(caNoteLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildEligibilityPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UITheme.APP_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.APP_BACKGROUND);

        eligibilityTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        eligibilityTitleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(eligibilityTitleLabel, BorderLayout.WEST);

        JButton backToOverviewButton = new JButton("Back to Marks Menu");
        UITheme.styleNeutralButton(backToOverviewButton);
        backToOverviewButton.addActionListener(e -> centerCardLayout.show(centerCardPanel, "Overview"));
        headerPanel.add(backToOverviewButton, BorderLayout.EAST);

        eligibilityTable = new JTable(eligibilityModel);
        eligibilityTable.setRowHeight(28);
        UITheme.styleTable(eligibilityTable);

        eligibilityNoteLabel.setForeground(UITheme.TEXT_MUTED);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eligibilityTable), BorderLayout.CENTER);
        panel.add(eligibilityNoteLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFinalMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UITheme.APP_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.APP_BACKGROUND);

        finalMarksTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        finalMarksTitleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(finalMarksTitleLabel, BorderLayout.WEST);

        JButton backToOverviewButton = new JButton("Back to Marks Menu");
        UITheme.styleNeutralButton(backToOverviewButton);
        backToOverviewButton.addActionListener(e -> centerCardLayout.show(centerCardPanel, "Overview"));
        headerPanel.add(backToOverviewButton, BorderLayout.EAST);

        finalMarksTable = new JTable(finalMarksModel);
        finalMarksTable.setRowHeight(28);
        UITheme.styleTable(finalMarksTable);

        finalMarksNoteLabel.setForeground(UITheme.TEXT_MUTED);
        finalMarksNoteLabel.setText("Final marks logic will be added in the next step based on selected subject and scope.");

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(finalMarksTable), BorderLayout.CENTER);
        panel.add(finalMarksNoteLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAttendanceEligibilityPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UITheme.APP_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.APP_BACKGROUND);

        attendanceEligibilityTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        attendanceEligibilityTitleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(attendanceEligibilityTitleLabel, BorderLayout.WEST);

        JButton backToOverviewButton = new JButton("Back to Marks Menu");
        UITheme.styleNeutralButton(backToOverviewButton);
        backToOverviewButton.addActionListener(e -> centerCardLayout.show(centerCardPanel, "Overview"));
        headerPanel.add(backToOverviewButton, BorderLayout.EAST);

        attendanceEligibilityTable = new JTable(attendanceEligibilityModel);
        attendanceEligibilityTable.setRowHeight(28);
        UITheme.styleTable(attendanceEligibilityTable);

        attendanceEligibilityNoteLabel.setForeground(UITheme.TEXT_MUTED);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(attendanceEligibilityTable), BorderLayout.CENTER);
        panel.add(attendanceEligibilityNoteLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void loadInitialData() {
        LecturerMarksOverviewResult result = overviewController.loadOverview(lecturerId, null);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        courseComboBox.removeAllItems();
        for (String course : result.getLecturerCourses()) {
            courseComboBox.addItem(course);
        }

        if (courseComboBox.getItemCount() > 0) {
            loadOverview();
        }
    }

    private void loadOverview() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        LecturerMarksOverviewResult result = overviewController.loadOverview(lecturerId, courseCode);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        markTypeModel.clear();
        for (String type : result.getAllowedMarkTypes()) {
            markTypeModel.addElement(type);
        }

        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        caRuleLabel.setText(String.format(
                "CA Eligibility Rule: CA marks should be at least %.2f out of %.2f",
                scheme.getCaPassMark(), scheme.getCaWeight()
        ));
        attendanceRuleLabel.setText(
                "Attendance + CA Rule: attendance should be at least 80.00% and CA should reach the course CA pass mark"
        );
    }

    private void showCaMarksView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(scopeComboBox.getSelectedItem());
        String studentId = studentSearchField.getText().trim();

        Eng2122CaMarksResult result = caMarksController.loadCaMarks(courseCode, studentId, individualView);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "CA Marks", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        caTable.setModel(createCaMarksModel(result.getColumns()));
        caMarksModel = (DefaultTableModel) caTable.getModel();
        UITheme.styleTable(caTable);
        caMarksModel.setRowCount(0);
        for (Object[] row : result.getRows()) {
            caMarksModel.addRow(row);
        }

        caViewTitleLabel.setText(result.getTitle());
        caNoteLabel.setText(result.getNote());
        centerCardLayout.show(centerCardPanel, "CA");
    }

    private void showEligibilityByCaView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(scopeComboBox.getSelectedItem());
        String studentId = studentSearchField.getText().trim();

        Eng2122CaMarksResult result = caMarksController.loadEndEligibilityByCa(courseCode, studentId, individualView);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "End Exam Eligibility by CA", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        eligibilityTable.setModel(createCaMarksModel(result.getColumns()));
        eligibilityModel = (DefaultTableModel) eligibilityTable.getModel();
        UITheme.styleTable(eligibilityTable);
        eligibilityModel.setRowCount(0);
        for (Object[] row : result.getRows()) {
            eligibilityModel.addRow(row);
        }

        eligibilityTitleLabel.setText(result.getTitle());
        eligibilityNoteLabel.setText(result.getNote());
        centerCardLayout.show(centerCardPanel, "EligibilityByCa");
    }

    private void showEligibilityByAttendanceAndCaView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(scopeComboBox.getSelectedItem());
        String studentId = studentSearchField.getText().trim();

        Eng2122CaMarksResult result = caMarksController.loadEndEligibilityByAttendanceAndCa(courseCode, studentId, individualView);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "End Exam Eligibility by Attendance + CA", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        attendanceEligibilityTable.setModel(createCaMarksModel(result.getColumns()));
        attendanceEligibilityModel = (DefaultTableModel) attendanceEligibilityTable.getModel();
        UITheme.styleTable(attendanceEligibilityTable);
        attendanceEligibilityModel.setRowCount(0);
        for (Object[] row : result.getRows()) {
            attendanceEligibilityModel.addRow(row);
        }

        attendanceEligibilityTitleLabel.setText(result.getTitle());
        attendanceEligibilityNoteLabel.setText(result.getNote());
        centerCardLayout.show(centerCardPanel, "EligibilityByAttendanceAndCa");
    }

    private void showFinalMarksBaseView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(scopeComboBox.getSelectedItem());
        String studentId = studentSearchField.getText().trim();

        Eng2122CaMarksResult result = caMarksController.loadFinalMarks(courseCode, studentId, individualView);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Final Marks", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        finalMarksTable.setModel(createCaMarksModel(result.getColumns()));
        finalMarksModel = (DefaultTableModel) finalMarksTable.getModel();
        UITheme.styleTable(finalMarksTable);
        finalMarksModel.setRowCount(0);
        for (Object[] row : result.getRows()) {
            finalMarksModel.addRow(row);
        }

        finalMarksTitleLabel.setText(result.getTitle());
        finalMarksNoteLabel.setText(result.getNote());
        centerCardLayout.show(centerCardPanel, "FinalMarks");
    }

    private DefaultTableModel createCaMarksModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void updateSearchState() {
        boolean individual = "Individual Student".equals(scopeComboBox.getSelectedItem());
        studentSearchField.setEnabled(individual);
        if (!individual) {
            studentSearchField.setText("");
        }
    }

    private JPanel createActionCard(String title, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setPreferredSize(new Dimension(360, 68));
        card.setMaximumSize(new Dimension(360, 68));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return card;
    }

}
