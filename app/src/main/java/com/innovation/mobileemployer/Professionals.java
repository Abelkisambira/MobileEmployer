package com.innovation.mobileemployer;

import java.util.List;

public class Professionals {

    private String id;
    private String username;
    private String email;
    private String phone;
    private String imageUrl;
    private String category;
    private List<String> subcategories;
    private String fcmToken;
    private String bookingStatus;
    // Empty constructor needed for Firebase
    public Professionals() {
    }

    public Professionals(String username, String email, String phone, String imageUrl, String category, List<String> subcategories) {

        this.username = username;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategories = subcategories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }
    public String getFCMToken() {
        return fcmToken;
    }

    public void setFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    public String getBookingStatus() {
        return bookingStatus;
    }
}

