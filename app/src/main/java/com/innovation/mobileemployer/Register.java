package com.innovation.mobileemployer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    Toolbar backTool;
    Button registerBtn;

    DatabaseReference dbRef;

    EditText name,email,phone,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn=findViewById(R.id.register);
        name=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);

        dbRef = FirebaseDatabase.getInstance().getReference().child("users");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=name.getText().toString();
                String Email=email.getText().toString();
                String Phone=phone.getText().toString();
                String Password=password.getText().toString();

                User user=new User(username,Email,Phone,Password);
                dbRef.push().setValue(user);
                Intent intent= new Intent(Register.this, Navigation.class);
                startActivity(intent);
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
}