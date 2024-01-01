package com.innovation.mobileemployer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innovation.mobileemployer.Professionals;
import com.innovation.mobileemployer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ProfessionalViewHolder> {

    private Context context;
    private List<Professionals> professionals;
    private OnItemClickListener onItemClickListener;

    public CatAdapter(Context context, List<Professionals> professionals) {
        this.context = context;
        this.professionals = professionals;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ProfessionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_professional, parent, false);
        return new ProfessionalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalViewHolder holder, int position) {
        Professionals professional = professionals.get(position);

        // Load image using Picasso (replace with Glide if preferred)
        Picasso.get()
                .load(professional.getImageUrl())
                .into(holder.imageView);


        holder.usernameTextView.setText(professional.getUsername());
        holder.emailTextView.setText("Email: " + professional.getEmail());
        holder.phoneTextView.setText("Phone: " + professional.getPhone());

        // Add RatingBar
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize(0.5f);
        holder.ratingBar.setRating(professional.getTotalRating() / professional.getRatingCount());

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(professional);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return professionals.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Professionals professional);
    }

    static class ProfessionalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView usernameTextView;
        TextView emailTextView;
        TextView phoneTextView;
        RatingBar ratingBar;

        public ProfessionalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.professionalImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
