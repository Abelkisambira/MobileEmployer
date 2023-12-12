package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Send_Booking extends AppCompatActivity {

        private DatabaseReference databaseReference;
        private FirebaseUser currentUser;
        private TimePicker timePicker;
        private DatePicker datePicker;
        private EditText locationEditText;
        private Button pickLocationButton,bookingBtn;
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
                 saveBookingDetails();
                 Intent intent=new Intent(Send_Booking.this, Navigation.class);
                 startActivity(intent);
                 finish();
                }
            });


        }

        private void saveBookingDetails() {
            if (currentUser != null) {
                String userId = currentUser.getUid();

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

                // Save to Firebase
                DatabaseReference userBookingRef = databaseReference.child("user_bookings").child(userId);
                userBookingRef.child("selectedTime").setValue(selectedTime);
                userBookingRef.child("selectedDate").setValue(selectedDate); // Save selected date
                userBookingRef.child("selectedLocation").setValue(selectedLocation);

                Toast.makeText(this, "Booking details saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        }
    @Override
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
