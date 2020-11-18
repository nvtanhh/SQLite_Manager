package com.example.sqlitemanager.listViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitemanager.model.DatabaseEntry;
import com.example.sqlitemanager.R;

import java.util.List;

public class DatabaseCardViewAdapter extends RecyclerView.Adapter<DatabaseCardViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DatabaseEntry item);

        void onClickDelete(DatabaseEntry item);
    }


    private List<DatabaseEntry> databases;
    private final OnItemClickListener listener;

    public DatabaseCardViewAdapter(List<DatabaseEntry> list, OnItemClickListener listener) {
        this.databases = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_card_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (databases != null && position < databases.size()) {
            holder.bind(databases.get(position), listener);
        }

    }

    @Override
    public int getItemCount() {
        return databases.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dbTitle;
        public TextView dbDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dbTitle = itemView.findViewById(R.id.db_title);
            dbDescription = itemView.findViewById(R.id.db_description);
        }

        public void bind(final DatabaseEntry item, final OnItemClickListener listener) {
            dbTitle.setText(item.name);
            dbDescription.setText(item.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            View deleteButton = itemView.findViewById(R.id.delete_option);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickDelete(item);
                }
            });
        }
    }
}



