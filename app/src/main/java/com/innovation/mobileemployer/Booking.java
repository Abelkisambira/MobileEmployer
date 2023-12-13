package com.innovation.mobileemployer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
    EditText searchEditText;

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

        // Initialize the search EditText
        searchEditText = view.findViewById(R.id.search_username_input);

        // Add a TextWatcher to the search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter displayed professionals based on the entered search query
                filterProfessionals(charSequence.toString(), null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this example
            }
        });

        // Get references to the three buttons
        Button pendingButton = view.findViewById(R.id.pending_button);
        Button acceptedButton = view.findViewById(R.id.accepted_button);
        Button cancelledButton = view.findViewById(R.id.cancelled_button);

        // Set click listeners for the three buttons
        pendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Filter professionals by pending booking status
                filterProfessionals(null, "pending");
            }
        });

        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Filter professionals by accepted booking status
                filterProfessionals(null, "accepted");
            }
        });

        cancelledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Filter professionals by cancelled booking status
                filterProfessionals(null, "cancelled");
            }
        });
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
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 20, 0, 20);
            cardView.setLayoutParams(cardParams);

            // Set elevation and corner radius for the CardView
            cardView.setElevation(10f);
            cardView.setRadius(20f);

            // Set card background color to cream
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cream));

            // Create a new LinearLayout for each CardView
            LinearLayout linearLayout = new LinearLayout(requireContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // Display the image (assuming you are using an ImageView)
            ImageView imageView = new ImageView(requireContext());
            // Load and display the image using Glide only if permission is granted
            if (hasReadExternalStoragePermission()) {
                loadAndDisplayImage(professional.getImageUrl(), imageView);
                linearLayout.addView(imageView);
            }

            // Display other details
            TextView categoryTextView = new TextView(requireContext());
            categoryTextView.setText("Category: " + professional.getCategory());
            linearLayout.addView(categoryTextView);

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

            TextView emailTextView = new TextView(requireContext());
            emailTextView.setText("Email: " + professional.getEmail());
            linearLayout.addView(emailTextView);

            TextView phoneTextView = new TextView(requireContext());
            phoneTextView.setText("Phone: " + professional.getPhone());
            linearLayout.addView(phoneTextView);

            Button bookButton = new Button(requireContext());
            bookButton.setText("Book");
            bookButton.setBackgroundColor(Color.CYAN);

            linearLayout.addView(bookButton);

            // Set padding for the LinearLayout
            linearLayout.setPadding(20, 0, 20, 0);

            // Add the LinearLayout to the CardView
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
                    intent.putExtra("professionalId", professional.getId());
                    startActivity(intent);
                }
            });
        }
    }

    private void loadAndDisplayImage(String imageUrl, ImageView imageView) {
        // Load the image using Glide
        Glide.with(requireContext())
                .load(imageUrl)
                .override(200, 200)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Ensure that the view is still valid
                        if (imageView != null) {
                            imageView.setImageDrawable(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e(TAG, "Failed to load image: " + imageUrl);
                    }
                });
    }

    private boolean hasReadExternalStoragePermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendNotificationToProfessional(Professionals professional) {
        // Get the FCM token of the professional from your database
        String professionalFCMToken = professional.getFCMToken();
        if (!professionalFCMToken.isEmpty()) {
            // Create a data payload for the notification
            Map<String, String> data = new HashMap<>();
            data.put("title", "New Booking");
            data.put("message", "You have a new booking request.");

            // Send the FCM message
            FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(professionalFCMToken)
                    .setData(data)
                    .build());
        } else {
            Log.e(TAG, "Professional's FCM token is empty");
        }
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

    private void filterProfessionals(String query, String status) {
        // Filter the professionals based on the entered query and status
        List<Professionals> filteredProfessionals = new ArrayList<>();

        for (Professionals professional : allProfessionals) {
            boolean matchesQuery = query == null || professional.getUsername().toLowerCase().contains(query.toLowerCase());
            boolean matchesStatus = status == null || professional.getBookingStatus().equalsIgnoreCase(status);

            if (matchesQuery && matchesStatus) {
                filteredProfessionals.add(professional);
            }
        }

        // Update the displayed professionals with the filtered list
        displayedProfessionals = filteredProfessionals;
        // Update the UI to display the filtered professionals
        displayProfessionalsInView();
    }
}
