package com.innovation.mobileemployer;

public class User {
    private String FullName;
    private String Email;
    private String Phone;
    private String Password;


    public User() {
    }

    public User(String fullName, String email, String phone, String password) {
        FullName = fullName;
        Email = email;
        Phone = phone;
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
