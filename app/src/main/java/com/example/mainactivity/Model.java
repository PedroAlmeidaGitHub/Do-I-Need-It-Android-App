package com.example.mainactivity;

public class Model {
    private String id;          // Unique identifier for the model
    private String title;       // Title of the model (corrected from 'tile' to 'title')
    private String description; // Description of the model

    // Default constructor for Firebase
    public Model() {
        // Required for Firebase
    }

    // Parameterized constructor for initializing the model
    public Model(String id, String title, String description) {
        this.id = id;
        this.title = title;         // Updated to 'title'
        this.description = description;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title; // Updated to 'title'
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }
}