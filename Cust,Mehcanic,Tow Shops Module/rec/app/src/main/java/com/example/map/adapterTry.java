package com.example.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapterTry extends RecyclerView.Adapter<adapterTry.ViewHolder> {

    private List<String> rootKeysList;

    public adapterTry(List<String> rootKeysList) {
        this.rootKeysList = rootKeysList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String rootKey = rootKeysList.get(position);
        holder.textViewRootKey.setText(rootKey);
    }

    @Override
    public int getItemCount() {
        return rootKeysList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRootKey;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewRootKey = itemView.findViewById(R.id.custname);
        }
    }
}