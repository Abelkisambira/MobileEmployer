package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.innovation.mobileemployer.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private EditText searchEditText;
    private TextView nameEditText;
    private RecyclerView categoryRecyclerView;
    private List<Category> categories;  // Declare categories at the class level
    private DatabaseReference usersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchEditText = view.findViewById(R.id.search_username_input1);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        nameEditText = view.findViewById(R.id.name1);

        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Clients");

        // Get the current user's ID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);


                        nameEditText.setText(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            nameEditText.setText("User not authenticated");
        }


        // Fetch category data (replace with your actual data retrieval)
        fetchCategoryData();

        // Set up RecyclerView and adapter for categories
        CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Set item click listener for the category RecyclerView
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                if (category != null) {
                    // Navigate to CategoryActivity with the selected category ID
                    navigateToCategoryActivity(category.getId());
                } else {
                    Log.e("Category Error", "Selected category is null");
                }
            }
        });

        // Add search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCategories(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void fetchCategoryData() {
        categories = new ArrayList<>();  // Initialize the list

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
                                String imageUrl = document.getString("imageUrl");
                                Category category = new Category(categoryId, categoryName, imageUrl, subcategories);
                                categories.add(category);
                            }

                            updateCategoryAdapter(categories);
                        } else {
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void filterCategories(String searchText) {
        List<Category> filteredCategories = new ArrayList<>();

        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredCategories.add(category);
            }
        }

        updateCategoryAdapter(filteredCategories);
    }

    private void updateCategoryAdapter(List<Category> categories) {
        if (isAdded()) {
            CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categories);
            categoryRecyclerView.setAdapter(categoryAdapter);
        }
    }


    private void navigateToCategoryActivity(String categoryId) {
        Intent intent = new Intent(requireContext(), CategoryActivity.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }
}
