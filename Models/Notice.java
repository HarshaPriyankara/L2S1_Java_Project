package Models;
import java.time.LocalDate;
import java.util.List;

class Notice {
    private String noticeId;
    private String title;
    private String content;
    private String createdBy;
    private LocalDate createdDate;
    private String targetRole;
    private String departmentId;

 


    public Notice(){
    }


    public void createNotice() {
        
    }

    public static void deleteNotice() {
       
    }


    public void viewNotice() {
       
    }


    public static List<Notice> viewAllNotice() {

        return List.of();
    }
}


