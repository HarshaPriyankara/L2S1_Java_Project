package GUI.admin;

import Controllers.NoticeControllers.NoticeAdminController;
import Controllers.NoticeControllers.NoticeFormData;
import Controllers.NoticeControllers.NoticeOperationResult;

import javax.swing.*;
import java.awt.*;

// ENCAPSULATION & INHERITANCE: CreateNotice acts as a base class. 
// Relevant UI elements are declared as 'protected' so subclasses (UpdateNotice) can inherit and access them.
public class CreateNotice extends JPanel {

    protected JTextField titleField;
    protected JTextArea contentArea;
    protected JCheckBox chkLecturer, chkTechnical, chkUndergrad;
    protected NoticeManagementPanel parentPanel;
    protected JButton btnSubmit;
    protected final NoticeAdminController noticeController;

    public CreateNotice(NoticeManagementPanel parentPanel) {
        this(parentPanel, new NoticeAdminController());
    }

    protected CreateNotice(NoticeManagementPanel parentPanel, NoticeAdminController noticeController) {
        this.parentPanel = parentPanel;
        this.noticeController = noticeController;
        setLayout(new BorderLayout());
        add(CreateMainCon(), BorderLayout.CENTER);
    }

    protected JPanel CreateMainCon() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(null);
        mainContent.setBackground(new Color(236, 240, 241));

        // --- Title Field ---
        JLabel lblTitle = new JLabel("Notice Title:");
        lblTitle.setBounds(50, 60, 100, 30);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblTitle);

        titleField = new JTextField();
        titleField.setBounds(50, 90, 650, 35);
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainContent.add(titleField);

        // --- Content Area ---
        JLabel lblContent = new JLabel("Notice Content:");
        lblContent.setBounds(50, 140, 150, 30);
        lblContent.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblContent);

        contentArea = new JTextArea();
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBounds(50, 170, 650, 230);
        mainContent.add(scrollPane);

        // --- Target Roles ---
        JLabel lblTarget = new JLabel("Target Roles:");
        lblTarget.setBounds(50, 410, 100, 30);
        lblTarget.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblTarget);

        chkLecturer = new JCheckBox("Lecturer");
        chkLecturer.setBounds(150, 410, 100, 30);
        chkLecturer.setBackground(new Color(236, 240, 241));
        mainContent.add(chkLecturer);

        chkTechnical = new JCheckBox("Technical Officer");
        chkTechnical.setBounds(260, 410, 150, 30);
        chkTechnical.setBackground(new Color(236, 240, 241));
        mainContent.add(chkTechnical);

        chkUndergrad = new JCheckBox("Undergraduate");
        chkUndergrad.setBounds(410, 410, 150, 30);
        chkUndergrad.setBackground(new Color(236, 240, 241));
        mainContent.add(chkUndergrad);

        // --- Submit Button ---
        btnSubmit = new JButton("Submit & Save");
        btnSubmit.setBounds(50, 460, 150, 40);
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> saveNoticeAction());
        mainContent.add(btnSubmit);

        // --- Back Button ---
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 20, 80, 25);
        btnBack.setBackground(new Color(149, 165, 166));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            parentPanel.showMainButtons();
        });
        mainContent.add(btnBack);

        return mainContent;
    }

    // POLYMORPHISM: This method is designed to be overridden by subclasses.
    protected void saveNoticeAction() {
        NoticeOperationResult result = handleNoticeSave(buildFormData());
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage());
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected NoticeFormData buildFormData() {
        return new NoticeFormData(
                titleField.getText(),
                contentArea.getText(),
                chkLecturer.isSelected(),
                chkTechnical.isSelected(),
                chkUndergrad.isSelected()
        );
    }

    protected NoticeOperationResult handleNoticeSave(NoticeFormData formData) {
        return noticeController.createNotice(formData);
    }

    // Reusability
    protected void clearFields() {
        titleField.setText("");
        contentArea.setText("");
        chkLecturer.setSelected(false);
        chkTechnical.setSelected(false);
        chkUndergrad.setSelected(false);
    }
}
