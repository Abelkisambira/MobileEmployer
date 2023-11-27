package com.innovation.mobileemployer;

public class Subcategory {
    private String id; // assuming each subcategory has an ID
    private String name;

    // Default constructor for Firestore
    public Subcategory() {
    }

    // Constructor with parameters
    public Subcategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
