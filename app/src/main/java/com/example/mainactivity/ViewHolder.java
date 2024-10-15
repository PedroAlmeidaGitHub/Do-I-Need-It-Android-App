package com.example.mainactivity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder class to represent individual items in the RecyclerView
public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTitleTv;       // TextView for displaying the title
    TextView mDescriptionTv; // TextView for displaying the description
    private View mView;              // Reference to the item view

    // Constructor to initialize the ViewHolder with the itemView
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView; // Store the itemView for further usage

        // Initialize views from the model_layout.xml
        mTitleTv = itemView.findViewById(R.id.rTitleTv);
        mDescriptionTv = itemView.findViewById(R.id.rDescriptionTv);

        // Set click listener for the item view
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        });

        // Set long click listener for the item view
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                }
                return true; // Indicate that the long click was handled
            }
        });
    }

    // ClickListener interface to handle click events
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // Variable to hold the click listener instance
    private ClickListener mClickListener;

    // Method to set the click listener
    public void setOnClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }
}
