package Controllers.NoticeControllers;

import DAO.NoticeDAO;
import Models.Notice;

import java.util.List;

public class NoticeViewController {
    private final NoticeDAO noticeDAO = new NoticeDAO();

    public List<Notice> getNoticesByRole(String role) {
        return noticeDAO.getNoticesByRole(role);
    }

    public NoticeContentResult loadNoticeContent(int noticeId) {
        String pathText = noticeDAO.getNoticeContentPath(noticeId);
        return NoticeFileSupport.readContent(pathText);
    }
}
