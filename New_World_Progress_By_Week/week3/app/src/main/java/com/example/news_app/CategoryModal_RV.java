package com.example.news_app;

//this class is for the horizontal  recycle view, which
public class CategoryModal_RV {

    private String category;
    private String categoryImageUrl;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public CategoryModal_RV(String category, String categoryImageUrl) {
        this.category = category;
        this.categoryImageUrl = categoryImageUrl;
    }
}
