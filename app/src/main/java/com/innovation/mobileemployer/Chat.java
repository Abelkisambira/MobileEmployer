package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.innovation.mobileemployer.adapter.ChatAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private EditText messageEditText, nameText;
    private FirebaseUser currentUser;
    private ChatViewModel chatViewModel;
    private ImageButton sendButton,backtool;
    //    private Toolbar backTool;
    private String employerID;
    private DatabaseReference messagesRef;
    private static final String TAG = "Chats";
    private ImageView profilePicImageView;
    private TextView otherUsernameTextView;

    private String professionalID;
    private String professionalName;
    private String professionalFCMToken, professionalProfilePicUrl;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
//
//        // Observe changes in the LiveData and update the UI
//        chatViewModel.getChatMessagesLiveData().observe(this, new Observer<List<ChatMessage>>() {
//            @Override
//            public void onChanged(List<ChatMessage> updatedMessages) {
//                chatMessages = updatedMessages;
//                chatAdapter.notifyDataSetChanged();
//            }
//        });
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
            professionalProfilePicUrl = intent.getStringExtra("PROFESSIONAL_PROFILE_PIC_URL");

        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.message_send_btn);
        backtool=findViewById(R.id.back_btn);

        backtool.setOnClickListener((v) ->{
                        onBackPressed();

        });

        // Initialize toolbar components
        profilePicImageView = findViewById(R.id.profile_pic_image_view);
        otherUsernameTextView = findViewById(R.id.other_username);

        // Set professional's profile picture and username in the toolbar
        Glide.with(this)
                .load(professionalProfilePicUrl)
                .into(profilePicImageView);

        otherUsernameTextView.setText(professionalName);

        // Initialize RecyclerView and ChatAdapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        chatAdapter= new ChatAdapter(chatMessages,se);
        chatAdapter = new ChatAdapter(chatMessages,employerID);
        recyclerView.setAdapter(chatAdapter);


        // Listen for incoming messages
        listenForIncomingMessages();

        sendButton.setOnClickListener(v -> sendMessage());
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
        String employerID = snapshot.child("employerID").getValue(String.class);
        String professionalID = snapshot.child("professionalID").getValue(String.class);
        Timestamp timestamp = snapshot.child("time").getValue(Timestamp.class);
        // Ensure you have a way to retrieve the employer name
        String employerName = getEmployerName();


        // Assuming you have a method to get the receiver name based on employerID
        String receiverName = getReceiverName(professionalName);
        boolean isSentByCurrentUser = checkIfSentByCurrentUser(employerID);
        // Create a ChatMessage object
        ChatMessage chatMessage = new ChatMessage(employerName, messageText,isSentByCurrentUser, employerID,professionalID, receiverName, timestamp);
        addMessage(chatMessage);


    }
    private boolean checkIfSentByCurrentUser(String employerID) {
        // Implement logic to check if the message is sent by the current user
        // Compare with the ID of the current user in your app
        // For example, if you have the FirebaseUser object:
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null && currentUser.getUid().equals(employerID);
    }

    // Example method to get employer name (replace with your actual implementation)
    private String getEmployerName() {
        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("Clients");

        // Get the current user's ID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            messagesRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String user = dataSnapshot.child("username").getValue(String.class);
                        username=user;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(Chat.this, "error", Toast.LENGTH_SHORT).show();
                }


            });
        }

        return username;
    }


//
//    private void updateViewModelWithNewMessage(ChatMessage chatMessage) {
//        List<ChatMessage> currentMessages = chatViewModel.getChatMessagesLiveData().getValue();
//        if (currentMessages == null) {
//            currentMessages = new ArrayList<>();
//        }
//        currentMessages.add(chatMessage);
//        chatViewModel.setChatMessages(currentMessages);
//    }

    private void sendMessage() {


//        if (!messageText.isEmpty()) {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            DatabaseReference professionalsRef = FirebaseDatabase.getInstance().getReference("Clients");
//            String professionalId = getIntent().getStringExtra("professionalId");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            Timestamp timestamp = Timestamp.now();


//            Date timestampDate = new Date(timestamp.getSeconds() * 1000);
            // Adjust to use professional's ID
            String employerID = currentUser.getUid();

            professionalsRef.child(employerID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String employerName = dataSnapshot.child("username").getValue(String.class);
                            String employersFCMToken = dataSnapshot.child("fcmToken").getValue(String.class);

                        if (employersFCMToken != null && !employersFCMToken.isEmpty() && employerName != null && !employerName.isEmpty()) {
                            String messageKey = messagesRef.push().getKey();
                            Map<String, String> messageMap = new HashMap<>();
//                                    Map<String, String> data = new HashMap<>();

                            messageMap.put("username", employerName);
                            messageMap.put("message", messageText);
                            messageMap.put("professionalID", professionalID);
                            messageMap.put("employerID", employerID);
                            messageMap.put("time", String.valueOf(timestamp));

//                                    messagesRef.child(messageKey).updateChildren(messageMap);

                            // Send notification to the professional
                            sendNotificationToProfessional();

                            // Pass senderName and receiverName to addMessage

//                                    addMessage(employerName, messageText, true, employerID, professionalName, timestamp);
                            // Add the new message to the list
                            ChatMessage chatMessage = new ChatMessage(employerName, messageText, true, employerID,professionalID, professionalName, timestamp);
                            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
                            messagesRef.push().setValue(chatMessage);

                            FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(employersFCMToken)
                                    .setData(messageMap)
                                    .build());
                            messageEditText.getText().clear();

                        }
                        else {
                            Log.e(TAG, "Employers's FCM token is null or empty");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

//

//

//        }
            });
        }
    }

    private void sendNotificationToProfessional() {
        DatabaseReference professionalsRef = FirebaseDatabase.getInstance().getReference("Professionals");
        professionalsRef.child(professionalID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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

                        // Convert Map to JSONObject
                        JSONObject jsonObject = new JSONObject(messagePayload);

                        // Call the API to send FCM message
                        callAPI(jsonObject);
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


    private String getReceiverName(String receiverName) {
        //
        return professionalName;
    }
    private void addMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        if (!chatMessages.isEmpty()) {
            recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
        }

    }
void callAPI(JSONObject jsonObject){
    MediaType JSON = MediaType.get("application/json");

    OkHttpClient client = new OkHttpClient();
    String url="https://fcm.googleapis.com/fcm/send";
    RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
    Request Request = new Request.Builder()
            .url(url)
        .post(body)
            .header("Authorization", "Bearer AAAAhyqc8eo:APA91bFRoB_UFl6MGRlF-PXBOytYHYBx3-S-1_tUDXlfPkY-hRBSxgbFeYa9XHFtsu6EH7-rb0Af2ZdsiFcDh2QmeBDrHTd3x2evIuA4DUYXN-PPXa6Y-Y9c4KVI1BsXhbXS9Mj8Hu9s")
            .build();
            client.newCall(Request);
    }
//
//    private void addMessage(String employerName, String message, boolean isSentByUser, String senderID, String receiverName, Date timestamp) {
//        // Get the current chat messages from the ViewModel
//        List<ChatMessage> currentChatMessages = chatViewModel.getChatMessagesLiveData().getValue();
//
//        if (currentChatMessages == null) {
//            currentChatMessages = new ArrayList<>();  // Initialize the list if it's null
//        }
//
//        // Add the new message to the list
//        ChatMessage chatMessage = new ChatMessage(employerName, message, isSentByUser, senderID, receiverName, timestamp);
//        currentChatMessages.add(chatMessage);
//
//        // Update the ViewModel with the new list of chat messages
//        chatViewModel.setChatMessages(currentChatMessages);
//
//        // Notify the adapter of the data change
//        chatAdapter.notifyDataSetChanged();
//
//        // Scroll to the last item
//        recyclerView.smoothScrollToPosition(currentChatMessages.size() - 1);
//    }


}
