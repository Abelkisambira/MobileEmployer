package com.innovation.mobileemployer;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Booking extends Fragment {

    private List<Professionals> allProfessionals;
    private List<Professionals> displayedProfessionals;

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
        EditText searchEditText = view.findViewById(R.id.search_username_input);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterProfessionals(editable.toString());
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
        displayProfessionalsInView();
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

            // Create a new TextView for each field (email, password, phone, username)
            TextView emailTextView = new TextView(requireContext());
            emailTextView.setText("Email: " + professional.getEmail());
            linearLayout.addView(emailTextView);

            TextView passwordTextView = new TextView(requireContext());
            passwordTextView.setText("Password: " + professional.getPassword());
            linearLayout.addView(passwordTextView);

            TextView phoneTextView = new TextView(requireContext());
            phoneTextView.setText("Phone: " + professional.getPhone());
            linearLayout.addView(phoneTextView);

            TextView usernameTextView = new TextView(requireContext());
            usernameTextView.setText("Username: " + professional.getUsername());
            linearLayout.addView(usernameTextView);

            // Create a new Button or ImageButton
            Button bookButton = new Button(requireContext());
            bookButton.setText("Book");
            bookButton.setBackgroundColor(Color.CYAN);
            // Customize the Button as needed (background, click listener, etc.)

            // Add the Button to the LinearLayout
            linearLayout.addView(bookButton);

            // Add the LinearLayout to the CardView
            cardView.addView(linearLayout);

            // Add the CardView to the parent LinearLayout
            professionalLayout.addView(cardView);
        }
    }
    private void filterProfessionals(String query) {
        List<Professionals> filteredList = new ArrayList<>();

        for (Professionals professional : allProfessionals) {
            if (professional.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(professional);
            }
        }

        displayedProfessionals = filteredList;
        displayProfessionalsInView();
    }

}

