package com.innovation.mobileemployer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        checkUserSession();
    }

    private void checkUserSession() {
        authStateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                // User is logged in, navigate to home
                Intent intent = new Intent(MainActivity.this, Navigation.class);
                startActivity(intent);
                finish();
            } else {
                // User is not logged in, navigate to login
                redirectToLogin();
            }
        };

        // Add AuthStateListener
        mAuth.addAuthStateListener(authStateListener);

        // Delay for splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remove AuthStateListener after the splash screen
                mAuth.removeAuthStateListener(authStateListener);

                // Finish splash screen and move to the next screen
                finish();
            }
        }, SPLASH_SCREEN);
    }

    private void redirectToLogin() {
        // User is not logged in, navigate to login
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                    String token = task.getResult();
                    Log.i("My token",token);
            }
        });
    }
}

