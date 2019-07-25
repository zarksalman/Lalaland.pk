package com.lalaland.ecommerce.data.models.globalSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchData {

    @SerializedName("product")
    @Expose
    private List<SearchProduct> product = null;
    @SerializedName("brands")
    @Expose
    private List<SearchBrand> brands = null;
    @SerializedName("category")
    @Expose
    private List<SearchCategory> category = null;

    public List<SearchProduct> getProduct() {
        return product;
    }

    public void setProduct(List<SearchProduct> product) {
        this.product = product;
    }

    public List<SearchBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<SearchBrand> brands) {
        this.brands = brands;
    }

    public List<SearchCategory> getCategory() {
        return category;
    }

    public void setCategory(List<SearchCategory> category) {
        this.category = category;
    }
}