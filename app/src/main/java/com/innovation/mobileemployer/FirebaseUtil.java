package com.innovation.mobileemployer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static DatabaseReference currentUserDetails() {
        // Use the "users" node in the Realtime Database
        return FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId());
    }

    public static DatabaseReference allUserDatabaseReference() {
        // Use the "users" node in the Realtime Database
        return FirebaseDatabase.getInstance().getReference().child("users");
    }
}
