package com.innovation.mobileemployer;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Chat extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        searchButton=findViewById(R.id.searchbtn);

        searchButton.setOnClickListener((V)->{
            startActivity(new Intent(Chat.this,Search.class));
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.chat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainframe_layout,chatFragment).commit();

                }
                if (item.getItemId()==R.id.profile2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainframe_layout,profileFragment).commit();

                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.chat);

    }
}