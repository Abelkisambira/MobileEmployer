package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.innovation.mobileemployer.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private EditText messageEditText;
    private FirebaseUser currentUser;

    private ImageButton sendButton;
    private Toolbar backTool;
    private String employerID;
    private DatabaseReference messagesRef;
    private static final String TAG = "Chats";
    UserModel userModel;

    private String professionalID; // Added for storing the professional ID
    private String professionalName; // Added for storing the professional name
    private String professionalFCMToken; // Added for storing the professional FCM token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Fetch employer ID dynamically
        if (currentUser != null) {
            employerID = currentUser.getUid();
        }

        // Receive professional details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            professionalID = intent.getStringExtra("professionalId");
            professionalName = intent.getStringExtra("PROFESSIONAL_NAME");
            professionalFCMToken = intent.getStringExtra("professionalFCMToken");
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.message_send_btn);

        // Initialize RecyclerView and ChatAdapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatMessages, employerID);
        recyclerView.setAdapter(chatAdapter);

        backTool = findViewById(R.id.back);
        setSupportActionBar(backTool);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Listen for incoming messages
        listenForIncomingMessages();

        // Set a click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming you have employerID and messageText
                String messageText = messageEditText.getText().toString().trim();

                // Use the parameters to send the message and notification
                sendMessage(professionalID, employerID, messageText);
            }
        });
    }

    private void listenForIncomingMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleMessage(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes to the existing messages (if needed)
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removal of messages (if needed)
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle movement of messages (if needed)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors (if needed)
            }
        });
    }

    private void handleMessage(DataSnapshot snapshot) {
        String messageText = snapshot.child("message").getValue(String.class);
        String senderID = snapshot.child("employerID").getValue(String.class);
        String senderName=snapshot.child("employerName").getValue(String.class);

        if (messageText != null && senderID != null && senderName != null) {
            // Assuming you have a method to get the receiver name based on senderID
            String receiverName = getReceiverName(senderName);
            // Pass senderName and receiverName to addMessage
            addMessage(messageText, !senderID.equals(employerID), senderID, receiverName);
        }
    }

    private void sendMessage(String professionalID, String employerID, String messageText) {
        if (!messageText.isEmpty()) {
            String messageKey = messagesRef.push().getKey();

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", messageText);
            messageMap.put("professionalID", professionalID);
            messageMap.put("employerID", employerID); // Add employerID to the message

            messagesRef.child(messageKey).updateChildren(messageMap);

            // Send notification to the professional
            sendNotificationToProfessional();

            // Pass senderName and receiverName to addMessage
            addMessage(messageText, true, employerID, professionalName);

            messageEditText.getText().clear();
        }
    }

    private void sendNotificationToProfessional() {
        // Query the database to get the FCM token of the professional you are chatting with
        DatabaseReference professionalsRef = FirebaseDatabase.getInstance().getReference("professionals");
        professionalsRef.child(professionalID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the FCM token
                    String professionalFCMToken = dataSnapshot.child("fcmToken").getValue(String.class);

                    if (professionalFCMToken != null && !professionalFCMToken.isEmpty()) {
                        // Create a data payload for the notification
                        Map<String, String> data = new HashMap<>();
                        data.put("title", "New Message");
                        data.put("body", "You have a new message from " + currentUser.getDisplayName());

                        // Create the notification payload
                        Map<String, Object> notification = new HashMap<>();
                        notification.put("title", "New Message");
                        notification.put("body", "You have a new message from " + currentUser.getDisplayName());

                        // Create the message payload
                        Map<String, Object> messagePayload = new HashMap<>();
                        messagePayload.put("data", data);
                        messagePayload.put("notification", notification);
                        messagePayload.put("to", professionalFCMToken);

                        // Send the FCM message
                        FirebaseDatabase.getInstance().getReference("fcmMessages").push().setValue(messagePayload);
                    } else {
                        Log.e(TAG, "Professional's FCM token is empty");
                    }
                }
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching professional's FCM token", databaseError.toException());
            }
        });
    }



    private String getReceiverName(String receiverID) {
        // Implement logic to get the receiver's name based on the ID
        // This could be from Firebase or any other source
        // For example, you might have a Users node where you can look up the name
        // For simplicity, let's return a placeholder name for now
        return professionalName;
    }

    private void addMessage(String message, boolean isSentByUser, String senderID, String receiverName) {
        ChatMessage chatMessage = new ChatMessage(message, isSentByUser, senderID, receiverName);
        chatMessages.add(chatMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }
}
