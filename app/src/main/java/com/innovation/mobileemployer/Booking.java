package com.innovation.mobileemployer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booking extends Fragment {

    private List<Professionals> allProfessionals;
    private List<Professionals> displayedProfessionals;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    private static final String TAG = "Booking";
    private boolean isViewCreated = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize lists
        allProfessionals = new ArrayList<>();
        displayedProfessionals = new ArrayList<>();

        // Retrieve and fill professional data
        retrieveAndFillProfessionalData();

        // Check and request permission
        if (hasReadExternalStoragePermission()) {
            displayProfessionalsInView();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    private void retrieveAndFillProfessionalData() {
        getProfessionalListFromDatabase();
    }

    private void getProfessionalListFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Professionals");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Professionals> professionals = new ArrayList<>(); // Initialize a new list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming the ID is the key of the professional data
                    String professionalId = snapshot.getKey();
                    Professionals professional = snapshot.getValue(Professionals.class);

                    // Set the ID in the professional object
                    professional.setId(professionalId);

                    professionals.add(professional);
                }

                // Update your UI or do other processing with the retrieved data
                allProfessionals = professionals;
                updateUIWithProfessionals(allProfessionals);

                // Call displayProfessionalsInView here after data retrieval
                displayProfessionalsInView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                Log.e("Firebase Error", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateUIWithProfessionals(List<Professionals> professionals) {
        // Assuming you want to display all professionals by default
        displayedProfessionals = professionals;
        // You can choose to call displayProfessionalsInView() here if needed
    }

    private void displayProfessionalsInView() {
        LinearLayout professionalLayout = requireView().findViewById(R.id.professional_layout);
        professionalLayout.removeAllViews(); // Clear existing views

        for (int i = 0; i < displayedProfessionals.size(); i++) {
            Professionals professional = displayedProfessionals.get(i);

            CardView cardView = new CardView(requireContext());
            // Customize CardView as needed (radius, elevation, etc.)

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 20, 0, 20);
            cardView.setLayoutParams(cardParams);

            // Create a new LinearLayout for each CardView
            LinearLayout linearLayout = new LinearLayout(requireContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // Display the image (assuming you are using an ImageView)
            ImageView imageView = new ImageView(requireContext());

            // Load and display the image using Glide only if permission is granted
            if (hasReadExternalStoragePermission()) {
                loadAndDisplayImage(professional.getImageUrl(), imageView);
            }

            linearLayout.addView(imageView);

            // Display the category
            TextView categoryTextView = new TextView(requireContext());
            categoryTextView.setText("Category: " + professional.getCategory());
            linearLayout.addView(categoryTextView);
            // Display the subcategories (assuming a list of subcategories)
            List<String> subcategories = professional.getSubcategories();
            if (subcategories != null) {
                for (String subcategory : subcategories) {
                    TextView subcategoryTextView = new TextView(requireContext());
                    subcategoryTextView.setText("Subcategory: " + subcategory);
                    linearLayout.addView(subcategoryTextView);
                }
            }

            TextView usernameTextView = new TextView(requireContext());
            usernameTextView.setText("Username: " + professional.getUsername());
            linearLayout.addView(usernameTextView);

            // Create a new TextView for each field (email, password, phone, username)
            TextView emailTextView = new TextView(requireContext());
            emailTextView.setText("Email: " + professional.getEmail());
            linearLayout.addView(emailTextView);

            TextView phoneTextView = new TextView(requireContext());
            phoneTextView.setText("Phone: " + professional.getPhone());
            linearLayout.addView(phoneTextView);

            // Create a new Button or ImageButton
            Button bookButton = new Button(requireContext());
            bookButton.setText("Book");
            bookButton.setBackgroundColor(Color.CYAN);
            // Customize the Button as needed (background, click listener, etc.)

            // Add the Button to the LinearLayout
            linearLayout.addView(bookButton);
            // Inside Booking Fragment
            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to Send_Booking activity with professional's UID
                    Intent intent = new Intent(getActivity(), Send_Booking.class);
                    intent.putExtra("professionalUid", professional.getId());
                    startActivity(intent);
                }
            });


            cardView.addView(linearLayout);
            professionalLayout.addView(cardView);

            // Add a click listener to the "Book" button
            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call a method to send a notification to the professional
                    sendNotificationToProfessional(professional);

                    // Launch SendBookingActivity with professional's FCM token
                    Intent intent = new Intent(requireContext(), Send_Booking.class);
                    intent.putExtra("professionalFCMToken", professional.getFCMToken());
                    startActivity(intent);
                }
            });
        }
    }

    private void loadAndDisplayImage(String imageUrl, ImageView imageView) {
        // Load the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    private boolean hasReadExternalStoragePermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendNotificationToProfessional(Professionals professional) {
        // Get the FCM token of the professional from your database
        String professionalFCMToken = professional.getFCMToken();

        // Create a data payload for the notification
        Map<String, String> data = new HashMap<>();
        data.put("title", "New Booking");
        data.put("message", "You have a new booking request.");

        // Send the FCM message
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(professionalFCMToken)
                .setData(data)
                .build());
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, refresh the view
                displayProfessionalsInView();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
