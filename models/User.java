package models;

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

    public String getRole() {
        return "*************************************";
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return this.lname;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public String getAddress() {
        return this.address;
    }

    public String getUserID() {
        return this.userID;
    }
}
