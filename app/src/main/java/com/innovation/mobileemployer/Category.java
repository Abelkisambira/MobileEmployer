package com.innovation.mobileemployer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Category implements Parcelable {
    private String id;
    private String name;

    private String imageUrl;
    private List<String> subcategories;

    // Empty constructor needed for Parcelable
    public Category() {
    }

    public Category(String id, String name, String imageUrl ,List<String> subcategories) {
        this.id = id;
        this.name = name;
        this.imageUrl=imageUrl;
        this.subcategories = subcategories;
    }

    // Parcelable implementation
    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl=in.readString();
        subcategories = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeStringList(subcategories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    // Getters and setters for id, imageUrl, name, and subcategories

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }
}
