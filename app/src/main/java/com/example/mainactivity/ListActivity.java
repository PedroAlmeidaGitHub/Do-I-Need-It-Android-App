package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Variables to hold model data, UI components, Firestore instance, and progress dialog
    private List<Model> modelList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton mAddBtn;
    private FirebaseFirestore db;
    private CustomAdapter adapter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("List Data");
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);

        // Set RecyclerView properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize Progress Dialog
        pd = new ProgressDialog(this);

        // Load data into RecyclerView
        showData();

        // Handle FloatingActionButton click to go to HomeActivity
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void showData() {
        // Set up and show progress dialog
        pd.setTitle("Loading Data...");
        pd.show();

        // Retrieve documents from Firestore
        db.collection("Documents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear(); // Clear the existing list
                        pd.dismiss(); // Dismiss the progress dialog

                        // Check if task was successful and retrieve data
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Model model = new Model(doc.getString("id"),
                                        doc.getString("title"),
                                        doc.getString("description"));
                                modelList.add(model); // Add model to list
                            }
                            // Set up adapter and bind it to RecyclerView
                            adapter = new CustomAdapter(ListActivity.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to retrieve data
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int index) {
        // Set up and show progress dialog
        pd.setTitle("Deleting Data...");
        pd.show();

        // Delete document from Firestore
        db.collection("Documents").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ListActivity.this, "Deleted...", Toast.LENGTH_SHORT).show();
                        showData(); // Refresh the data
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchData(String query) {
        // Set up and show progress dialog
        pd.setTitle("Searching...");
        pd.show();

        // Search for documents matching the query
        db.collection("Documents").whereEqualTo("search", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear(); // Clear the existing list
                        pd.dismiss(); // Dismiss the progress dialog

                        // Check if task was successful and retrieve data
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                Model model = new Model(doc.getString("id"),
                                        doc.getString("title"),
                                        doc.getString("description"));
                                modelList.add(model); // Add model to list
                            }
                            // Set up adapter and bind it to RecyclerView
                            adapter = new CustomAdapter(ListActivity.this, modelList);
                            mRecyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ListActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu_main.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Set up SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Called when user submits the search
                searchData(s); // Function call with search query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Optionally handle text change if needed
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item clicks
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true; // Indicate that the event was handled
        }
        return super.onOptionsItemSelected(item);
    }
}