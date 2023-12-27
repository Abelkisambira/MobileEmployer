package com.innovation.mobileemployer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innovation.mobileemployer.R;

import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    private List<String> subcategories;

    public SubcategoryAdapter(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subcategory = subcategories.get(position);
        holder.bind(subcategory);
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subcategoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subcategoryTextView = itemView.findViewById(R.id.subcategoryTextView);
        }

        public void bind(String subcategory) {
            subcategoryTextView.setText(subcategory);
        }
    }
}

