package com.example.mainactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    // UI elements
    private EditText titleEditText, descriptionEditText;
    private Button saveButton, listButton, logoutButton;

    // Progress dialog to show feedback during operations
    private ProgressDialog progressDialog;

    // Firebase Firestore instance
    private FirebaseFirestore firestore;

    // Firebase Auth instance
    private FirebaseAuth auth;

    // Holds document ID if updating an existing record
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the UI components and Firebase instances
        initUI();

        // Set up button click listeners for saving data, logging out, and opening the list activity
        setupListeners();

        // Check if activity is for updating an existing record or creating a new one
        checkForUpdate();
    }

    // Method to initialize UI components and Firebase instances
    private void initUI() {
        titleEditText = findViewById(R.id.titleEt);
        descriptionEditText = findViewById(R.id.descriptionEt);
        saveButton = findViewById(R.id.saveBtn);
        listButton = findViewById(R.id.listBtn);
        logoutButton = findViewById(R.id.logout);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Set a default message for the dialog

        // Initialize Firestore and FirebaseAuth instances
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    // Method to set up event listeners for buttons
    private void setupListeners() {
        // Logout button listener
        logoutButton.setOnClickListener(v -> {
            auth.signOut();  // Sign out from Firebase
            startActivity(new Intent(HomeActivity.this, FirstActivity.class));  // Navigate back to login activity
            finish();  // Finish current activity
        });

        // Save button listener for adding/updating data
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            // Validate that title and description are not empty
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                Toast.makeText(HomeActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // If documentId exists, update the data; otherwise, upload new data
            if (documentId != null) {
                updateData(documentId, title, description);
            } else {
                uploadData(title, description);
            }
        });

        // List button listener to navigate to ListActivity
        listButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ListActivity.class));
            finish();
        });
    }

    // Method to check if the activity is for updating an existing record or adding a new one
    private void checkForUpdate() {
        ActionBar actionBar = getSupportActionBar();
        Bundle bundle = getIntent().getExtras();  // Get data passed from the previous activity
        if (bundle != null) {
            // Updating existing record
            actionBar.setTitle("Update Data");
            saveButton.setText("Update");

            // Set the document ID and pre-fill title and description fields with existing data
            documentId = bundle.getString("pId");
            titleEditText.setText(bundle.getString("pTitle"));
            descriptionEditText.setText(bundle.getString("pDescription"));
        } else {
            // Adding new record
            actionBar.setTitle("Add Data");
            saveButton.setText("Save");
        }
    }

    // Method to upload new data to Firestore
    private void uploadData(String title, String description) {
        progressDialog.setTitle("Uploading Data...");
        progressDialog.show();

        // Create a new document ID
        String id = UUID.randomUUID().toString();

        // Create a map for the data
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("title", title);
        data.put("description", description);

        // Add the data to Firestore
        firestore.collection("Documents").document(id)
                .set(data)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                        titleEditText.setText(""); // Clear the input fields
                        descriptionEditText.setText("");
                    } else {
                        Toast.makeText(HomeActivity.this, "Upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to update existing data in Firestore
    private void updateData(String documentId, String title, String description) {
        progressDialog.setTitle("Updating Data...");
        progressDialog.show();

        // Create a map for the updated data
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);

        // Update the document in Firestore
        firestore.collection("Documents").document(documentId)
                .update(data)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}