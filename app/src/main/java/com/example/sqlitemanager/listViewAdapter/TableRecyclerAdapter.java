package com.example.sqlitemanager.listViewAdapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TableRecyclerAdapter extends RecyclerView.Adapter<TableRecyclerAdapter.ViewHolder> {

    private ArrayList<String[]> rows;
    private Listener mListener;

    public TableRecyclerAdapter(@Nullable ArrayList<String[]> rows) {
        this.rows = rows;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).length;
    }

    void setData(ArrayList<String[]> rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new RowView(parent.getContext(), viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mRowView.setData(rows.get(position));
    }

    @Override
    public int getItemCount() {
        if (rows == null) {
            return 0;
        }

        return rows.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        final RowView mRowView;

        ViewHolder(RowView itemView) {
            super(itemView);
            mRowView = itemView;

            mRowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int layoutPosition = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onColumnValueClicked(rows.get(layoutPosition));
                    }
                }
            });
        }
    }

    public interface Listener {
        void onColumnValueClicked(String[] columnValues);
    }
}
