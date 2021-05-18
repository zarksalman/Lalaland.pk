package com.lalaland.ecommerce.data.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesData {

    @SerializedName("sub_categories")
    @Expose
    private List<SubCategory> subCategories = null;

    @SerializedName("home_banner")
    @Expose
    private List<CategoryHomeBanner> homeBanner = null;

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public List<CategoryHomeBanner> getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(List<CategoryHomeBanner> homeBanner) {
        this.homeBanner = homeBanner;
    }

}