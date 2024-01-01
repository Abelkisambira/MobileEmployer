package com.innovation.mobileemployer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.innovation.mobileemployer.adapter.CatAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryNameTextView;
    private RecyclerView professionalRecyclerView;
    private List<Professionals> professionalsInCategory;
    private CatAdapter catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryNameTextView = findViewById(R.id.categoryNameTextView);
        professionalRecyclerView = findViewById(R.id.professionalRecyclerView);

        professionalsInCategory = new ArrayList<>();
        catAdapter = new CatAdapter(this, professionalsInCategory);
        professionalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        professionalRecyclerView.setAdapter(catAdapter);

        // Retrieve the selected category's ID from the intent
        String selectedCategory = getIntent().getStringExtra("categoryName");
        if (selectedCategory != null) {
            // Set the selected category in CategoryActivity
            setSelectedCategory(selectedCategory);
        } else {
            Log.e("Category Error", "Selected Category is null");
        }
    }

    private void setSelectedCategory(String selectedCategory) {
        // Set the category name in the TextView
        categoryNameTextView.setText(selectedCategory);

        // Fetch and display professionals based on the selected category
        fetchAndDisplayProfessionals(selectedCategory);
    }

    private void fetchAndDisplayProfessionals(String selectedCategory) {
        // Fetch professionals from the database based on the selected category
        // Modify the code to fit your database structure and retrieval logic

        // Example: Fetching professionals from Firestore
        FirebaseFirestore.getInstance()
                .collection("Professionals")
                .whereEqualTo("category", selectedCategory)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Professionals> professionals = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Professionals professional = document.toObject(Professionals.class);
                            professionals.add(professional);
                        }
                        // Update UI with the fetched professionals
                        updateUIWithProfessionals(professionals);
                    } else {
                        Log.e("Firestore Error", "Error getting professionals: ", task.getException());
                    }
                });
    }

    private void updateUIWithProfessionals(List<Professionals> professionals) {
        // Update the UI to display the fetched professionals
        // For example, update a RecyclerView or any other UI component
        professionalsInCategory.clear();
        professionalsInCategory.addAll(professionals);
        catAdapter.notifyDataSetChanged();
    }
}
