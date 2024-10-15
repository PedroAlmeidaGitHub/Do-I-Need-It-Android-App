package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

    // UI components
    private EditText emailId, password;
    private Button btnSignUp;
    private TextView tvSignIn;

    // Firebase Authentication instance
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailId = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        btnSignUp = findViewById(R.id.buttonbtnSignUp);
        tvSignIn = findViewById(R.id.textView);

        // Set up sign-up button click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(); // Call method to register user
            }
        });

        // Set up sign-in text view click listener
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void registerUser() {
        String email = emailId.getText().toString().trim(); // Get trimmed email
        String pwd = password.getText().toString().trim(); // Get trimmed password

        // Validate email and password
        if (email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
            return; // Exit method if email is empty
        }
        if (pwd.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
            return; // Exit method if password is empty
        }

        // Attempt to create a user with the provided email and password
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(FirstActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // Sign-up failed
                            Toast.makeText(FirstActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign-up successful, redirect to HomeActivity
                            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                            finish(); // Optionally finish the activity to prevent going back to it
                        }
                    }
                });
    }
}