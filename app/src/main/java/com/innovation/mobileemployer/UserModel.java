package com.innovation.mobileemployer;

public class UserModel {

    private String username;
    private String email;
    private String phone;
    private String password;
    private String userId;

    private String fcmToken;

    public UserModel() {
    }

    public UserModel(String username, String email, String phone, String password, String userId,String fcmToken) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userId = userId;
        this.fcmToken=fcmToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
