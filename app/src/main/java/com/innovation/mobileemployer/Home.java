package com.innovation.mobileemployer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.innovation.mobileemployer.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private EditText searchEditText;
    private GridView categoryGridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchEditText = view.findViewById(R.id.search_username_input);
        categoryGridView = view.findViewById(R.id.categoryGridView);

        // Fetch category data (replace with your actual data retrieval)
        List<Category> categories = fetchCategoryData();

        // Update your adapter or UI with the retrieved categories
        updateCategoryAdapter(categories);

        // Set item click listener for the GridView
        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);

                if (selectedCategory != null && selectedCategory.getName() != null) {
                    // Fetch and log subcategories for the selected category
                    fetchSubcategoriesForCategory(selectedCategory.getName());
                } else {
                    Log.e("Category Error", "Selected category or its name is null");
                }
            }
        });

    }

    private List<Category> fetchCategoryData() {
        List<Category> categories = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryId = document.getId();
                                String categoryName = document.getString("name");
                                List<String> subcategories = (List<String>) document.get("subcategories");

                                Category category = new Category(categoryId, categoryName, subcategories);
                                categories.add(category);
                            }

                            // Update your adapter or UI with the retrieved categories
                            updateCategoryAdapter(categories);
                        } else {
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return categories;
    }

    private void fetchSubcategoriesForCategory(String categoryName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference categoryRef = db.collection("categories").document(categoryName).collection("subcategories");

        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> subcategories = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subcategoryName = document.getId();
                        subcategories.add(subcategoryName);
                    }

                    // Now you have the list of subcategories for the specified category
                    Log.d("Subcategories", "Subcategories for " + categoryName + ": " + subcategories.toString());
                } else {
                    Log.e("Firestore Error", "Error getting subcategories: ", task.getException());
                }
            }
        });
    }

    private void updateCategoryAdapter(List<Category> categories) {
        // Update your adapter or UI with the retrieved categories
        // For example, if you are using a CategoryAdapter, you can do something like:
        CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categories);
        categoryGridView.setAdapter(categoryAdapter);
    }
}
