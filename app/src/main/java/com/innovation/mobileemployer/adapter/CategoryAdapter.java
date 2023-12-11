package com.innovation.mobileemployer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innovation.mobileemployer.Category;
import com.innovation.mobileemployer.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate the layout for each item in the GridView
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_category, parent, false);

            // Create a ViewHolder to store references to the views for this item
            viewHolder = new ViewHolder();
            viewHolder.categoryTextView = convertView.findViewById(R.id.categoryName);
            viewHolder.subcategoriesTextView = convertView.findViewById(R.id.subcategoriesTextView);
            viewHolder.categoryImageView = convertView.findViewById(R.id.categoryImage);

            // Set the ViewHolder as a tag for the convertView for easy access
            convertView.setTag(viewHolder);
        } else {
            // If convertView is not null, reuse the ViewHolder from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data for this position
        Category category = categoryList.get(position);

        // Set data to views
        viewHolder.categoryTextView.setText(category.getName());

        // Handle subcategories (you can customize this part based on your UI)
        List<String> subcategories = category.getSubcategories();
        if (subcategories != null && !subcategories.isEmpty()) {
            String subcategoriesText = TextUtils.join(", ", subcategories);
            viewHolder.subcategoriesTextView.setText("Subcategories: " + subcategoriesText);
            viewHolder.subcategoriesTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.subcategoriesTextView.setVisibility(View.GONE);
        }

        // Load the image using Glide
        Glide.with(context)
                .load(category.getImageUrl()) // Use getImageUrl() from your Category class
                .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                .into(viewHolder.categoryImageView);

        return convertView;
    }


    // ViewHolder pattern to improve performance
    private static class ViewHolder {
        TextView categoryTextView,subcategoriesTextView;
        ImageView categoryImageView;
    }
}
