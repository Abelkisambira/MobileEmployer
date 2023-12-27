package com.innovation.mobileemployer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String username;
    private String Email;
    private String Phone;
    private String Password;

    public User() {
    }

    public User(String username, String email, String phone, String password) {
        this.username = username;
        Email = email;
        Phone = phone;
        Password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    // New method to get the current user's display name
    public static String getCurrentUserName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            return user.getDisplayName();
        }

        return null;
    }
}
