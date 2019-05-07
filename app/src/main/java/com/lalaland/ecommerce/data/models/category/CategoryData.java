package com.lalaland.ecommerce.data.models.category;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryData {

    @SerializedName("cartCount")
    @Expose
    private Integer cartCount;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

}