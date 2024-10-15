package com.example.mainactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final ListActivity listActivity;
    private final List<Model> modelList;

    public CustomAdapter(ListActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        // Set click listener for item view
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                Toast.makeText(listActivity, title + "\n" + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showOptionsDialog(position);
            }
        });

        return viewHolder;
    }

    private void showOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
        String[] options = {"Update", "Delete"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openUpdateActivity(position);
                } else if (which == 1) {
                    listActivity.deleteData(position);
                }
            }
        }).create().show();
    }

    private void openUpdateActivity(int position) {
        String id = modelList.get(position).getId();
        String title = modelList.get(position).getTitle();
        String description = modelList.get(position).getDescription();

        Intent intent = new Intent(listActivity, HomeActivity.class);
        intent.putExtra("pId", id);
        intent.putExtra("pTitle", title);
        intent.putExtra("pDescription", description);
        listActivity.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.mTitleTv.setText(modelList.get(position).getTitle());
        viewHolder.mDescriptionTv.setText(modelList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}