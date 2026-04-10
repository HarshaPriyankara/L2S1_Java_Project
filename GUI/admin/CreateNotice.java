package GUI.admin;

import DAO.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateNotice extends JPanel{

    private JTextField titleField;
    private JTextArea contentArea;
    private JCheckBox chkLecturer, chkTechnical, chkUndergrad;
    private NoticeManagementPanel parentPanel;

    public CreateNotice(NoticeManagementPanel parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());
        add(CreateMainCon(), BorderLayout.CENTER);
    }


    private JPanel CreateMainCon() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(null);
        mainContent.setBackground(new Color(236, 240, 241));



        // --- Title Field ---
        JLabel lblTitle = new JLabel("Notice Title:");
        lblTitle.setBounds(50, 60, 100, 30); // උඩින් Back button එක තියෙන නිසා bounds පොඩ්ඩක් පහත් කළා
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainContent.add(lblTitle);

        titleField = new JTextField(); // මෙන්න මේ initialization ටික තමයි අඩුවෙලා තිබුණේ
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
        scrollPane.setBounds(50, 170, 650, 230); // Height එක පොඩ්ඩක් අඩු කළා roles වලට ඉඩ මදි වෙයි කියලා
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
        JButton btnSubmit = new JButton("Submit & Save");
        btnSubmit.setBounds(50, 460, 150, 40);
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> saveNoticeAction());
        mainContent.add(btnSubmit);

        // --- කලින් තිබ්බ ඔක්කොම Labels/Fields (Bounds වෙනස් නොකර) ---
        // ... (titleField, contentArea, Checkboxes ටික කලින් විදියටම මෙතන තියෙනවා) ...

        // --- Back Button එක (අලුතින් එකතු කරනවා) ---
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 20, 80, 25); // උඩින් පොඩි ඉඩක දාමු
        btnBack.setBackground(new Color(149, 165, 166));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            parentPanel.showMainButtons(); // කලින් පැනල් එකේ බට්න් ටික ආපහු පෙන්වන්න කියනවා
        });
        mainContent.add(btnBack);



        return mainContent;
    }





    private void saveNoticeAction() {
        String title = titleField.getText();
        String content = contentArea.getText();

        // 1. Roles ටික එකතු කරගන්නවා
        StringBuilder targetRole = new StringBuilder();
        if (chkLecturer.isSelected()) targetRole.append("Lecturer,");
        if (chkTechnical.isSelected()) targetRole.append("Technical Officer,");
        if (chkUndergrad.isSelected()) targetRole.append("Undergraduate,");

        String finalRoles = targetRole.toString();
        if (finalRoles.endsWith(",")) {
            finalRoles = finalRoles.substring(0, finalRoles.length() - 1);
        }

        // Validation - හිස්නම් error එකක් දෙනවා
        if (title.isEmpty() || content.isEmpty() || finalRoles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            // 2. File එක Save කරන කොටස
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

            // 3. AdminDAO එක හරහා DATABASE එකට දාන කොටස
            DAO.AdminDAO adminDAO = new DAO.AdminDAO();
            boolean isSaved = adminDAO.addNotice(finalRoles, title, finalPathForDB);

            if (isSaved) {
                JOptionPane.showMessageDialog(this, "Notice saved to file and Database successfully!");
                // Fields ටික clear කරනවා
                titleField.setText("");
                contentArea.setText("");
                chkLecturer.setSelected(false);
                chkTechnical.setSelected(false);
                chkUndergrad.setSelected(false);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
