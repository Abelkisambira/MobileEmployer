package com.innovation.mobileemployer;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private List<Professionals> professionalsList;
    Toolbar bar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        String categoryId = getIntent().getStringExtra("categoryId");

        // Now you have the category ID, use it to fetch professionals for that category
        // Example: Fetch professionals for the category from Firestore
        FirebaseFirestore.getInstance().collection("Professionals")
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            professionalsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Professionals professional = document.toObject(Professionals.class);
                                professionalsList.add(professional);
                            }
                            // Update UI with the retrieved professionals
                            updateUIWithProfessionals(professionalsList);
                        } else {
                            Log.e("CategoryActivity", "Error getting documents", task.getException());
                        }
                    }
                });
        bar = findViewById(R.id.back2);

        setSupportActionBar(bar);

        // Enable the back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateUIWithProfessionals(List<Professionals> professionals) {
        // Assuming you have a layout similar to the Booking activity
        // You can customize this part based on how you want to display professionals in the UI

        LinearLayout professionalLayout = findViewById(R.id.specific);
        professionalLayout.removeAllViews(); // Clear existing views

        for (Professionals professional : professionals) {
            CardView cardView = new CardView(this);
            // Customize CardView as needed (radius, elevation, etc.)

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 20, 0, 20);
            cardView.setLayoutParams(cardParams);

            // Create a new LinearLayout for each CardView
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // Load and display the image using Glide only if permission is granted
            if (hasReadExternalStoragePermission()) {
                ImageView imageView = new ImageView(this);
                loadAndDisplayImage(professional.getImageUrl(), imageView);
                linearLayout.addView(imageView);
            }

            // Display the category
            TextView categoryTextView = new TextView(this);
            categoryTextView.setText("Category: " + professional.getCategory());
            linearLayout.addView(categoryTextView);

            // Display the subcategories (assuming a list of subcategories)
            for (String subcategory : professional.getSubcategories()) {
                TextView subcategoryTextView = new TextView(this);
                subcategoryTextView.setText("Subcategory: " + subcategory);
                linearLayout.addView(subcategoryTextView);
            }

            TextView usernameTextView = new TextView(this);
            usernameTextView.setText("Username: " + professional.getUsername());
            linearLayout.addView(usernameTextView);

            // Create a new TextView for each field (email, password, phone, username)
            TextView emailTextView = new TextView(this);
            emailTextView.setText("Email: " + professional.getEmail());
            linearLayout.addView(emailTextView);

            TextView phoneTextView = new TextView(this);
            phoneTextView.setText("Phone: " + professional.getPhone());
            linearLayout.addView(phoneTextView);

            // Create a new Button or ImageButton
            Button bookButton = new Button(this);
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

    private void loadAndDisplayImage(String imageUrl, ImageView imageView) {
        // Load the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    private boolean hasReadExternalStoragePermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


}
