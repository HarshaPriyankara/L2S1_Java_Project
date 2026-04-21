package GUI.admin;

import DAO.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// ENCAPSULATION & INHERITANCE: CreateNotice acts as a base class. 
// Relevant UI elements are declared as 'protected' so subclasses (UpdateNotice) can inherit and access them.
public class CreateNotice extends JPanel {

    protected JTextField titleField;
    protected JTextArea contentArea;
    protected JCheckBox chkLecturer, chkTechnical, chkUndergrad;
    protected NoticeManagementPanel parentPanel;
    protected JButton btnSubmit;

    public CreateNotice(NoticeManagementPanel parentPanel) {
        this.parentPanel = parentPanel;
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
        String title = titleField.getText();
        String content = contentArea.getText();

        StringBuilder targetRole = new StringBuilder();
        if (chkLecturer.isSelected()) targetRole.append("Lecturer,");
        if (chkTechnical.isSelected()) targetRole.append("Technical Officer,");
        if (chkUndergrad.isSelected()) targetRole.append("Undergraduate,");

        String finalRoles = targetRole.toString();
        if (finalRoles.endsWith(",")) {
            finalRoles = finalRoles.substring(0, finalRoles.length() - 1);
        }

        if (title.isEmpty() || content.isEmpty() || finalRoles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            String finalPathForDB = saveToFileAndReturnPath(title, content);

            // Using polymorphic database saving method
            boolean isSaved = saveToDatabase(finalRoles, title, finalPathForDB);

            if (isSaved) {
                JOptionPane.showMessageDialog(this, getSuccessMessage());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    protected String saveToFileAndReturnPath(String title, String content) throws IOException {
        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "_");
        String folderPath = "notices/" + sanitizedTitle;
        String fileName = sanitizedTitle + ".txt";
        String finalPathForDB = folderPath + "/" + fileName;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
        return finalPathForDB;
    }

    // POLYMORPHISM (Runtime): Can be overridden later to alter DB behavior
    protected boolean saveToDatabase(String roles, String title, String path) {
        DAO.AdminDAO adminDAO = new DAO.AdminDAO();
        return adminDAO.addNotice(roles, title, path);
    }

    // POLYMORPHISM
    protected String getSuccessMessage() {
        return "Notice saved to file and Database successfully!";
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
