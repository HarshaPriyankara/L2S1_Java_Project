package GUI.admin;

import DAO.NoticeDAO;
import Models.Notice;

import javax.swing.*;
import java.awt.*;
import java.io.*;

// INHERITANCE: UpdateNotice is "a kind of" CreateNotice. It inherits all UI layout.
// This prevents rewriting the whole UI panel logic perfectly showing code reusability.
public class UpdateNotice extends CreateNotice {
    
    // ENCAPSULATION
    private int noticeId;

    public UpdateNotice(NoticeManagementPanel parent, int id, String title) {
        // Calling super class constructor to prepare the base UI
        super(parent);
        this.noticeId = id;
        
        // POLYMORPHISM: Adapting the base UI components to fit "Update" instead of "Create"
        this.btnSubmit.setText("Update Notice");
        
        // Adding a descriptive label dynamically
        JLabel lbl = new JLabel("Updating Notice: " + title + " (ID: " + id + ")");
        lbl.setBounds(250, 20, 300, 25);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(new Color(41, 128, 185));
        
        Component mainCon = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (mainCon instanceof JPanel) {
            ((JPanel) mainCon).add(lbl);
        }

        loadNoticeData();
    }

    // ABSTRACTION: Delegating complex database retrieval directly to NoticeDAO 
    private void loadNoticeData() {
        NoticeDAO dao = new NoticeDAO();
        Notice notice = dao.getNoticeById(this.noticeId);
        
        if (notice != null) {
            titleField.setText(notice.getTitle());
            String roles = notice.getTargetRole();
            if (roles != null) {
                if (roles.contains("Lecturer")) chkLecturer.setSelected(true);
                if (roles.contains("Technical Officer")) chkTechnical.setSelected(true);
                if (roles.contains("Undergraduate")) chkUndergrad.setSelected(true);
            }
            
            // Reusing file loading abstraction if possible
            File file = new File(notice.getFilePath());
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    contentArea.read(br, null);
                } catch (IOException e) {
                    contentArea.setText("Error reading file content.");
                }
            }
        }
    }

    // POLYMORPHISM: Overriding the super class Database-Save behavior to do an Update instead 
    @Override
    protected boolean saveToDatabase(String roles, String title, String path) {
        NoticeDAO dao = new NoticeDAO();
        // Uses the newly created updateNotice abstraction
        return dao.updateNotice(this.noticeId, roles, title, path);
    }

    // POLYMORPHISM: Customize success response text
    @Override
    protected String getSuccessMessage() {
        return "Notice updated successfully!";
    }

    // POLYMORPHISM: Change post-action behavior to exit panel
    @Override
    protected void clearFields() {
        super.clearFields();
        parentPanel.showMainButtons(); // Auto redirect after update
    }
}