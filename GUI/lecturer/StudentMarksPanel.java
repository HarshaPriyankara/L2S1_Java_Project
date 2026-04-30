package GUI.lecturer;

import Controllers.StudentControllers.CaMarksController;
import Controllers.StudentControllers.CaMarksResult;
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

    //create objects
    private final LecturerMarksOverviewController overviewController = new LecturerMarksOverviewController();
    private final CaMarksController caMarksController = new CaMarksController();

    //for top search options
    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JTextField studentSearchField = new JTextField(12);
    private final JComboBox<String> scopeComboBox = new JComboBox<>(new String[]{"Whole Batch", "Individual Student"});

    private final DefaultListModel<String> markTypeModel = new DefaultListModel<>();  // for available assessment type of course display
    private final JLabel caRuleLabel = new JLabel("CA Eligibility Rule: -");  //for eligibility rule
    private final JLabel attendanceRuleLabel = new JLabel("Attendance + CA Rule: -");  //for attendance rule

    private final Runnable onBack;  //for goto menu

    private final String lecturerId;

    private final CardLayout centerCardLayout = new CardLayout();
    private final JPanel centerCardPanel = new JPanel(centerCardLayout);

    //data models of various table
    private DefaultTableModel caMarksModel;
    private DefaultTableModel eligibilityModel;
    private DefaultTableModel attendanceEligibilityModel;
    private DefaultTableModel finalMarksModel;

    //GUI tables
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

    //constructor
    /// @author dilusha
    public StudentMarksPanel(String lecturerId, Runnable onBack) {
        this.lecturerId = lecturerId;
        this.onBack = onBack;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JPanel topPanel = buildTopPanel();
        add(topPanel, BorderLayout.NORTH);

        //create table models
        caMarksModel = createCaMarksModel(new String[]{"Reg No"});
        eligibilityModel = createCaMarksModel(new String[]{"Reg No"});
        attendanceEligibilityModel = createCaMarksModel(new String[]{"Reg No"});
        finalMarksModel = createCaMarksModel(new String[]{"Reg No", "CA Marks", "End Marks", "Final Marks", "Grade"});

        //add all cards for centerpanel
        centerCardPanel.setBackground(UITheme.APP_BACKGROUND);
        centerCardPanel.add(buildOverviewPanel(), "Overview");
        centerCardPanel.add(buildCaMarksPanel(), "CA");
        centerCardPanel.add(buildEligibilityPanel(), "EligibilityByCa");
        centerCardPanel.add(buildAttendanceEligibilityPanel(), "EligibilityByAttendanceAndCa");
        centerCardPanel.add(buildFinalMarksPanel(), "FinalMarks");
        add(centerCardPanel, BorderLayout.CENTER);

        scopeComboBox.addActionListener(e -> updateSearchState());

        updateSearchState(); // regno field disable

        loadInitialData();  // bottom descriptions
        centerCardLayout.show(centerCardPanel, "Overview");
    }

    /// @author dilusha
    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        //back button
        JButton backButton = new JButton("Back");
        UITheme.styleNeutralButton(backButton);
        UITheme.setStandardButtonSize(backButton);
        backButton.addActionListener(e -> this.onBack.run());

        //view scope and regno field
        UITheme.styleComboBox(courseComboBox);
        UITheme.styleComboBox(scopeComboBox);
        UITheme.styleTextField(studentSearchField);

        //load mark base button
        JButton loadButton = new JButton("Load Marks Base");
        UITheme.stylePrimaryButton(loadButton);
        UITheme.setWideButtonSize(loadButton);
        loadButton.addActionListener(e -> {
            loadOverview();
            centerCardLayout.show(centerCardPanel, "Overview");
        });

        //create complete top panel
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

    /// @author dilusha
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

        //change panel when create button
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

    /// @author dilusha
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

    /// @author dilusha
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

    /// @author dilusha
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

    /// @author dilusha
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

    ///  @author dilusha
    private void loadInitialData() {
        LecturerMarksOverviewResult result = overviewController.loadOverview(lecturerId, null);  // get courses and types and marks types based on lec id
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

    /// @author dilusha
    private void loadOverview() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        LecturerMarksOverviewResult result = overviewController.loadOverview(lecturerId, courseCode); //
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        //update available marks type
        markTypeModel.clear();
        for (String type : result.getAllowedMarkTypes()) {
            markTypeModel.addElement(type);
        }

        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode); //get marking scheme based on course
        double caPassMark = scheme.getCaPassMark(); // get passmark of relevent scheme
        double caTotalMark = scheme.getCaWeight(); //get ca weight
        String caRuleText = String.format(
                "CA Eligibility Rule: CA marks should be at least %.2f out of %.2f",
                caPassMark,
                caTotalMark
        );
        String attendanceRuleText = "Attendance + CA Rule: attendance should be at least 80.00% and CA should reach the course CA pass mark";

        caRuleLabel.setText(caRuleText);
        attendanceRuleLabel.setText(attendanceRuleText);
    }

    //Ca mark panel
    private void showCaMarksView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        String selectedScope = (String) scopeComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(selectedScope);
        String studentId = studentSearchField.getText().trim();

        CaMarksResult result = caMarksController.loadCaMarks(courseCode, studentId, individualView);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "CA Marks", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //set table model
        caTable.setModel(createCaMarksModel(result.getColumns()));
        caMarksModel = (DefaultTableModel) caTable.getModel();
        UITheme.styleTable(caTable);
        caMarksModel.setRowCount(0);

        //create table rows
        for (Object[] row : result.getRows()) {
            caMarksModel.addRow(row);
        }

        caViewTitleLabel.setText(result.getTitle());  //set title
        caNoteLabel.setText(result.getNote());  //set note
        centerCardLayout.show(centerCardPanel, "CA");
    }

    /// @author dilusha
    private void showEligibilityByCaView() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        String selectedScope = (String) scopeComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(selectedScope);
        String studentId = studentSearchField.getText().trim();

        CaMarksResult result = caMarksController.loadEndEligibilityByCa(courseCode, studentId, individualView);
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

    //for 3rd button
    /// @author dilusha
    private void showEligibilityByAttendanceAndCaView() {

        //get selected itmens from top panel
        String courseCode = (String) courseComboBox.getSelectedItem();
        String selectedScope = (String) scopeComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(selectedScope);
        String studentId = studentSearchField.getText().trim();


        CaMarksResult result = caMarksController.loadEndEligibilityByAttendanceAndCa(courseCode, studentId, individualView);
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
        String selectedScope = (String) scopeComboBox.getSelectedItem();
        boolean individualView = "Individual Student".equals(selectedScope);
        String studentId = studentSearchField.getText().trim();

        CaMarksResult result = caMarksController.loadFinalMarks(courseCode, studentId, individualView);
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

    //create table and set editable false
    /// @author dilusha
    private DefaultTableModel createCaMarksModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /// @author dilusha
    private void updateSearchState() {
        String selectedScope = (String) scopeComboBox.getSelectedItem();
        boolean individual = "Individual Student".equals(selectedScope);

        studentSearchField.setEnabled(individual);
        if (!individual) {
            studentSearchField.setText("");
        }
    }

    private JPanel createActionCard(String title, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        Dimension cardSize = new Dimension(360, 68);

        card.setBackground(CARD_COLOR);
        card.setPreferredSize(cardSize);
        card.setMaximumSize(cardSize);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);

        label.setForeground(Color.WHITE);
        label.setFont(labelFont);
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
