package com.innovation.mobileemployer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innovation.mobileemployer.Professionals;
import com.innovation.mobileemployer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.ProfessionalViewHolder> {

    private Context context;
    private List<Professionals> professionalsList;

    public ProfessionalAdapter(Context context, List<Professionals> professionalsList) {
        this.context = context;
        this.professionalsList = professionalsList;
    }

    @NonNull
    @Override
    public ProfessionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_booking, parent, false);
        return new ProfessionalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalViewHolder holder, int position) {
        Professionals professional = professionalsList.get(position);

        // Load image using Picasso (replace with Glide if preferred)
        Picasso.get().load(professional.getImageUrl()).into(holder.imageView);

        holder.nameTextView.setText(professional.getUsername());
        holder.categoryTextView.setText(professional.getCategory());
        // Add other relevant information

        // You can set click listener or any other customization based on your requirements
    }

    @Override
    public int getItemCount() {
        return professionalsList.size();
    }

    static class ProfessionalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView categoryTextView;

        public ProfessionalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            // Add other views as needed
        }
    }
}
