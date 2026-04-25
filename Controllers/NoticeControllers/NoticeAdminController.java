package Controllers.NoticeControllers;

import DAO.AdminDAO;
import DAO.NoticeDAO;
import Models.Notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdminController {
    private final AdminDAO adminDAO = new AdminDAO();
    private final NoticeDAO noticeDAO = new NoticeDAO();

    public NoticeOperationResult createNotice(NoticeFormData formData) {
        String roles = buildRoles(formData);
        if (formData.getTitle().isEmpty() || formData.getContent().isEmpty() || roles.isEmpty()) {
            return new NoticeOperationResult(false, "Please fill all fields!");
        }

        try {
            String path = NoticeFileSupport.saveContent(formData.getTitle(), formData.getContent());
            boolean saved = adminDAO.addNotice(roles, formData.getTitle(), path);
            if (saved) {
                return new NoticeOperationResult(true, "Notice saved to file and database successfully!");
            }
            return new NoticeOperationResult(false, "Database Error!");
        } catch (IOException ex) {
            return new NoticeOperationResult(false, "Error saving file: " + ex.getMessage());
        }
    }

    public NoticeOperationResult updateNotice(int noticeId, NoticeFormData formData) {
        String roles = buildRoles(formData);
        if (formData.getTitle().isEmpty() || formData.getContent().isEmpty() || roles.isEmpty()) {
            return new NoticeOperationResult(false, "Please fill all fields!");
        }

        try {
            String path = NoticeFileSupport.saveContent(formData.getTitle(), formData.getContent());
            boolean updated = noticeDAO.updateNotice(noticeId, roles, formData.getTitle(), path);
            if (updated) {
                return new NoticeOperationResult(true, "Notice updated successfully!");
            }
            return new NoticeOperationResult(false, "Database Error!");
        } catch (IOException ex) {
            return new NoticeOperationResult(false, "Error saving file: " + ex.getMessage());
        }
    }

    public List<NoticeAdminRow> getAllNotices() {
        List<Object[]> data = adminDAO.getAllNotices();
        List<NoticeAdminRow> rows = new ArrayList<>();
        for (Object[] row : data) {
            rows.add(new NoticeAdminRow(
                    (Integer) row[0],
                    String.valueOf(row[1]),
                    String.valueOf(row[2]),
                    row[3]
            ));
        }
        return rows;
    }

    public NoticeOperationResult deleteNotice(int noticeId, String title) {
        String path = adminDAO.getNoticeContentPath(noticeId);
        if (path == null || path.isBlank()) {
            return new NoticeOperationResult(false, "Notice content not found.");
        }

        boolean deleted = adminDAO.deleteNotice(noticeId, path);
        if (deleted) {
            return new NoticeOperationResult(true, "Deleted successfully!");
        }
        return new NoticeOperationResult(false, "Unable to delete " + title + ".");
    }

    public NoticeContentResult loadNoticeContent(int noticeId) {
        String path = adminDAO.getNoticeContentPath(noticeId);
        return NoticeFileSupport.readContent(path);
    }

    public NoticeDetailsResult getNoticeDetails(int noticeId) {
        Notice notice = noticeDAO.getNoticeById(noticeId);
        if (notice == null) {
            return new NoticeDetailsResult(null, null, "Notice not found.");
        }

        NoticeContentResult contentResult = NoticeFileSupport.readContent(notice.getFilePath());
        if (!contentResult.isSuccess()) {
            return new NoticeDetailsResult(notice, null, contentResult.getMessage());
        }

        return new NoticeDetailsResult(notice, contentResult.getContent(), null);
    }

    private String buildRoles(NoticeFormData formData) {
        List<String> roles = new ArrayList<>();
        if (formData.isLecturerSelected()) {
            roles.add("Lecturer");
        }
        if (formData.isTechnicalSelected()) {
            roles.add("Technical Officer");
        }
        if (formData.isUndergraduateSelected()) {
            roles.add("Undergraduate");
        }
        return String.join(",", roles);
    }
}
