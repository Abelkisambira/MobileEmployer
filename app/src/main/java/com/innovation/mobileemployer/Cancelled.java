package com.innovation.mobileemployer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.innovation.mobileemployer.adapter.AcceptedBookingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class Cancelled extends AppCompatActivity {

    private List<Professionals> acceptedProfessionals;
    private AcceptedBookingsAdapter acceptedBookingsAdapter;
    private Toolbar backTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled);

        // Initialize lists and adapter
        acceptedProfessionals = new ArrayList<>();
        acceptedBookingsAdapter = new AcceptedBookingsAdapter(this, acceptedProfessionals);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(acceptedBookingsAdapter);

        // Retrieve and fill accepted professionals
        retrieveAcceptedProfessionals();
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

    private void retrieveAcceptedProfessionals() {
        DatabaseReference professionalBookingsRef = FirebaseDatabase.getInstance().getReference("professional_bookings");

        professionalBookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                acceptedProfessionals.clear(); // Clear the list before adding data

                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    String bookingStatus = bookingSnapshot.child("bookingStatus").getValue(String.class);

                    // Check if the booking is accepted
                    if ("cancelled".equals(bookingStatus)) {
                        String professionalId = bookingSnapshot.child("professionalId").getValue(String.class);

                        // Retrieve the corresponding professional details from "Professionals" node
                        retrieveProfessionalDetails(professionalId);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                Log.e("Firebase Error", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void retrieveProfessionalDetails(String professionalId) {
        DatabaseReference professionalsRef = FirebaseDatabase.getInstance().getReference("Professionals");

        professionalsRef.child(professionalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Professionals professional = dataSnapshot.getValue(Professionals.class);

                if (professional != null) {
                    // Set the ID in the professional object
                    professional.setProfessionalID(dataSnapshot.getKey());
                    acceptedProfessionals.add(professional);
                    acceptedBookingsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                Log.e("Firebase Error", "Database Error: " + databaseError.getMessage());
            }
        });
    }
}
