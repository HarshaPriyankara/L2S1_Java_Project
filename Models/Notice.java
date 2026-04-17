package Models;

public class Notice {
   private int id;
   private String title;
   private String targetRole;
   private String addedDate;
   private String filePath; //

   public Notice(int id, String title, String targetRole, String addedDate, String filePath) {
      this.id = id;
      this.title = title;
      this.targetRole = targetRole;
      this.addedDate = addedDate;
      this.filePath = filePath;
   }

   // Getters...
   public String getTitle() { return title; }
   public String getTargetRole() { return targetRole; }
   public String getAddedDate() { return addedDate; }
   public String getFilePath() { return filePath; }
   public int getId() {
      return id;
   }
}