package com.innovation.mobileemployer;

public class ChatMessage {
    private String message;
    private String senderID;
    private boolean isSentByUser;
    private String receiverName;

    public ChatMessage(String message, boolean isSentByUser, String senderID, String receiverName) {
        this.message = message;
        this.isSentByUser = isSentByUser;
        this.senderID = senderID;
        this.receiverName = receiverName;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public String getReceiverName() {
        return receiverName;
    }
}
