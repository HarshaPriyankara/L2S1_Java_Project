package Models;

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

    private String originalUserID;

    public boolean login(){

        return false;
    }
    public void logout(){}
    public void updateProfile(){}
    public String getDetails(){return null;}

    public String getRole() {
        return role;
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

    public LocalDate getDateOfBirth() {
        return this.dob;
    }

    public String getAddress() {
        return this.address;
    }

    public String getUserID() {
        return this.userID;
    }

    // Add these to your existing User.java

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getContactNo() {
        return this.contactNo;
    }


    public String getOriginalUserID() { return originalUserID; }
    public void setOriginalUserID(String id) { this.originalUserID = id; }
}
