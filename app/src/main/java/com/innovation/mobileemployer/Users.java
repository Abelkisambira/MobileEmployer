package com.innovation.mobileemployer;
public class Users {
    private String userId;
    // other user fields...

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    }

    public Users(String userId) {
        this.userId = userId;
        // initialize other fields...
    }

    public String getUserId() {
        return userId;
    }
    // other getters and setters...
}
