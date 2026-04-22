package Models;

// ENCAPSULATION: We hide the internal state by making all fields private
// and expose controlled access via public getter and setter methods.
public class Notice {
   private int id;
   private String title;
   private String targetRole;
   private String addedDate;
   private String filePath;

   public Notice(int id, String title, String targetRole, String addedDate, String filePath) {
      this.id = id;
      this.title = title;
      this.targetRole = targetRole;
      this.addedDate = addedDate;
      this.filePath = filePath;
   }

   // --- ENCAPSULATION: Getters and Setters ---
   
   public int getId() { 
       return id; 
   }
   
   public void setId(int id) { 
       this.id = id; 
   }

   public String getTitle() { 
       return title; 
   }
   
   public void setTitle(String title) { 
       if(title != null && !title.trim().isEmpty()) {
           this.title = title; 
       }
   }

   public String getTargetRole() { 
       return targetRole; 
   }
   
   public void setTargetRole(String targetRole) { 
       this.targetRole = targetRole; 
   }

   public String getAddedDate() { 
       return addedDate; 
   }
   
   public void setAddedDate(String addedDate) { 
       this.addedDate = addedDate; 
   }

   public String getFilePath() { 
       return filePath; 
   }
   
   public void setFilePath(String filePath) { 
       this.filePath = filePath; 
   }
}