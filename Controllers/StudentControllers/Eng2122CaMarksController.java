package Controllers.StudentControllers;

import DAO.AttendanceDAO;
import DAO.MarkDAO;
import Utils.CourseMarkScheme;
import Utils.MarksCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

public class Eng2122CaMarksController {
    private static final double ATTENDANCE_ELIGIBILITY_PERCENT = 80.0;
    private static final String[] ENG2122_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Assignment 1",
            "Assignment 2", "Assignment Part (10)", "Mid Theory", "Mid Part (20)", "CA Marks (40)", "CA Eligible"
    };

    private static final String[] ICT2113_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Assignment 1",
            "Assignment 2", "Assignment Part (10)", "Mid Practical", "Practical Part (20)", "CA Marks (40)", "CA Eligible"
    };

    private static final String[] ICT2122_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Mid Theory",
            "Theory Part (20)", "CA Marks (30)", "CA Eligible"
    };

    private static final String[] ICT2132_COLUMNS = {
            "Reg No", "Mid Practical", "Practical Part (10)", "Mini Project",
            "Project Part (30)", "CA Marks (40)", "CA Eligible"
    };

    private static final String[] ICT2142_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Assignment 1",
            "Assignment 2", "Assignment Part (20)", "CA Marks (30)", "CA Eligible"
    };

    private static final String[] ICT2152_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Mini Project",
            "Project Part (20)", "Assignment 1", "Assignment 2", "Assignment Part (10)", "CA Marks (40)", "CA Eligible"
    };

    private static final String[] TCS2112_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Assignment 1",
            "Assignment 2", "Assignment Part (10)", "Mid Theory", "Theory Part (10)", "CA Marks (30)", "CA Eligible"
    };

    private static final String[] TCS2122_COLUMNS = {
            "Reg No", "Quiz 1", "Quiz 2", "Quiz 3", "Quiz Part (10)", "Assignment 1",
            "Assignment 2", "Assignment Part (20)", "CA Marks (30)", "Final Result Base"
    };

    private final MarkDAO markDAO = new MarkDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public Eng2122CaMarksResult loadCaMarks(String courseCode, String studentId, boolean individualView) {
        if (!supportsCourse(courseCode)) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "This step supports CA logic for ENG2122, ICT2113, ICT2122, ICT2132, ICT2142, ICT2152, TCS2112, and TCS2122 only.");
        }

        try {
            List<Object[]> rows = new ArrayList<>();
            if (individualView) {
                if (studentId == null || studentId.isBlank()) {
                    return new Eng2122CaMarksResult(null, null, null, null, "Please enter a registration number.");
                }
                Map<String, Double> marks = markDAO.getStudentCourseMarks(studentId.trim(), courseCode);
                rows.add(buildRow(courseCode, studentId.trim(), marks));
            } else {
                Map<String, Map<String, Double>> groupedMarks = markDAO.getCourseMarksByStudent(courseCode);
                for (Map.Entry<String, Map<String, Double>> entry : groupedMarks.entrySet()) {
                    rows.add(buildRow(courseCode, entry.getKey(), entry.getValue()));
                }
            }
            return new Eng2122CaMarksResult(
                    resolveColumns(courseCode),
                    rows,
                    buildTitle(courseCode, studentId, individualView),
                    buildNote(courseCode),
                    null
            );
        } catch (Exception ex) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "Unable to load " + courseCode + " CA marks: " + ex.getMessage());
        }
    }

    public Eng2122CaMarksResult loadEndEligibilityByCa(String courseCode, String studentId, boolean individualView) {
        if (!supportsCourse(courseCode)) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "This step supports selected configured courses only.");
        }

        try {
            List<Object[]> rows = new ArrayList<>();
            if (individualView) {
                if (studentId == null || studentId.isBlank()) {
                    return new Eng2122CaMarksResult(null, null, null, null, "Please enter a registration number.");
                }
                Map<String, Double> marks = markDAO.getStudentCourseMarks(studentId.trim(), courseCode);
                rows.add(buildEligibilityRow(courseCode, studentId.trim(), marks));
            } else {
                Map<String, Map<String, Double>> groupedMarks = markDAO.getCourseMarksByStudent(courseCode);
                for (Map.Entry<String, Map<String, Double>> entry : groupedMarks.entrySet()) {
                    rows.add(buildEligibilityRow(courseCode, entry.getKey(), entry.getValue()));
                }
            }

            return new Eng2122CaMarksResult(
                    new String[]{"Reg No", "CA Marks", "Required CA Mark", "End Assessment", "Eligibility"},
                    rows,
                    buildEligibilityTitle(courseCode, studentId, individualView),
                    buildEligibilityNote(courseCode),
                    null
            );
        } catch (Exception ex) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "Unable to load end exam eligibility by CA: " + ex.getMessage());
        }
    }

    public Eng2122CaMarksResult loadFinalMarks(String courseCode, String studentId, boolean individualView) {
        if (!"ENG2122".equalsIgnoreCase(courseCode)
                && !"ICT2113".equalsIgnoreCase(courseCode)
                && !"ICT2122".equalsIgnoreCase(courseCode)
                && !"ICT2132".equalsIgnoreCase(courseCode)
                && !"ICT2142".equalsIgnoreCase(courseCode)
                && !"ICT2152".equalsIgnoreCase(courseCode)
                && !"TCS2112".equalsIgnoreCase(courseCode)
                && !"TCS2122".equalsIgnoreCase(courseCode)) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "This step implements final marks logic for ENG2122, ICT2113, ICT2122, ICT2132, ICT2142, ICT2152, TCS2112, and TCS2122 only.");
        }

        try {
            List<Object[]> rows = new ArrayList<>();
            if (individualView) {
                if (studentId == null || studentId.isBlank()) {
                    return new Eng2122CaMarksResult(null, null, null, null, "Please enter a registration number.");
                }
                MarksCalculator.MarkBreakdown breakdown = markDAO.getStudentCourseBreakdown(studentId.trim(), courseCode);
                if (breakdown != null) {
                    rows.add(buildFinalMarksRow(breakdown));
                }
            } else {
                for (MarksCalculator.MarkBreakdown breakdown : markDAO.getCourseMarkBreakdowns(courseCode)) {
                    rows.add(buildFinalMarksRow(breakdown));
                }
            }

            return new Eng2122CaMarksResult(
                    resolveFinalMarksColumns(courseCode),
                    rows,
                    buildFinalMarksTitle(courseCode, studentId, individualView),
                    buildFinalMarksNote(courseCode),
                    null
            );
        } catch (Exception ex) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "Unable to load final marks: " + ex.getMessage());
        }
    }

    public Eng2122CaMarksResult loadEndEligibilityByAttendanceAndCa(String courseCode, String studentId, boolean individualView) {
        if (!supportsCourse(courseCode)) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "This step supports selected configured courses only.");
        }

        try {
            CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
            if (!scheme.hasEndAssessment()) {
                return buildNoEndExamAttendanceEligibility(courseCode, studentId, individualView, scheme);
            }

            List<Object[]> rows = new ArrayList<>();
            if (individualView) {
                if (studentId == null || studentId.isBlank()) {
                    return new Eng2122CaMarksResult(null, null, null, null, "Please enter a registration number.");
                }
                rows.add(buildAttendanceAndCaRow(courseCode, studentId.trim()));
            } else {
                List<AttendanceSummary> summaries = new ArrayList<>();
                try (ResultSet summaryRs = attendanceDAO.getCourseAttendanceSummary(courseCode, null, "Overall")) {
                    while (summaryRs.next()) {
                        summaries.add(new AttendanceSummary(
                                summaryRs.getString("Reg_no"),
                                calculateAttendancePercentage(summaryRs.getDouble("attended_hours"), summaryRs.getDouble("total_hours"))
                        ));
                    }
                }
                for (AttendanceSummary summary : summaries) {
                    rows.add(buildAttendanceAndCaRow(courseCode, summary.regNo, summary.attendancePercentage));
                }
            }

            return new Eng2122CaMarksResult(
                    new String[]{"Reg No", "Attendance %", "Required Attendance %", "CA Marks", "Required CA Mark", "End Assessment", "Eligibility"},
                    rows,
                    buildAttendanceEligibilityTitle(courseCode, studentId, individualView),
                    buildAttendanceEligibilityNote(courseCode),
                    null
            );
        } catch (Exception ex) {
            return new Eng2122CaMarksResult(null, null, null, null,
                    "Unable to load end exam eligibility by attendance + CA: " + ex.getMessage());
        }
    }

    private Object[] buildRow(String courseCode, String regNo, Map<String, Double> marks) {
        double passMark = CourseMarkScheme.forCourse(courseCode).getCaPassMark();
        if ("ICT2113".equalsIgnoreCase(courseCode)) {
            return buildIct2113Row(regNo, marks, passMark);
        }
        if ("ICT2122".equalsIgnoreCase(courseCode)) {
            return buildIct2122Row(regNo, marks, passMark);
        }
        if ("ICT2132".equalsIgnoreCase(courseCode)) {
            return buildIct2132Row(regNo, marks, passMark);
        }
        if ("ICT2142".equalsIgnoreCase(courseCode)) {
            return buildIct2142Row(regNo, marks, passMark);
        }
        if ("ICT2152".equalsIgnoreCase(courseCode)) {
            return buildIct2152Row(regNo, marks, passMark);
        }
        if ("TCS2112".equalsIgnoreCase(courseCode)) {
            return buildTcs2112Row(regNo, marks, passMark);
        }
        if ("TCS2122".equalsIgnoreCase(courseCode)) {
            return buildTcs2122Row(regNo, marks, passMark);
        }
        return buildEng2122Row(regNo, marks, passMark);
    }

    private Object[] buildEligibilityRow(String courseCode, String regNo, Map<String, Double> marks) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        double caMarks = round(scheme.calculateCA(marks));
        double passMark = scheme.getCaPassMark();
        String endAssessment = resolveEndAssessmentLabel(courseCode);

        String eligibility;
        if (!scheme.hasEndAssessment()) {
            eligibility = "No End Exam";
        } else {
            eligibility = caMarks >= passMark ? "Eligible" : "Not Eligible";
        }

        return new Object[]{regNo, caMarks, passMark, endAssessment, eligibility};
    }

    private Object[] buildFinalMarksRow(MarksCalculator.MarkBreakdown breakdown) {
        if ("TCS2122".equalsIgnoreCase(breakdown.getCourseCode())) {
            return new Object[]{
                    breakdown.getRegNo(),
                    breakdown.getCaMarks(),
                    "No End Exam",
                    breakdown.hasMarks() ? breakdown.getCaMarks() : "-",
                    breakdown.getGrade()
            };
        }
        if ("ICT2113".equalsIgnoreCase(breakdown.getCourseCode())
                || "ICT2132".equalsIgnoreCase(breakdown.getCourseCode())) {
            return new Object[]{
                    breakdown.getRegNo(),
                    breakdown.getCaMarks(),
                    breakdown.getEndMarks(),
                    breakdown.hasMarks() ? breakdown.getTotalMarks() : "-",
                    breakdown.getGrade()
            };
        }
        return new Object[]{
                breakdown.getRegNo(),
                breakdown.getCaMarks(),
                breakdown.getEndMarks(),
                breakdown.hasMarks() ? breakdown.getTotalMarks() : "-",
                breakdown.getGrade()
        };
    }

    private Eng2122CaMarksResult buildNoEndExamAttendanceEligibility(String courseCode, String studentId,
                                                                      boolean individualView, CourseMarkScheme scheme) throws Exception {
        List<Object[]> rows = new ArrayList<>();

        if (individualView) {
            if (studentId == null || studentId.isBlank()) {
                return new Eng2122CaMarksResult(null, null, null, null, "Please enter a registration number.");
            }
            Map<String, Double> marks = markDAO.getStudentCourseMarks(studentId.trim(), courseCode);
            rows.add(new Object[]{
                    studentId.trim(),
                    "-",
                    ATTENDANCE_ELIGIBILITY_PERCENT,
                    round(scheme.calculateCA(marks)),
                    scheme.getCaPassMark(),
                    "No End Exam",
                    "No End Exam"
            });
        } else {
            Map<String, Map<String, Double>> groupedMarks = markDAO.getCourseMarksByStudent(courseCode);
            for (Map.Entry<String, Map<String, Double>> entry : groupedMarks.entrySet()) {
                rows.add(new Object[]{
                        entry.getKey(),
                        "-",
                        ATTENDANCE_ELIGIBILITY_PERCENT,
                        round(scheme.calculateCA(entry.getValue())),
                        scheme.getCaPassMark(),
                        "No End Exam",
                        "No End Exam"
                });
            }
        }

        return new Eng2122CaMarksResult(
                new String[]{"Reg No", "Attendance %", "Required Attendance %", "CA Marks", "Required CA Mark", "End Assessment", "Eligibility"},
                rows,
                buildAttendanceEligibilityTitle(courseCode, studentId, individualView),
                courseCode.toUpperCase() + " has no end exam. Attendance + CA end-exam eligibility is not required for this subject.",
                null
        );
    }

    private Object[] buildAttendanceAndCaRow(String courseCode, String regNo) throws Exception {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        double caMarks = round(scheme.calculateCA(markDAO.getStudentCourseMarks(regNo, courseCode)));
        double attendancePercentage = loadAttendancePercentage(courseCode, regNo);
        return buildAttendanceAndCaRow(courseCode, regNo, caMarks, attendancePercentage);
    }

    private Object[] buildAttendanceAndCaRow(String courseCode, String regNo, double attendancePercentage) throws Exception {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        double caMarks = round(scheme.calculateCA(markDAO.getStudentCourseMarks(regNo, courseCode)));
        return buildAttendanceAndCaRow(courseCode, regNo, caMarks, attendancePercentage);
    }

    private Object[] buildAttendanceAndCaRow(String courseCode, String regNo, double caMarks, double attendancePercentage) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        boolean attendanceEligible = attendancePercentage >= ATTENDANCE_ELIGIBILITY_PERCENT;
        boolean caEligible = caMarks >= scheme.getCaPassMark();

        return new Object[]{
                regNo,
                attendancePercentage,
                ATTENDANCE_ELIGIBILITY_PERCENT,
                caMarks,
                scheme.getCaPassMark(),
                resolveEndAssessmentLabel(courseCode),
                attendanceEligible && caEligible ? "Eligible" : "Not Eligible"
        };
    }

    private double loadAttendancePercentage(String courseCode, String regNo) throws Exception {
        try (ResultSet summaryRs = attendanceDAO.getCourseAttendanceSummary(courseCode, regNo, "Overall")) {
            if (!summaryRs.next()) {
                return 0.0;
            }

            return calculateAttendancePercentage(summaryRs.getDouble("attended_hours"), summaryRs.getDouble("total_hours"));
        }
    }

    private double calculateAttendancePercentage(double attendedHours, double totalHours) {
        if (totalHours <= 0) {
            return 0.0;
        }
        return round((attendedHours / totalHours) * 100.0);
    }

    private Object[] buildEng2122Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");
        double midTheory = valueOf(marks, "Mid_theory");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 10.0 / 100.0);
        double midContribution = round(midTheory * 20.0 / 100.0);
        double caMarks = round(quizContribution + assignmentContribution + midContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                display(marks, "Mid_theory", midTheory),
                midContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildIct2113Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");
        double midPractical = valueOf(marks, "Mid_practical");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 10.0 / 100.0);
        double practicalContribution = round(midPractical * 20.0 / 100.0);
        double caMarks = round(quizContribution + assignmentContribution + practicalContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                display(marks, "Mid_practical", midPractical),
                practicalContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildIct2122Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double midTheory = valueOf(marks, "Mid_theory");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double theoryContribution = round(midTheory * 20.0 / 100.0);
        double caMarks = round(quizContribution + theoryContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Mid_theory", midTheory),
                theoryContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildIct2132Row(String regNo, Map<String, Double> marks, double passMark) {
        double midPractical = valueOf(marks, "Mid_practical");
        double miniProject = valueOf(marks, "Mini_project");

        double practicalContribution = round(midPractical * 10.0 / 100.0);
        double projectContribution = round(miniProject * 30.0 / 100.0);
        double caMarks = round(practicalContribution + projectContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Mid_practical", midPractical),
                practicalContribution,
                display(marks, "Mini_project", miniProject),
                projectContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildIct2142Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 20.0 / 100.0);
        double caMarks = round(quizContribution + assignmentContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildIct2152Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double miniProject = valueOf(marks, "Mini_project");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double projectContribution = round(miniProject * 20.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 10.0 / 100.0);
        double caMarks = round(quizContribution + projectContribution + assignmentContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Mini_project", miniProject),
                projectContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildTcs2112Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");
        double midTheory = valueOf(marks, "Mid_theory");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 10.0 / 100.0);
        double theoryContribution = round(midTheory * 10.0 / 100.0);
        double caMarks = round(quizContribution + assignmentContribution + theoryContribution);
        String eligible = caMarks >= passMark ? "Eligible" : "Not Eligible";

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                display(marks, "Mid_theory", midTheory),
                theoryContribution,
                caMarks,
                eligible
        };
    }

    private Object[] buildTcs2122Row(String regNo, Map<String, Double> marks, double passMark) {
        double quiz1 = valueOf(marks, "Quiz_1");
        double quiz2 = valueOf(marks, "Quiz_2");
        double quiz3 = valueOf(marks, "Quiz_3");
        double assignment1 = valueOf(marks, "Assignment_1");
        double assignment2 = valueOf(marks, "Assignment_2");

        double quizContribution = round(bestTwoAverage(quiz1, quiz2, quiz3) * 10.0 / 100.0);
        double assignmentContribution = round(((assignment1 + assignment2) / 2.0) * 20.0 / 100.0);
        double caMarks = round(quizContribution + assignmentContribution);

        return new Object[]{
                regNo,
                display(marks, "Quiz_1", quiz1),
                display(marks, "Quiz_2", quiz2),
                display(marks, "Quiz_3", quiz3),
                quizContribution,
                display(marks, "Assignment_1", assignment1),
                display(marks, "Assignment_2", assignment2),
                assignmentContribution,
                caMarks,
                caMarks >= passMark ? "CA-based final result" : "CA below pass limit"
        };
    }

    private boolean supportsCourse(String courseCode) {
        return "ENG2122".equalsIgnoreCase(courseCode)
                || "ICT2113".equalsIgnoreCase(courseCode)
                || "ICT2122".equalsIgnoreCase(courseCode)
                || "ICT2132".equalsIgnoreCase(courseCode)
                || "ICT2142".equalsIgnoreCase(courseCode)
                || "ICT2152".equalsIgnoreCase(courseCode)
                || "TCS2112".equalsIgnoreCase(courseCode)
                || "TCS2122".equalsIgnoreCase(courseCode);
    }

    private String[] resolveColumns(String courseCode) {
        if ("ICT2113".equalsIgnoreCase(courseCode)) {
            return ICT2113_COLUMNS;
        }
        if ("ICT2122".equalsIgnoreCase(courseCode)) {
            return ICT2122_COLUMNS;
        }
        if ("ICT2132".equalsIgnoreCase(courseCode)) {
            return ICT2132_COLUMNS;
        }
        if ("ICT2142".equalsIgnoreCase(courseCode)) {
            return ICT2142_COLUMNS;
        }
        if ("ICT2152".equalsIgnoreCase(courseCode)) {
            return ICT2152_COLUMNS;
        }
        if ("TCS2112".equalsIgnoreCase(courseCode)) {
            return TCS2112_COLUMNS;
        }
        if ("TCS2122".equalsIgnoreCase(courseCode)) {
            return TCS2122_COLUMNS;
        }
        return ENG2122_COLUMNS;
    }

    private String buildTitle(String courseCode, String studentId, boolean individualView) {
        return individualView
                ? courseCode.toUpperCase() + " CA Marks - " + studentId
                : courseCode.toUpperCase() + " CA Marks - Whole Batch";
    }

    private String buildEligibilityTitle(String courseCode, String studentId, boolean individualView) {
        return individualView
                ? courseCode.toUpperCase() + " End Exam Eligibility by CA - " + studentId
                : courseCode.toUpperCase() + " End Exam Eligibility by CA - Whole Batch";
    }

    private String buildFinalMarksTitle(String courseCode, String studentId, boolean individualView) {
        return individualView
                ? courseCode.toUpperCase() + " Final Marks (CA + END) - " + studentId
                : courseCode.toUpperCase() + " Final Marks (CA + END) - Whole Batch";
    }

    private String buildAttendanceEligibilityTitle(String courseCode, String studentId, boolean individualView) {
        return individualView
                ? courseCode.toUpperCase() + " End Exam Eligibility by Attendance + CA - " + studentId
                : courseCode.toUpperCase() + " End Exam Eligibility by Attendance + CA - Whole Batch";
    }

    private String[] resolveFinalMarksColumns(String courseCode) {
        if ("TCS2122".equalsIgnoreCase(courseCode)) {
            return new String[]{"Reg No", "CA Marks", "End Exam", "Final Marks", "Grade"};
        }
        if ("ICT2113".equalsIgnoreCase(courseCode) || "ICT2132".equalsIgnoreCase(courseCode)) {
            return new String[]{"Reg No", "CA Marks", "End Marks", "Final Marks", "Grade"};
        }
        return new String[]{"Reg No", "CA Marks", "End Theory", "Final Marks", "Grade"};
    }

    private String buildFinalMarksNote(String courseCode) {
        if ("ICT2113".equalsIgnoreCase(courseCode)) {
            return "ICT2113 final marks reuse the existing course breakdown logic: CA (40) + End Theory/Practical (60).";
        }
        if ("ICT2132".equalsIgnoreCase(courseCode)) {
            return "ICT2132 final marks reuse the existing course breakdown logic: CA (40) + End Practical (60).";
        }
        if ("ICT2142".equalsIgnoreCase(courseCode)) {
            return "ICT2142 final marks reuse the existing course breakdown logic: CA (30) + End Theory (70).";
        }
        if ("ICT2152".equalsIgnoreCase(courseCode)) {
            return "ICT2152 final marks reuse the existing course breakdown logic: CA (40) + End Theory (60).";
        }
        if ("TCS2112".equalsIgnoreCase(courseCode)) {
            return "TCS2112 final marks reuse the existing course breakdown logic: CA (30) + End Theory (70).";
        }
        if ("TCS2122".equalsIgnoreCase(courseCode)) {
            return "TCS2122 final marks reuse the existing course breakdown logic: no end exam, final result is based on CA only.";
        }
        if ("ICT2122".equalsIgnoreCase(courseCode)) {
            return "ICT2122 final marks reuse the existing course breakdown logic: CA (30) + End Theory (70).";
        }
        return "ENG2122 final marks reuse the existing course breakdown logic: CA (40) + End Theory (60).";
    }

    private String buildNote(String courseCode) {
        if ("ICT2113".equalsIgnoreCase(courseCode)) {
            return "ICT2113 rule: Best 2 quizzes -> 10%, Assignment 1 & 2 -> 10%, Mid Practical -> 20%";
        }
        if ("ICT2122".equalsIgnoreCase(courseCode)) {
            return "ICT2122 rule: Best 2 quizzes -> 10%, Mid Theory -> 20%";
        }
        if ("ICT2132".equalsIgnoreCase(courseCode)) {
            return "ICT2132 rule: Mid Practical -> 10%, Mini Project -> 30%";
        }
        if ("ICT2142".equalsIgnoreCase(courseCode)) {
            return "ICT2142 rule: Best 2 quizzes -> 10%, Assignment 1 & 2 -> 20%";
        }
        if ("ICT2152".equalsIgnoreCase(courseCode)) {
            return "ICT2152 rule: Best 2 quizzes -> 10%, Mini Project -> 20%, Assignment 1 & 2 -> 10%";
        }
        if ("TCS2112".equalsIgnoreCase(courseCode)) {
            return "TCS2112 rule: Best 2 quizzes -> 10%, Assignment 1 & 2 -> 10%, Mid Theory -> 10%";
        }
        if ("TCS2122".equalsIgnoreCase(courseCode)) {
            return "TCS2122 rule: Best 2 quizzes -> 10%, Assignment 1 & 2 -> 20%, no end exam";
        }
        return "ENG2122 rule: Best 2 quizzes -> 10%, Assignment 1 & 2 -> 10%, Mid Theory -> 20%";
    }

    private String buildEligibilityNote(String courseCode) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        if (!scheme.hasEndAssessment()) {
            return courseCode.toUpperCase() + " has no end exam. Final result is based on CA.";
        }
        return String.format("%s eligibility rule: CA marks should be at least %.2f out of %.2f",
                courseCode.toUpperCase(), scheme.getCaPassMark(), scheme.getCaWeight());
    }

    private String buildAttendanceEligibilityNote(String courseCode) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        if (!scheme.hasEndAssessment()) {
            return courseCode.toUpperCase() + " has no end exam. Final result is based on CA only.";
        }
        return String.format("%s attendance + CA rule: attendance should be at least %.2f%% and CA marks should be at least %.2f out of %.2f",
                courseCode.toUpperCase(), ATTENDANCE_ELIGIBILITY_PERCENT, scheme.getCaPassMark(), scheme.getCaWeight());
    }

    private String resolveEndAssessmentLabel(String courseCode) {
        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        if (!scheme.hasEndAssessment()) {
            return "No End Exam";
        }

        boolean theory = false;
        boolean practical = false;
        for (String type : scheme.getAllowedMarkTypes()) {
            if ("End_theory".equals(type)) {
                theory = true;
            }
            if ("End_practical".equals(type)) {
                practical = true;
            }
        }

        if (theory && practical) {
            return "Theory + Practical";
        }
        if (practical) {
            return "Practical";
        }
        return "Theory";
    }

    private double bestTwoAverage(double q1, double q2, double q3) {
        double[] values = {q1, q2, q3};
        java.util.Arrays.sort(values);
        return (values[1] + values[2]) / 2.0;
    }

    private double valueOf(Map<String, Double> marks, String key) {
        if (marks == null) {
            return 0.0;
        }
        Double value = marks.get(key);
        return value == null ? 0.0 : value;
    }

    private Object display(Map<String, Double> marks, String key, double value) {
        return marks != null && marks.containsKey(key) ? value : "-";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static class AttendanceSummary {
        private final String regNo;
        private final double attendancePercentage;

        private AttendanceSummary(String regNo, double attendancePercentage) {
            this.regNo = regNo;
            this.attendancePercentage = attendancePercentage;
        }
    }
}
