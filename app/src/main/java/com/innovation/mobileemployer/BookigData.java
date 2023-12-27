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

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
