package com.innovation.mobileemployer;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class Register extends AppCompatActivity {
    Toolbar backTool;
    Button registerBtn;

    DatabaseReference dbRef;

    EditText name,email,phone,password;
    TextView backToLog;
    FirebaseAuth mAuth;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent= new Intent(getApplicationContext(), Navigation.class);
//            startActivity(intent);
//            finish();
//        }
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        backToLog=findViewById(R.id.backTolog);

        registerBtn=findViewById(R.id.register);
        name=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phoneholder);
        password=findViewById(R.id.password);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Clients");

        backToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Register.this, Login.class);
                startActivity(intent1);
                finish();
            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=name.getText().toString();
                String Email=email.getText().toString();
                String Phone=phone.getText().toString();
                String Password=password.getText().toString();
                User user=new User(username,Email,Phone,Password);


                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Password)){
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;

                }
                else {


                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        getFCMToken();
                                        String uid = mAuth.getCurrentUser().getUid();

                                        // Store user data in the database under the UID
                                        dbRef.child(uid).setValue(user);

                                        Toast.makeText(Register.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign up fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed. " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



                }

        });

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
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get the FCM token
                        String fcmToken = task.getResult();



                        Log.d(TAG, "FCM token: " + fcmToken);
                        // Store the FCM token in the database under the professional's UID
                        if (mAuth.getCurrentUser() != null) {
                            String uid = mAuth.getCurrentUser().getUid();
                            dbRef.child(uid).child("fcmToken").setValue(fcmToken);
                        }
                    }
                });
    }

}