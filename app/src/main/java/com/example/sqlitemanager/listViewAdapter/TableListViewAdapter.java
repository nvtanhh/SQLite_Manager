package com.example.sqlitemanager.listViewAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitemanager.DatabaseDetail;
import com.example.sqlitemanager.R;
import com.example.sqlitemanager.model.DBTableEntry;

import java.util.List;

public class TableListViewAdapter extends RecyclerView.Adapter<TableListViewAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(DBTableEntry item);

        void onLongClick(DBTableEntry item);
    }

    private List<DBTableEntry> tables;
    private final OnItemClickListener listener;

    public TableListViewAdapter(List<DBTableEntry> list, OnItemClickListener listener) {
        this.tables = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_card_item, parent, false);
        return new TableListViewAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tables != null && position < tables.size()) {
            boolean isSelected = position == DatabaseDetail.currentTableIndex;
            holder.bind(tables.get(position), listener, isSelected);
        }

    }

    @Override
    public int getItemCount() {
        return tables.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tableName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableName = itemView.findViewById(R.id.table_name);
        }

        public void bind(final DBTableEntry item, final OnItemClickListener listener, boolean isSelected) {
            tableName.setText(item.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(item);
                    return true;
                }
            });

            if (isSelected) tableName.setBackgroundColor(Color.parseColor("#666ad1"));
            else tableName.setBackgroundColor(Color.parseColor("#001970"));
        }
    }


}
