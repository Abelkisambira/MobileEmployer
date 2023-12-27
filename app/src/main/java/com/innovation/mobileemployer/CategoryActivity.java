package com.innovation.mobileemployer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.innovation.mobileemployer.adapter.SubcategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryNameTextView;
    private RecyclerView subcategoryRecyclerView;
    private List<String> subcategories;
    private SubcategoryAdapter subcategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryNameTextView = findViewById(R.id.categoryNameTextView);
        subcategoryRecyclerView = findViewById(R.id.subcategoryRecyclerView);

        subcategories = new ArrayList<>();
        subcategoryAdapter = new SubcategoryAdapter(subcategories);
        subcategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subcategoryRecyclerView.setAdapter(subcategoryAdapter);

        // Retrieve the selected category's ID from the intent
        String categoryId = getIntent().getStringExtra("categoryId");
        if (categoryId != null) {
            // Fetch and display subcategories
            fetchAndDisplaySubcategories(categoryId);
        } else {
            Log.e("Category Error", "Category ID is null");
        }
    }

    // Add these lines in your CategoryActivity class

    private void fetchAndDisplaySubcategories(String categoryId) {

        fetchSubcategoriesFromDatabase(categoryId);
    }

    private void fetchSubcategoriesFromDatabase(String categoryId) {
        subcategories.clear();

        // Fetch subcategories and update the list
        FirebaseFirestore.getInstance()
                .collection("categories")
                .document(categoryId)
                .collection("subcategories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String subcategoryName = document.getString("name");
                            subcategories.add(subcategoryName);
                        }
                        // Notify the adapter that data has changed
                        subcategoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore Error", "Error getting subcategories: ", task.getException());
                    }
                });
    }


}
