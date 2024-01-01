package com.innovation.mobileemployer;


import com.google.firebase.Timestamp;

public class ChatMessage {
    private String message;
    private String employerID,professionalID;
    private boolean isSentByUser;
    private String receiverName, employerName;
    private Timestamp timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String employerName, String message, boolean isSentByUser, String employerID,String professionalID, String receiverName, Timestamp timestamp) {
        this.employerName = employerName;
        this.message = message;
        this.isSentByUser = isSentByUser;
        this.employerID = employerID;
        this.professionalID=professionalID;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
    }

    public String getProfessionalID() {
        return professionalID;
    }

    public void setProfessionalID(String professionalID) {
        this.professionalID = professionalID;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
