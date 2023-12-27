package com.innovation.mobileemployer;

import java.util.List;

public class Professionals {

    private String professionalID;
    private String username;
    private String email;
    private String phone;
    private String imageUrl;
    private String category;
    private List<String> subcategories;
    private String fcmToken;
    private  String bookingID;

    private String bookingStatus;
    private float totalRating;
    private int ratingCount;
    private float rating;

    // Empty constructor needed for Firebase
    public Professionals() {
    }

    public Professionals(String username, String email, String phone, String imageUrl, String category, List<String> subcategories,String professionalID, String bookingStatus,String bookingID ) {

        this.username = username;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategories = subcategories;
        this.bookingStatus=bookingStatus;
        this.professionalID=professionalID;
        this.bookingID=bookingID;
    }

    public String getProfessionalID() {
        return professionalID;
    }

    public void setProfessionalID(String professionalID) {
        this.professionalID = professionalID;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

