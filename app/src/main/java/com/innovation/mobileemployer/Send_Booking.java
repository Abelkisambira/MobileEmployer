package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class Send_Booking extends AppCompatActivity {

        private DatabaseReference databaseReference;
        private FirebaseUser currentUser;
        private TimePicker timePicker;
        private DatePicker datePicker;
        private EditText locationEditText,seekerNameEditText;
        private Button pickLocationButton,bookingBtn;
        Toolbar backTool;
    private static final int PICK_LOCATION_REQUEST_CODE = 4;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_send_booking);

            // Initialize Firebase
            databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            // Initialize UI components
            timePicker = findViewById(R.id.timePicker);
            datePicker = findViewById(R.id.datePicker);
            locationEditText = findViewById(R.id.location);
            pickLocationButton = findViewById(R.id.pickLocationButton);
            bookingBtn=findViewById(R.id.bookbtn);
            seekerNameEditText=findViewById(R.id.nameText);

            String professionalId = getIntent().getStringExtra("professionalId");
            String professionalFCMToken = getIntent().getStringExtra("professionalFCMToken");
            pickLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pickLocationIntent = new Intent(Send_Booking.this, Pick_Location.class);
                    startActivityForResult(pickLocationIntent, PICK_LOCATION_REQUEST_CODE);
                }
            });
            bookingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Save booking details
                    saveBookingDetails(professionalId, professionalFCMToken);

                    Intent intent = new Intent(Send_Booking.this, Navigation.class);
                    startActivity(intent);
                    finish();
                }
            });
            backTool=findViewById(R.id.back);
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


        }

    private void saveBookingDetails(String professionalId, String professionalFCMToken) {
        if (currentUser != null) {

            // Ensure professionalId is not null or empty
            if (TextUtils.isEmpty(professionalId)) {
                Toast.makeText(this, "Invalid professionalId", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(professionalFCMToken)) {
                Toast.makeText(this, "Invalid professionalFCMToken", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected time from TimePicker
            int hour, minute;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                // For API level 21 to 22
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }

            String selectedTime = String.format("%02d:%02d", hour, minute);

            // Get selected date from DatePicker
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is 0-based
            int year = datePicker.getYear();
            String selectedDate = String.format("%02d-%02d-%04d", day, month, year);

            // Get selected location from EditText
            String selectedLocation = locationEditText.getText().toString().trim();
            String seekerName = seekerNameEditText.getText().toString().trim();

            // Save to Firebase
            DatabaseReference professionalBookingRef = databaseReference.child("professional_bookings");
            String bookingId = professionalBookingRef.push().getKey();

            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("seekerName", seekerName);
            bookingData.put("selectedTime", selectedTime);
            bookingData.put("selectedDate", selectedDate);
            bookingData.put("selectedLocation", selectedLocation);
            bookingData.put("bookingStatus", "pending");
            bookingData.put("professionalId", professionalId);
            bookingData.put("userId", currentUser.getUid()); // Include the userId

            professionalBookingRef.child(bookingId).setValue(bookingData);

            sendNotificationToProfessional(professionalFCMToken);
            Toast.makeText(this, "Booking details saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendNotificationToProfessional(String professionalFCMToken) {
        // Create a data payload for the notification
        Map<String, String> data = new HashMap<>();
        data.put("title", "New Booking");
        data.put("message", "You have a new booking request.");

        // Send the FCM message
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(professionalFCMToken)
                .setData(data)
                .build());
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve data from the result intent
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);
            String locationDescription = data.getStringExtra("locationDescription");

            // Update the UI with the selected location details
            locationEditText.setText(locationDescription);

            // You can also save latitude and longitude if needed
        }
    }

}
