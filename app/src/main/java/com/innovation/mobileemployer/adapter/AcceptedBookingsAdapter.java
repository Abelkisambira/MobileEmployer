package com.innovation.mobileemployer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innovation.mobileemployer.Professionals;
import com.innovation.mobileemployer.R;

import java.util.List;

public class AcceptedBookingsAdapter extends RecyclerView.Adapter<AcceptedBookingsAdapter.ViewHolder> {



    private static List<Professionals> acceptedBookings;
    private static Context context;

    public AcceptedBookingsAdapter(Context context, List<Professionals> acceptedBookings) {
        this.context = context;
        this.acceptedBookings = acceptedBookings;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accepted_booking, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        // Bind data to the views in the item layout
//        Professionals professional = acceptedBookings.get(position);
//
//        // Set other TextViews or views with professional details
//        holder.categoryTextView.setText("Category: " + professional.getCategory());
//        holder.usernameTextView.setText("Username: " + professional.getUsername());
//        // Set other details...
//
//        // Load and display the image using Glide
//        AcceptedBookingsAdapter adapter = new AcceptedBookingsAdapter(context, acceptedBookings);
//        adapter.loadAndDisplayImage(professional.getImageUrl(), holder.imageView);
//    }
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Bind data to the views in the item layout
    Professionals professional = acceptedBookings.get(position);

    // Set other TextViews or views with professional details
    holder.categoryTextView.setText("Category: " + professional.getCategory());
    holder.usernameTextView.setText("Username: " + professional.getUsername());
    // Set other details...

    // Load and display the image using Glide
    loadAndDisplayImage(professional.getImageUrl(), holder.imageView);
}



    @Override
    public int getItemCount() {
        return acceptedBookings != null ? acceptedBookings.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        // Declare views from the item layout
        ImageView professionalImageView;
        TextView usernameTextView;
        TextView categoryTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            // Initialize views from the item layout
            professionalImageView = itemView.findViewById(R.id.professionalImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            imageView=itemView.findViewById(R.id.professionalImageView);
            // Initialize more views as needed



        }
    }
    // Inside AcceptedBookingsAdapter class
    public void loadAndDisplayImage(String imageUrl, ImageView imageView) {
        // Use Glide to load and display the image
        if (imageUrl != null && imageView != null && context != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }





    public void setAcceptedBookings(List<Professionals> acceptedBookings) {
        this.acceptedBookings = acceptedBookings;
        notifyDataSetChanged();
        // Log the size of the acceptedBookings list
        if (acceptedBookings != null) {
            Log.d("AcceptedBookingsAdapter", "Accepted Bookings Size: " + acceptedBookings.size());
        }
    }
}
