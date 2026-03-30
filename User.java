import java.time.LocalDate;

public class User {
    private String userID;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String role;
    private LocalDate dob;
    private String contactNo;
    private String address;

    public boolean login(){

        return false;
    }
    public void logout(){}
    public void updateProfile(){}
    public String getDetails(){return null;}
}
