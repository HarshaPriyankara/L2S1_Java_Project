package GUI.admin;

import Controllers.NoticeControllers.NoticeAdminController;
import Controllers.NoticeControllers.NoticeDetailsResult;
import Controllers.NoticeControllers.NoticeFormData;
import Controllers.NoticeControllers.NoticeOperationResult;
import Models.Notice;

import javax.swing.*;
import java.awt.*;

// INHERITANCE: UpdateNotice is "a kind of" CreateNotice. It inherits all UI layout.
// This prevents rewriting the whole UI panel logic perfectly showing code reusability.
public class UpdateNotice extends CreateNotice {
    
    // ENCAPSULATION
    private int noticeId;

    public UpdateNotice(NoticeManagementPanel parent, int id, String title) {
        // Calling super class constructor to prepare the base UI
        super(parent, new NoticeAdminController());
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
        NoticeDetailsResult result = noticeController.getNoticeDetails(this.noticeId);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Notice notice = result.getNotice();
        if (notice != null) {
            titleField.setText(notice.getTitle());
            String roles = notice.getTargetRole();
            if (roles != null) {
                if (roles.contains("Lecturer")) chkLecturer.setSelected(true);
                if (roles.contains("Technical Officer")) chkTechnical.setSelected(true);
                if (roles.contains("Undergraduate")) chkUndergrad.setSelected(true);
            }
            contentArea.setText(result.getContent());
        }
    }

    @Override
    protected NoticeOperationResult handleNoticeSave(NoticeFormData formData) {
        return noticeController.updateNotice(this.noticeId, formData);
    }

    // POLYMORPHISM: Change post-action behavior to exit panel
    @Override
    protected void clearFields() {
        super.clearFields();
        parentPanel.showMainButtons(); // Auto redirect after update
    }
}
