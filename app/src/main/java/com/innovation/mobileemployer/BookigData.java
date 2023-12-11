package com.innovation.mobileemployer;
public class BookigData {
    private String professionalId;
    private String status;
    private long timestamp;

    // Add an empty constructor for Firebase
    public BookigData() {
    }

    public BookigData(String professionalId, String status, long timestamp) {
        this.professionalId = professionalId;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Add getters and setters
}
