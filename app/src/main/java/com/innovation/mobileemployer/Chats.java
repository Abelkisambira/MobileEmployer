package com.innovation.mobileemployer;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.RatingBar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Chats extends Fragment {
    Button next;

    public Chats() {
        // Required empty public constructor
    }

    private List<Professionals> allProfessionals;
    private List<BookingModel> allBookings;

    private List<Professionals>acceptedBookings;
    private List<Professionals> displayedProfessionals;
    private List<BookingModel> displayedBookings;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    private static final String TAG = "Booking";
    private boolean isViewCreated = false;
    EditText searchEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        // Other initialization if needed
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize lists
        allProfessionals = new ArrayList<>();
        displayedProfessionals = new ArrayList<>();
        allBookings =new ArrayList<>();
        acceptedBookings=new ArrayList<>();
        displayedBookings =new ArrayList<>();

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


    }

    private void retrieveAndFillProfessionalData() {
        getProfessionalListFromDatabase();
    }

    private void getAcceptedBookingsFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accepted_bookings");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> acceptedProfessionalIds = new ArrayList<>(); // Initialize a new list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String professionalId = snapshot.getKey();  // Use the key as the professional ID
                    Boolean isAccepted = snapshot.getValue(Boolean.class);

                    // Check if the professional is accepted (value is true)
                    if (isAccepted != null && isAccepted) {
                        acceptedProfessionalIds.add(professionalId);
                    }
                }

                // Filter the professionals based on the accepted IDs
//                List<Professionals> acceptedProfessionals = filterProfessionalsById(allProfessionals, acceptedProfessionalIds);
                // Update your UI or do other processing with the retrieved data
//                acceptedBookings = acceptedProfessionals;
                acceptedBookings = filterAcceptedProfessionals();
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

    private List<Professionals> filterProfessionalsById(List<Professionals> allProfessionals, List<String> acceptedProfessionalIds) {
        List<Professionals> filteredProfessionals = new ArrayList<>();

        for (Professionals professional : allProfessionals) {
            // Assuming there's a method getId() in the Professionals class
            String professionalId = professional.getProfessionalID();

            // Check if the professional's ID exists in the accepted IDs
            if (acceptedProfessionalIds.contains(professionalId)) {
                filteredProfessionals.add(professional);
            }
        }

        return filteredProfessionals;
    }


    private List<Professionals> filterAcceptedProfessionals() {
        List<Professionals> filteredProfessionals = new ArrayList<>();

        for (Professionals professional : allProfessionals) {
            // Assuming there's a method getId() in the Professionals class
            String professionalId = professional.getProfessionalID();

            // Check if the professional's ID exists in accepted_bookings node
            if (isProfessionalAccepted(professionalId)) {
                filteredProfessionals.add(professional);
            }
        }

        return filteredProfessionals;
    }

    private boolean isProfessionalAccepted(String professionalId) {
        // Replace this with your logic to check if the professional's ID exists in accepted_bookings node
        // For example, using the accepted_bookings list you retrieved earlier
        for (Professionals acceptedProfessional : acceptedBookings) {
            if (acceptedProfessional.getProfessionalID().equals(professionalId)) {
                return true;
            }
        }

        return false;
    }


    private void getProfessionalListFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Professionals");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Professionals> professionals = new ArrayList<>(); // Initialize a new list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String professionalId = snapshot.getKey();
                    Professionals professional = snapshot.getValue(Professionals.class);
//                    professional.setRating(snapshot.child("rating").getValue(Float.class));


                    // Set the ID in the professional object
                    professional.setProfessionalID(professionalId);

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
        if (!isAdded()) {
            // Fragment is not attached to the activity, do nothing
            return;
        }
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
            cardView.setElevation(20f);
            cardView.setRadius(20f);

            // Set card background color to cream
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));

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
            // Add RatingBar
            // Add RatingBar
            RatingBar ratingBar = new RatingBar(requireContext(), null, android.R.attr.ratingBarStyle);
            ratingBar.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

// Set the size of the stars
            int starSizePx = getResources().getDimensionPixelSize(R.dimen.start_size); // Replace R.dimen.your_star_size with your dimension resource
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.5f);
            ratingBar.setRating(professional.getTotalRating() / professional.getRatingCount());
            ratingBar.setScaleX(0.5f); // Adjust the scale for X-axis to make stars smaller
            ratingBar.setScaleY(0.5f); // Adjust the scale for Y-axis to make stars smaller

            linearLayout.addView(ratingBar);


            // Add a listener to update the rating when it changes
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    // Update the professional's rating in the list and in the database
                    professional.setRating(rating);

                    // Update totalRating and ratingCount
                    float newTotalRating = professional.getTotalRating() + rating;
                    int newRatingCount = professional.getRatingCount() + 1;

                    // Update the professional's totalRating and ratingCount
                    professional.setTotalRating(newTotalRating);
                    professional.setRatingCount(newRatingCount);

                    // Update the professional's rating in the database
                    updateProfessionalRatingInDatabase(professional.getProfessionalID(), newTotalRating, newRatingCount);
                }
            });


            Button chatButton = new Button(requireContext());
            chatButton.setText("Chat");
            chatButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan));

            linearLayout.addView(chatButton);

            linearLayout.setPadding(20, 0, 20, 0);

            cardView.addView(linearLayout);
            professionalLayout.addView(cardView);
//


            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the professional details from your data source
                    String professionalId = professional.getProfessionalID();
                    String professionalFCMToken = professional.getFcmToken();
                    String professionalName = professional.getUsername();
                    String professionalProfilePicUrl=professional.getImageUrl();

                    // Launch Chat activity with professional's information
                    Intent intent = new Intent(requireContext(), Chat.class);
                    intent.putExtra("professionalFCMToken", professionalFCMToken);
                    intent.putExtra("professionalId", professionalId);
                    intent.putExtra("PROFESSIONAL_NAME", professionalName);
                    intent.putExtra("PROFESSIONAL_PROFILE_PIC_URL", professionalProfilePicUrl);
                    startActivity(intent);
                }
            });


        }
    }
    private void updateProfessionalRatingInDatabase(String professionalId, float totalRating, int ratingCount) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Professionals")
                .child(professionalId)
                .child("totalRating");

        databaseReference.setValue(totalRating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Total rating updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update total rating: " + e.getMessage());
                    }
                });

        // Update ratingCount in the same way
        DatabaseReference ratingCountReference = FirebaseDatabase.getInstance().getReference("Professionals")
                .child(professionalId)
                .child("ratingCount");

        ratingCountReference.setValue(ratingCount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Rating count updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update rating count: " + e.getMessage());
                    }
                });
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
        List<BookingModel> filteredBookings = new ArrayList<>();

        for (Professionals professional : allProfessionals) {
            boolean matchesQuery = query == null || professional.getUsername().toLowerCase().contains(query.toLowerCase());


            if (matchesQuery) {
                filteredProfessionals.add(professional);
            }
        }
        for(BookingModel booking: allBookings){
            boolean matchesStatus = status == null || booking.getBookingStatus().equalsIgnoreCase(status);

            if (matchesStatus) {
                filteredBookings.add(booking);
            }
        }

        // Update the displayed professionals with the filtered list
        displayedProfessionals = filteredProfessionals;
        displayedBookings=filteredBookings;
        // Update the UI to display the filtered professionals
        displayProfessionalsInView();
    }
}