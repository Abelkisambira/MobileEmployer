package com.innovation.mobileemployer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeDefault extends Fragment {

    private EditText nameEditText;
    private DatabaseReference usersRef;
    private CardView chats, categories, edit, logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_default, container, false);

        // Initialize views
        nameEditText = rootView.findViewById(R.id.name);
        chats = rootView.findViewById(R.id.chatsCard);
        categories = rootView.findViewById(R.id.categoriesCard);
        edit = rootView.findViewById(R.id.editCard);
        logout = rootView.findViewById(R.id.logoutCard);

        // Set click listeners
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Chats());
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Home());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the current user
                FirebaseAuth.getInstance().signOut();

                // Clear the user session in shared preferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mobile Employers", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Navigate to the login activity
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();  // Close the current activity
            }
        });



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

        return rootView;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bottom_nav_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
